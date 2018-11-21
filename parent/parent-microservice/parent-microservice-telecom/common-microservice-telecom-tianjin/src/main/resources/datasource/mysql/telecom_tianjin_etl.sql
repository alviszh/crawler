DROP FUNCTION IF EXISTS telecom_tianjin_etl;

CREATE FUNCTION telecom_tianjin_etl(taskid varchar(50)) RETURNS text CHARSET utf8
BEGIN
	#Routine body goes here...
	DECLARE	this_id text;
	DECLARE	p_etl_status text DEFAULT 'success';
	DECLARE	p_etl_error_detail text;
	DECLARE	p_etl_error_content text;

	DECLARE EXIT HANDLER FOR SQLWARNING SET @info='ERROR'; 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION SET @info='ERROR';

	set this_id = taskid;

	delete from pro_mobile_bill_info where task_id = this_id;
	delete from pro_mobile_call_info where task_id = this_id;
	delete from pro_mobile_pay_info where task_id = this_id;
	delete from pro_mobile_service_info where task_id = this_id;
	delete from pro_mobile_sms_info where task_id = this_id;
	delete from pro_mobile_user_info where task_id = this_id;

#用户基本信息

		INSERT INTO pro_mobile_user_info
		(
			resource,task_id,createtime,carrier,basic_user_name,basic_id_num,city,
			province,phone_num,cur_balance,points,cus_level,net_in_date,net_in_duration,
			contact_num,user_name,email,id_num,address,postcode,package_name
		)
			select 
				'telecom_tianjin_userinfo:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				'中国电信' as carrier,
				e.name as basic_user_name,
				e.idnum as basic_id_num,
				b.city as city,
				b.province as province,
				b.phonenum as phone_num,
				d.month_charge as cur_balance,
				d.myjifen as points,
				f.item_name as cus_level,
				date_format(a.accesstime,'%y-%m-%d') as net_in_date,
				DATEDIFF(now(),a.accesstime) as net_in_duration,				
				a.contactnum as contact_num,
				a.cusname as user_name,
				a.email as email,
				a.idnum as id_num,
				a.linkaddress as address,
				a.postalcode as postcode,
				g.businessname as package_name
			from 
				telecom_tianjin_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join telecom_tianjin_business g
				on a.taskid = g.taskid
			where a.taskid = this_id
			and g.businesstype = '主套餐';

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_tianjin_chargeinfo:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.chargemoney as payfee,
				date_format(substr(a.chargetime,1,10),'%y-%m-%d') as paytime,
				date_format(substr(a.chargetime,1,10),'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_tianjin_chargeinfo a left join payinfo_paytype_item_code b
				on a.chargetype = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_tianjin_smsrecord:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				case when a.sendnum = b.phonenum then a.sendnum else a.getnum end as host_num,
				case when a.getnum = b.phonenum then a.sendnum else a.getnum  end as other_num,
				totalcost as fee,
				null as sms_type,
				null as sms_way,
				a.sendtime as send_time
			from telecom_tianjin_smsrecord a left join task_mobile b
				on a.taskid = b.taskid 
			where a.taskid = this_id;

#套餐（服务）信息

		INSERT INTO pro_mobile_service_info
		(
			resource,task_id,createtime,service_name
		)
			select
				'telecom_tianjin_business:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.businessname as service_name
			from telecom_tianjin_business a
			where a.taskid = this_id
			and a.businesstype <> '主套餐';

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_tianjin_callrecord:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				case when a.callednum = b.phonenum then a.callingnum else a.callednum end as his_num,
#				a.counter_number as his_num,
				a.totalcharge as fee,
				a.calladdress as call_location,
				a.starttime as call_time,
				c.item_name as call_type,
				null as charge_type,
				handle_timeformat('tianjin',a.costtime) as call_duration
			from telecom_tianjin_callrecord a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.linktype = c.source_name 
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select 
				a.taskid as task_id,
				sum(a.expense) as sum_fee,
				sum(case when b.item_name = '基本套餐费' then a.expense else 0 end) as basic_fee,
				sum(case when b.item_name = '短信彩信费' then a.expense else 0 end) as message_fee,
				sum(case when b.item_name = '国内通话费' then a.expense else 0 end) as normal_call_fee,
				sum(case when b.item_name = '异地通话费' then a.expense else 0 end) as roam_call_fee,
				sum(case when b.item_name = '互联网流量费' then a.expense else 0 end) as flow_fee,
				sum(case when b.item_name = '业务功能费' then a.expense else 0 end) as function_fee,
				sum(case when b.item_name = '其他费用' then a.expense else 0 end) as other_fee,
#				substr(a.countmonth,1,4) ||'-'|| substr(a.countmonth,5,2) as bill_month
#				a.yearmonth as bill_month
				replace(replace(querymonth,'年','-'),'月','') as bill_month
			from telecom_tianjin_monthbill a inner join billinfo_feetype_item_code b
				on a.expensename = source_name
			where a.taskid = this_id
			group by 
				replace(replace(querymonth,'年','-'),'月',''),
				a.taskid;

	RETURN p_etl_status;

END
;;