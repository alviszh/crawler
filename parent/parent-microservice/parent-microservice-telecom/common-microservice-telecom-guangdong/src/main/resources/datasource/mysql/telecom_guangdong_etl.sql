DROP FUNCTION IF EXISTS telecom_guangdong_etl;

CREATE FUNCTION telecom_guangdong_etl(taskid varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,certificate_type,education,
			email,id_num,postcode,qq,user_name,address
		)
			select 
				'telecom_guangdong_userinfo:'|| a.id as resource,
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
				g.item_name as certificate_type,
				a.education as education,
				a.email as email,
				a.indent_code as id_num,
				a.post_code as postcode,
				a.qq_number as qq,
				a.username as user_name,
				a.user_address as address
			from 
				telecom_guangdong_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_certificatetype_item_code g
				on a.indent_nbr_type = g.source_name
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_guangdong_payment:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.money as payfee,
				date_format(substr(a.pretime,1,8),'%y-%m-%d') as paytime,
				date_format(substr(a.pretime,1,8),'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_guangdong_payment a left join payinfo_paytype_item_code b
				on a.paymode = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_guangdong_smsthrem:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.dnumber as other_num,
				a.smsmoney as fee,
				c.item_name as sms_type,
				null as sms_way,
				a.smsdate as send_time
			from telecom_guangdong_smsthrem a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.smstype = c.source_name 
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_guangdong_callthrem:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.dialnumber as his_num,
				a.callmoney as fee,
				a.callland as call_location,
				a.calldate as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('guangdong',duration) as call_duration
			from telecom_guangdong_callthrem a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.calltype = c.source_name left join callinfo_chargetype_item_code d
				on a.datecalltype = d.source_name 
			where a.taskid = this_id;

	RETURN p_etl_status;

END
;;