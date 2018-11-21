﻿DROP FUNCTION IF EXISTS telecom_hunan_etl;

CREATE FUNCTION telecom_hunan_etl(taskid varchar(50)) RETURNS text CHARSET utf8
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
				'telecom_hunan_userinfo:'|| a.id as resource,
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
				telecom_hunan_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_cusstatus_item_code g
				on a.accountstatus = g.source_name
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_hunan_paymsg:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				replace(replace(a.paymoney,'元',''),'￥','') as payfee,
				date_format(a.paydate,'%y-%m-%d') as paytime,
				date_format(a.paydate,'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_hunan_paymsg a left join payinfo_paytype_item_code b
				on a.payditch = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_hunan_messagerhistory:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.oppositephone as other_num,
				round(a.messageaddr,2) as fee,
				c.item_name as sms_type,
				null as sms_way,
				sendtime as send_time
			from telecom_hunan_messagerhistory a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.bustype = c.source_name 
			where a.taskid = this_id;

#套餐（服务）信息

		INSERT INTO pro_mobile_service_info
		(
			resource,task_id,createtime,service_name
		)
			select
				'telecom_hunan_taocan_msg:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.businessname as service_name
			from telecom_hunan_taocan_msg a
			where a.taskid = this_id;
#			and position('<' in a.offer_comp_name) = 0

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_hunan_callhistory:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.oppositephone as his_num,
				round(a.costtotal/100,2) as fee,
				a.communicateaddr as call_location,
				a.communicatetime2 as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('hunan',communicatetime) as call_duration
			from telecom_hunan_callhistory a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.calltype = c.source_name left join callinfo_chargetype_item_code d
				on a.correspondencetype = d.source_name 
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select
				a.taskid as task_id,
				replace(split_part(a.monthall,'为',2),'元。','') as sum_fee,
				monthfee as basic_fee,
				msgfee as message_fee,
				null as normal_call_fee,
				null as roam_call_fee,
				null as flow_fee,
				null as function_fee,
				null as other_fee,
#				a.month as bill_month
#				substr(a.month,1,4) ||'-'|| substr(a.month,5,2) as bill_month
#				replace(substr(split_part(paydate,'-',1),1,7),'/','-')
				date_format(concat(func_get_split_string(a.month,'：',2),'01'),'%y-%m') as bill_month
			from telecom_hunan_monthbillhistory a
			where a.taskid = this_id;

		RETURN p_etl_status;

END
;;