DROP FUNCTION IF EXISTS telecom_shanghai_etl;

CREATE FUNCTION telecom_shanghai_etl(taskid varchar(50)) RETURNS text CHARSET utf8
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
			province,phone_num,cur_balance,points,cus_level,certificate_type,cus_status,
			cus_type,net_in_date,net_in_duration,email,id_num,contact_num,address,postcode,
			package_name
		)
			select 
				'telecom_shanghai_userinfo:'|| a.id as resource,
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
				h.item_name as cus_status,
				i.item_name as cus_type,
				a.cust_sincedt as net_in_date,
				DATEDIFF(now(),a.cust_sincedt) as net_in_duration,
				a.main_email_addr as email,
				a.main_iden_number as id_num,
				replace(a.main_ph_num,'+','') as contact_num,
				a.pr_addr_name as address,
				a.pr_addr_zip_code as postcode,
				j.parent_promotion_product_name as package_name
			from 
				telecom_shanghai_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_certificatetype_item_code g
				on a.main_iden_type = g.source_name left join userinfo_cusstatus_item_code h
				on a.cust_status = h.source_name left join userinfo_custype_item_code i
				on a.cust_type = i.source_name left join telecom_shanghai_account j
				on a.taskid = j.taskid
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_shanghai_payfee:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.store_in_amount as payfee,
				date_format(a.partner_trans_date,'%y-%m-%d') as paytime,
				date_format(a.partner_trans_date,'%y-%m') as paymonth,
				b.item_name as payway
			from telecom_shanghai_payfee a left join payinfo_paytype_item_code b
				on a.operation_type = b.source_name
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_shanghai_msg:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.target_party as other_num,
				a.fee1 as fee,
				null as sms_type,
				c.item_name as sms_way,
				a.begin_time as send_time
			from telecom_shanghai_msg a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smsway_item_code c
				on a.call_type = c.source_name 
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration,his_location
		)
			select 
				'telecom_shanghai_callresult:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
#				case when a.called_num = b.phonenum then a.calling_num else a.called_num end as his_num,
				a.target_party as his_num,
				a.total_fee as fee,
				case when position('.' in a.calling_party_visited_city) <> 0 
							then split_part(a.calling_party_visited_city,'.',2) 
							else a.calling_party_visited_city end as call_location,
				a.begin_time as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('shanghai',a.call_duriation) as call_duration,
				case when position('.' in a.called_party_visited_city) <> 0 
							then func_get_split_string(a.called_party_visited_city,'.',2) 
							else a.called_party_visited_city end as his_location
			from telecom_shanghai_callresult a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.call_type = c.source_name left join callinfo_chargetype_item_code d
				on a.long_distance_type = d.source_name 
			where a.taskid = this_id;

		RETURN p_etl_status;

END
;;