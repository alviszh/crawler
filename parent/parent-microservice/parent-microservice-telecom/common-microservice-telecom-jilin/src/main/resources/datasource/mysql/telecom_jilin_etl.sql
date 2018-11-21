DROP FUNCTION IF EXISTS telecom_jilin_etl;

CREATE FUNCTION telecom_jilin_etl(mission_id varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,address,contact_num,id_num,certificate_type,
			user_name,package_name,cus_level,cus_type
		)
			select 
				'telecom_jilin_userinfo:'|| a.id as resource,
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
				case when a.address = '无' then '' else a.address end as address,
				a.contact_tel as contact_num,
				a.id_num as id_num,
				h.item_name as certificate_type,
				a.name as user_name,
				a.plan_name as package_name,
				f.item_name as cus_level,
				g.item_name as cus_type
			from 
				telecom_jilin_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_custype_item_code g
				on a.type = g.source_name left join userinfo_certificatetype_item_code h
				on a.id_type = h.source_name
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_jilin_payment:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.pay_count as payfee,
				date_format(substr(a.pay_date,1,10),'%y-%m-%d') as paytime,
				date_format(substr(a.pay_date,1,10),'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_jilin_payment a left join payinfo_paytype_item_code b
				on a.pay_type = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_jilin_smsdetails:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.receive_num as other_num,
				a.fee as fee,
				null as sms_type,
				null as sms_way,
				replace(a.send_date,'T','') as send_time
			from telecom_jilin_smsdetails a left join task_mobile b
				on a.taskid = b.taskid 
			where a.taskid = this_id;

#套餐（服务）信息

		INSERT INTO pro_mobile_service_info
		(
			task_id,createtime,service_name
		)
			select
#				'telecom_jilin_increment:'|| a.id::TEXT as resource,
			distinct
				a.taskid as task_id,
				date_format(a.createtime,'%y-%m-%d %H:%i') || ':00' as createtime,
				a.service_name as service_name
			from telecom_jilin_increment a
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration,his_location
		)
			select 
				'telecom_jilin_calldetails:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.called_num as his_num,
				round(a.call_fee/100,2) as fee,
				e.city as call_location,
				replace(a.start_date,'T',' ') as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('jilin',call_time) as call_duration,
				f.city as his_location
			from telecom_jilin_calldetails a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.call_type = c.source_name left join callinfo_chargetype_item_code d
				on a.type = d.source_name left join city e
				on a.attribution = e.number left join city f
				on a.other_attribution = f.number
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select 
				a.taskid as task_id,
				sum(a.fee) as sum_fee,
				sum(case when b.item_name = '基本套餐费' then a.fee else 0 end) as basic_fee,
				sum(case when b.item_name = '短信彩信费' then a.fee else 0 end) as message_fee,
				sum(case when b.item_name = '国内通话费' then a.fee else 0 end) as normal_call_fee,
				sum(case when b.item_name = '异地通话费' then a.fee else 0 end) as roam_call_fee,
				sum(case when b.item_name = '互联网流量费' then a.fee else 0 end) as flow_fee,
				sum(case when b.item_name = '业务功能费' then a.fee else 0 end) as function_fee,
				sum(case when b.item_name = '其他费用' then a.fee else 0 end) as other_fee,
				substr(a.bill_date,1,4) ||'-'|| substr(a.bill_date,5,2) as bill_month
#				a.yearmonth as bill_month
			from telecom_jilin_monthbill a left join billinfo_feetype_item_code b
				on a.bill_type = source_name
			where a.taskid = this_id
			group by 
				substr(a.bill_date,1,4) ||'-'|| substr(a.bill_date,5,2),
				a.taskid;

		RETURN p_etl_status;

END
;;