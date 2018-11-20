DROP FUNCTION IF EXISTS telecom_yunnan_etl;

CREATE FUNCTION telecom_yunnan_etl(mission_id varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,email,id_num,user_name,
			postcode,net_in_date,net_in_duration,address
		)
			select 
				'telecom_yunnan_userinfo:'|| a.id as resource,
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
				a.useremail as email,
				a.useridcard as id_num,
				a.username as user_name,
				a.userpostcode as postcode,
				date_format(a.userstardate,'%y-%m-%d') as net_in_date,
				date_diff(now(),a.userstardate) as net_in_duration,
				a.address as address
			from 
				telecom_yunnan_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name 
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_yunnan_pay:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				num as payfee,
				date_format(a.bookedtime,'%y-%m-%d') as paytime,
				date_format(a.bookedtime,'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_yunnan_pay a left join payinfo_paytype_item_code b
				on a.paymethod = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_yunnan_smsresult:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				func_get_split_string(a.callphoneother,')',2) as other_num,
				a.callcosts as fee,
				null as sms_type,
				c.item_name as sms_way,
				substr(a.date,1,10) || ' ' || substr(a.date,12,8) as send_time
			from telecom_yunnan_smsresult a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smsway_item_code c
				on a.calltype = c.source_name 
			where a.taskid = this_id;


#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_yunnan_callresult:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
#				case when a.callednum = b.phonenum then a.callingnum else a.callednum end as his_num,
				func_get_split_string(a.callphoneother,')',2) as his_num,
				a.callcosts as fee,
				a.calllocation as call_location,
				substr(a.date,1,10) || ' ' || substr(a.date,12,8) as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('yunnan',a.calltime) as call_duration
			from telecom_yunnan_callresult a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.calltype = c.source_name left join callinfo_chargetype_item_code d
				on a.calltype = d.source_name 
			where a.taskid = this_id;

		RETURN p_etl_status;

END
;;