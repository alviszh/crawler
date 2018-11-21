DROP FUNCTION IF EXISTS cmcc_mobile_etl;

CREATE FUNCTION telecom_fujian_etl(taskid varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,cus_status,user_name
		)
			select 
				'telecom_fujian_userinfo:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				'中国电信' as carrier,
				e.name as basic_user_name,
				e.idnum as basic_id_num,
				b.city as city,
				b.province as province,
				a.phone as phone_num,
				d.month_charge as cur_balance,
				d.myjifen as points,
				f.item_name as cus_level,
				g.item_name as cus_status,
				func_get_split_string(a.name,'：',2) as user_name
			from 
				telecom_fujian_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_cusstatus_item_code g
				on func_get_split_string(a.accountstatus,'：',2) = g.source_name
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_fujian_paymsg:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.paymoney as payfee,
				date_format(a.paydate,'%y-%m-%d') as paytime,
				date_format(a.paydate,'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_fujian_paymsg a left join payinfo_paytype_item_code b
				on a.payditch = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_fujian_messagerhistory:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.oppositephone as other_num,
				a.messageaddr as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				a.sendtime as send_time
			from telecom_fujian_messagerhistory a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.bustype = c.source_name left join smsinfo_smsway_item_code d
				on a.messagetype = d.source_name
			where a.taskid = this_id;

#套餐（服务）信息

		INSERT INTO pro_mobile_service_info
		(
			resource,task_id,createtime,service_name
		)
			select
				'telecom_fujian_taocan_msg:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.businessname as service_name
			from telecom_fujian_taocan_msg a
			where a.taskid = this_id;
#			and position('<' in a.offer_comp_name) = 0

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_fujian_callhistory:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.oppositephone as his_num,
				a.costtotal as fee,
				a.communicateaddr as call_location,
				a.communicatetime2 as call_time,
				c.item_name as call_type,
				null as charge_type,
				handle_timeformat('fujian',communicatetime) as call_duration
			from telecom_fujian_callhistory a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.calltype = c.source_name 
			where a.taskid = this_id;


#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select
				a.taskid as task_id,
				replace(a.monthall,'：','') as sum_fee,
				replace(a.monthfee,' ','') as basic_fee,
				null as message_fee,
				null as normal_call_fee,
				null as roam_call_fee,
				null as flow_fee,
				null as function_fee,
				null as other_fee,
				substr(a.month,1,4) ||'-'|| substr(a.month,5,2) as bill_month
			from telecom_fujian_monthbillhistory a
			where a.taskid = this_id;

	RETURN p_etl_status;

END
;;