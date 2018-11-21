DROP FUNCTION IF EXISTS telecom_guangxi_etl;

CREATE FUNCTION telecom_guangxi_etl(taskid varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,qq,address,user_name,
			email,fax,contact_num,postcode,weibo
		)
			select 
				'telecom_guangxi_userinfo:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				'中国电信' as carrier,
				e.name as basic_user_name,
				e.idnum as basic_id_num,
				b.city as city,
				b.province as province,
				a.phone_no as phone_num,
				d.month_charge as cur_balance,
				d.myjifen as points,
				f.item_name as cus_level,
				a.qqno as qq,
				a.contact_adderss as address,
				a.contact_name as user_name,
				a.email as email,
				a.fax as fax,
				a.home_phone as contact_num,
				a.post_code as postcode,
				a.wei_bo as weibo
			from 
				telecom_guangxi_userinfo a left join task_mobile b
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
				'telecom_guangxi_pay:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				round(a.money/100,2) as payfee,
				date_format(a.datea,'%y-%m-%d') as paytime,
				date_format(a.datea,'%y-%') as paymonth,
				b.item_name as payway
			from telecom_guangxi_pay a left join payinfo_paytype_item_code b
				on a.type = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_guangxi_message:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.his_num as other_num,
				a.behind_money as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				a.start_time as send_time
			from telecom_guangxi_message a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.name_type = c.source_name left join smsinfo_smsway_item_code d
				on a.get_type = d.source_name
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_guangxi_call:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.his_num as his_num,
				a.money as fee,
				a.addr as call_location,
				a.start_time as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('guangxi',call_time) as call_duration
			from telecom_guangxi_call a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.call_type = c.source_name left join callinfo_chargetype_item_code d
				on a.call_status = d.source_name 
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select
				a.taskid as task_id,
				replace(a.sum_money,'    ','') as sum_fee,
				meal_money as basic_fee,
				message_money as message_fee,
				land_money as normal_call_fee,
				four_money as roam_call_fee,
				net_money as flow_fee,
				null as function_fee,
				red_money as other_fee,
#				a.month as bill_month
				substr(a.month,1,4) ||'-'|| substr(a.month,5,2) as bill_month
			from telecom_guangxi_bill a
			where a.taskid = this_id;

	RETURN p_etl_status;

END
;;