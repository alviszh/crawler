DROP FUNCTION IF EXISTS telecom_beijing_etl;
CREATE FUNCTION telecom_beijing_etl(mission_id varchar(50)) RETURNS text CHARSET utf8
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
			resource,task_id,createtime,carrier,basic_user_name,basic_id_num,
			city,province,phone_num,address,contact_num,email,user_name,net_in_date,
			net_in_duration,postcode,cus_type,cus_level,cur_balance,points
		)
			select 
				'telecom_beijing_userinfo:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				'中国电信' as carrier,
				e.name as basic_user_name,
				e.idnum as basic_id_num,
				b.city as city,
				b.province as province,
				b.phonenum as phone_num,
				a.address as address,
				a.contactnumber as contact_num,
				a.email as email,
				a.name as user_name,
				date_format(a.nettime,'%y-%m-%d') as net_in_date,
				DATEDIFF(now(),a.nettime) as net_in_duration,
				a.postcode as postcode,
				g.item_name as cus_type,
				f.item_name as cus_level,
				d.month_charge as cur_balance,
				d.myjifen as points
			from 
				telecom_beijing_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_custype_item_code g
				on a.type = g.source_name
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_beijing_payresult:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.num as payfee,
				date_format(a.bookedtime,'%y-%m-%d') as paytime,
				date_format(a.bookedtime,'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_beijing_payresult a left join payinfo_paytype_item_code b
				on a.paychannels = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_beijing_smsresult:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.othernum as other_num,
				a.costs as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				a.date as send_time
			from telecom_beijing_smsresult a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.type = c.source_name left join smsinfo_smsway_item_code d
				on a.smstype = d.source_name
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_beijing_callresult:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.othernum as his_num,
				a.callcosts as fee,
				a.address as call_location,
				a.startdate as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('beijing',calltime) as call_duration
			from telecom_beijing_callresult a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.calltype1 = c.source_name left join callinfo_chargetype_item_code d
				on a.calltype2 = d.source_name 
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select
				a.taskid as task_id,
				a.bill_number as sum_fee,
				null as basic_fee,
				null as message_fee,
				null as normal_call_fee,
				null as roam_call_fee,
				null as flow_fee,
				null as function_fee,
				null as other_fee,
				replace(replace(a.date,'年','-'),'月','') as bill_month				
			from telecom_beijing_bill a
			where a.taskid = this_id;

	RETURN p_etl_status;

END
;;