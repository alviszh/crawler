DROP FUNCTION IF EXISTS telecom_hainan_etl;

CREATE FUNCTION telecom_hainan_etl(mission_id varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,certificate_type,address,
			id_num,user_name,package_name
		)
			select 
				'telecom_hainan_userinfo:'|| a.id as resource,
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
				a.address as address,
				a.useridcard as id_num,
				a.username as user_name,
				h.nwkname as package_name
			from 
				telecom_hainan_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_certificatetype_item_code g
				on a.usertype = g.source_name left join telecom_hainan_business h
				on a.taskid = h.taskid
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_hainan_pay:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				replace(a.ruzhangmon,'元','') as payfee,
				date_format(a.ruzhangtime,'%y-%m-%d') as paytime,
				date_format(a.ruzhangtime,'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_hainan_pay a left join payinfo_paytype_item_code b
				on a.channel = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_hainan_smsresult:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.smsothercall as other_num,
				a.smscost as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				a.date as send_time
			from telecom_hainan_smsresult a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.businesstype = c.source_name left join smsinfo_smsway_item_code d
				on a.smstype = d.source_name
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_hainan_callresult:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.callphoneother as his_num,
				a.callcosts as fee,
				a.calllocation as call_location,
				a.date as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('hainan',concat(a.calltimeminute,':',a.calltimesecond)) as call_duration
			from telecom_hainan_callresult a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.type = c.source_name left join callinfo_chargetype_item_code d
				on a.calltype = d.source_name
			where a.taskid = this_id;


	RETURN p_etl_status;

END
;;