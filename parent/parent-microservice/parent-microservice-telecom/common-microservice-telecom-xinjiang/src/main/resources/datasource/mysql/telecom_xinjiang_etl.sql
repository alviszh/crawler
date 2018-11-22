DROP FUNCTION IF EXISTS telecom_xinjiang_etl;

CREATE FUNCTION telecom_xinjiang_etl(taskid varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,id_num,cus_type,address,email,
			contact_num,net_in_date,net_in_duration,postcode,user_name,cus_level
		)
			select 
				'telecom_xinjiang_userinfo:'|| a.id as resource,
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
				a.cert_number as id_num,
				g.item_name as cus_type,
				a.contact_address as address,
				a.contact_email as email,
				replace(a.contact_phone,'+','') as contact_num,
				date_format(substr(a.create_date,1,8),'%y-%m-%d') as net_in_date,
				datediff(now(),substr(a.create_date,1,8)) as net_in_duration,
				a.post_cod as postcode,
				replace(a.username,'.','') as user_name,
				f.item_name as cus_level
			from 
				telecom_xinjiang_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_custype_item_code g
				on a.cert_type = g.source_name
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_xinjiang_rechargerecord:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.account_amount as payfee,
				date_format(substr(a.account_time,1,8),'%y-%m-%d') as paytime,
				date_format(substr(a.account_time,1,8),'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_xinjiang_rechargerecord a left join payinfo_paytype_item_code b
				on a.account_type = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_xinjiang_smsrecord:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.sms_number as other_num,
				a.total_fee as fee,
				c.item_name as sms_type,
				null as sms_way,
				a.send_time as send_time
			from telecom_xinjiang_smsrecord a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.sms_type = c.source_name 
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_xinjiang_voicerecord:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
#				case when a.callednum = b.phonenum then a.callingnum else a.callednum end as his_num,
				a.called_number as his_num,
				a.total as fee,
				a.call_palce as call_location,
				a.start_time as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('xinjiang',a.talk_time) as call_duration
			from telecom_xinjiang_voicerecord a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.type = c.source_name left join callinfo_chargetype_item_code d
				on a.call_type = d.source_name 
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select 
				a.taskid as task_id,
				sum(a.item_value) as sum_fee,
				sum(case when b.item_name = '基本套餐费' then a.item_value else 0 end) as basic_fee,
				sum(case when b.item_name = '短信彩信费' then a.item_value else 0 end) as message_fee,
				sum(case when b.item_name = '国内通话费' then a.item_value else 0 end) as normal_call_fee,
				sum(case when b.item_name = '异地通话费' then a.item_value else 0 end) as roam_call_fee,
				sum(case when b.item_name = '互联网流量费' then a.item_value else 0 end) as flow_fee,
				sum(case when b.item_name = '业务功能费' then a.item_value else 0 end) as function_fee,
				sum(case when b.item_name = '其他费用' then a.item_value else 0 end) as other_fee,
				substr(a.month,1,4) ||'-'|| substr(a.month,5,2) as bill_month
#				a.yearmonth as bill_month
#				replace(replace(querymonth,'年','-'),'月','') as bill_month
			from telecom_xinjiang_paymonths a inner join billinfo_feetype_item_code b
				on a.item_name = source_name
			where a.taskid = this_id
			group by 
				substr(a.month,1,4) ||'-'|| substr(a.month,5,2),
				a.taskid;

		RETURN p_etl_status;

END
;;