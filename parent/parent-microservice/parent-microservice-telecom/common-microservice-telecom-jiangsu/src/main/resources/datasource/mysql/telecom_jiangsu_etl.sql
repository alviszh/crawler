﻿DROP FUNCTION IF EXISTS telecom_jiangsu_etl;

CREATE FUNCTION telecom_jiangsu_etl(mission_id varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,certificate_type,contact_num,
			user_name,postcode,address,id_num,email
		)
			select 
				'telecom_jiangsu_userinfo:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				'中国电信' as carrier,
				e.name as basic_user_name,
				e.idnum as basic_id_num,
				b.city as city,
				b.province as province,
				b.phonenum as phone_num,
				a.blance as cur_balance,
				a.scores as points,
				f.item_name as cus_level,
				g.item_name as certificate_type,
				a.contact_phone as contact_num,
				a.user_name as user_name,
				a.zip_code as postcode,
				a.user_address as address,
				a.document_code as id_num,
				a.email as email
			from 
				telecom_jiangsu_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_certificatetype_item_code g
				on a.document_type = g.source_name
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_jiangsu_pay:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				round(a.dcc_payment_amount/100,2) as payfee,
				date_format(substr(a.dcc_paid_time,1,8),'%y-%m-%d') as paytime,
				date_format(substr(a.dcc_paid_time,1,8),'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_jiangsu_pay a left join payinfo_paytype_item_code b
				on a.dcc_charge_source_id = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_jiangsu_message:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.acc_nbr as other_num,
				a.ticket_charge_ch as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				concat(start_date_new,' ',start_time_new) as send_time
			from telecom_jiangsu_message a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.ticket_type = c.source_name left join smsinfo_smsway_item_code d
				on a.ticket_type = d.source_name
			where a.taskid = this_id;

#套餐（服务）信息

		INSERT INTO pro_mobile_service_info
		(
			resource,task_id,createtime,service_name
		)
			select
				'telecom_jiangsu_business:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.offer_spec_name as service_name
			from telecom_jiangsu_business a
			where a.taskid = this_id;
#			and position('<' in a.offer_comp_name) = 0

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_jiangsu_callrecord:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.acc_nbr as his_num,
				a.ticket_charge_ch as fee,
				e.city as call_location,
				concat(start_date_new,' ',a.start_time_new) as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('jiangsu',duration_ch) as call_duration
			from telecom_jiangsu_callrecord a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.ticket_type_new = c.source_name left join callinfo_chargetype_item_code d
				on a.duration_type = d.source_name left join city e
				on a.area_code = e.number 
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select 
				a.taskid as task_id,
				sum(a.item_charge) as sum_fee,
				sum(case when b.item_name = '基本套餐费' then a.item_charge else 0 end) as basic_fee,
				sum(case when b.item_name = '短信彩信费' then a.item_charge else 0 end) as message_fee,
				sum(case when b.item_name = '国内通话费' then a.item_charge else 0 end) as normal_call_fee,
				sum(case when b.item_name = '异地通话费' then a.item_charge else 0 end) as roam_call_fee,
				sum(case when b.item_name = '互联网流量费' then a.item_charge else 0 end) as flow_fee,
				sum(case when b.item_name = '业务功能费' then a.item_charge else 0 end) as function_fee,
				sum(case when b.item_name = '其他费用' then a.item_charge else 0 end) as other_fee,
#				substr(a.cycle,1,4) ||'-'|| substr(a.cycle,5,2) as bill_month
				a.yearmonth as bill_month
			from telecom_jiangsu_bill a left join billinfo_feetype_item_code b
				on a.item_name = source_name
			where a.taskid = this_id
			group by 
				a.yearmonth,
				a.taskid;

		RETURN p_etl_status;

END
;;