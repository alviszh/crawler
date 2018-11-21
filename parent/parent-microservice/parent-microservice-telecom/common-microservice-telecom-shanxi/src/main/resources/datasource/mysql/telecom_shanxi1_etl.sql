DROP FUNCTION IF EXISTS telecom_shanxi1_etl;

CREATE FUNCTION telecom_shanxi1_etl(mission_id varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,certificate_type,cus_type,
			cus_status,address,net_in_date,net_in_duration,education,email,hobby,id_num,
			user_name,occupation,postcode,gender,package_name
		)
			select 
				'telecom_shanxi1_userinfo:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				'中国电信' as carrier,
				e.name as basic_user_name,
				e.idnum as basic_id_num,
				b.city as city,
				b.province as province,
				a.home_phone as phone_num,
				d.month_charge as cur_balance,
				d.myjifen as points,
				f.item_name as cus_level,
				h.item_name as certificate_type,
				g.item_name as cus_type,
				k.item_name as cus_status,
				a.address as address,
				date_format(a.create_date,'%y-%m-%d') as net_in_date,
				DATEDIFF(now(),a.create_date) as net_in_duration,
				a.education as education,
				a.email as email,
				a.hobby as hobby,
				a.id_num as id_num,
				a.name as user_name,
				a.occupation as occupation,
				a.postcode as postcode,
				a.sex as gender,
				j.product_name as package_name
			from 
				telecom_shanxi1_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_certificatetype_item_code h
				on a.documen_type = h.source_name left join userinfo_custype_item_code g
				on a.customer_type = g.source_name left join telecom_shanxi1_account j
				on a.taskid = j.taskid left join userinfo_cusstatus_item_code k
				on j.product_status_cd_name = k.source_name
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_shanxi1_payinfo:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.fee as payfee,
				date_format(a.payment_date,'%y-%m-%d') as paytime,
				date_format(a.payment_date,'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_shanxi1_payinfo a left join payinfo_paytype_item_code b
				on a.payment_type = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_shanxi1_message:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.sms_mobile as other_num,
				a.sms_cost as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				a.sms_time as send_time
			from telecom_shanxi1_message a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.sms_type = c.source_name left join smsinfo_smsway_item_code d
				on a.sms_style = d.source_name
			where a.taskid = this_id;

#套餐（服务）信息

		INSERT INTO pro_mobile_service_info
		(
			resource,task_id,createtime,service_name
		)
			select
				'telecom_shanxi1_order:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.offer_name as service_name
			from telecom_shanxi1_order a
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_shanxi1_callrecord:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
#				case when a.called_num = b.phonenum then a.calling_num else a.called_num end as his_num,
				a.call_mobile as his_num,
				a.call_fee as fee,
				a.call_area as call_location,
				a.call_time as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('shanxi1',a.call_time_cost) as call_duration
			from telecom_shanxi1_callrecord a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.call_style = c.source_name left join callinfo_chargetype_item_code d
				on a.call_type = d.source_name 
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select 
				a.taskid as task_id,
				sum(replace(a.fee,'元','')) as sum_fee,
				sum(case when b.item_name = '基本套餐费' then replace(a.fee,'元','') else 0 end) as basic_fee,
				sum(case when b.item_name = '短信彩信费' then replace(a.fee,'元','') else 0 end) as message_fee,
				sum(case when b.item_name = '国内通话费' then replace(a.fee,'元','') else 0 end) as normal_call_fee,
				sum(case when b.item_name = '异地通话费' then replace(a.fee,'元','') else 0 end) as roam_call_fee,
				sum(case when b.item_name = '互联网流量费' then replace(a.fee,'元','') else 0 end) as flow_fee,
				sum(case when b.item_name = '业务功能费' then replace(a.fee,'元','') else 0 end) as function_fee,
				sum(case when b.item_name = '其他费用' then replace(a.fee,'元','') else 0 end) as other_fee,
				substr(a.date,1,4) ||'-'|| substr(a.date,5,2) as bill_month
#				a.yearmonth as bill_month
			from telecom_shanxi1_bill a inner join billinfo_feetype_item_code b
				on a.payment_type = source_name
			where a.taskid = this_id
			group by 
				substr(a.date,1,4) ||'-'|| substr(a.date,5,2),
				a.taskid;

		RETURN p_etl_status;

END
;;