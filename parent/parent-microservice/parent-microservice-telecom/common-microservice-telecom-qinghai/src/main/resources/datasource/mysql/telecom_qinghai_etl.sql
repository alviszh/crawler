DROP FUNCTION IF EXISTS telecom_qinghai_etl;

CREATE FUNCTION telecom_qinghai_etl(taskid varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,user_name,package_name
		)
			select 
				'telecom_qinghai_userinfo:'|| a.id as resource,
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
				a.name as user_name,
				a.offer_name as package_name				
			from 
				telecom_qinghai_userinfo a left join task_mobile b
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
				'telecom_qinghai_payresult:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				num as payfee,
				date_format(substr(a.bookedtime,1,8),'%y-%m-%d') as paytime,
				date_format(substr(a.bookedtime,1,8),'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_qinghai_payresult a left join payinfo_paytype_item_code b
				on a.paymethod = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_qinghai_smsresult:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.callphoneother as other_num,
				round(a.callcosts/100,2) as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				concat(date_format(substr(a.date,1,8),'%y-%m-%d'),substr(a.date,9,9)) as send_time
			from telecom_qinghai_smsresult a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.calltype = c.source_name left join smsinfo_smsway_item_code d
				on a.type = d.source_name
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration,his_location
		)
			select 
				'telecom_qinghai_callresult:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				case when a.callphoneother = b.phonenum then a.callphone else a.callphoneother end as his_num,
#				a.oppositephone as his_num,
				a.callcosts as fee,
				e.city as call_location,
				concat(date_format(func_get_split_string(a.date,' ',1),'%y-%m-%d'),' ',func_get_split_string(a.date,' ',2)) as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('qinghai',calltime) as call_duration,
				f.city as his_location
			from telecom_qinghai_callresult a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.type = c.source_name left join callinfo_chargetype_item_code d
				on a.calltype = d.source_name left join city e
				on a.callarea = e.number left join city f
				on a.callareaother = f.number
			where a.taskid = this_id;

		RETURN p_etl_status;

END
;;