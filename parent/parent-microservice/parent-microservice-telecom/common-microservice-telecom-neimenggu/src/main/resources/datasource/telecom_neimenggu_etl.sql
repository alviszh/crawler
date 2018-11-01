CREATE OR REPLACE FUNCTION "public"."telecom_neimenggu_etl"("taskid" text)
  RETURNS "pg_catalog"."text" AS $BODY$   
BEGIN  
  DECLARE
		this_id text;
		p_etl_status text DEFAULT 'success';
		p_etl_error_detail text;
		p_etl_error_content text;
		
	BEGIN
		this_id = taskid;


--清除此taskid的数据

		delete from pro_mobile_bill_info where task_id = this_id;
		delete from pro_mobile_call_info where task_id = this_id;
		delete from pro_mobile_pay_info where task_id = this_id;
		delete from pro_mobile_service_info where task_id = this_id;
		delete from pro_mobile_sms_info where task_id = this_id;
		delete from pro_mobile_user_info where task_id = this_id;



-------------------------开始插入数据--------------------------------



---用户基本信息

		INSERT INTO pro_mobile_user_info
		(
			resource,task_id,createtime,carrier,basic_user_name,basic_id_num,city,
			province,phone_num,cur_balance,points,cus_level,certificate_type,cus_status,
			address,id_num,user_name,net_in_date,net_in_duration
		)
			select 
				'telecom_neimenggu_userinfo:'|| a.id::TEXT as resource,
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
				h.item_name as certificate_type,
				g.item_name as cus_status,
				a.addr as address,
				a.cardid as id_num,
				a.name as user_name,
				to_char(a.nettime::TIMESTAMP,'yyyy-mm-dd') as net_in_date,
				date_part('day',CURRENT_DATE-a.nettime::TIMESTAMP) as net_in_duration
			from 
				telecom_neimenggu_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join telecom_common_starlevel c
				on a.taskid = c.taskid left join telecom_common_pointsandcharges d
				on a.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name left join userinfo_certificatetype_item_code h
				on a.paperstype = h.source_name left join userinfo_cusstatus_item_code g
				on a.accountstatus = g.source_name
			where a.taskid = this_id;

--缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_neimenggu_paymsg:' || a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.paymoney as payfee,
				to_char(a.paydate::TIMESTAMP,'yyyy-mm-dd') as paytime,
				to_char(a.paydate::TIMESTAMP,'yyyy-mm') as paymonth,
				b.item_name as payway
			from telecom_neimenggu_paymsg a left join payinfo_paytype_item_code b
				on a.payway = b.source_name
			where a.taskid = this_id;

--短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_neimenggu_messagerhistory:' || a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.oppositephone as other_num,
				a.messageaddr as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				concat(substr(a.sendtime,1,10),' ',substr(a.sendtime,11,8)) as send_time
			from telecom_neimenggu_messagerhistory a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.bustype = c.source_name left join smsinfo_smsway_item_code d
				on a.messagetype = d.source_name
			where a.taskid = this_id;

--套餐（服务）信息

		INSERT INTO pro_mobile_service_info
		(
			resource,task_id,createtime,service_name
		)
			select
				'telecom_neimenggu_taocan_msg:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.businessname as service_name
			from telecom_neimenggu_taocan_msg a
			where a.taskid = this_id;


--通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration
		)
			select 
				'telecom_neimenggu_callhistory:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.oppositephone as his_num,
				a.costtotal as fee,
				a.communicateaddr as call_location,
				substr(a.communicatetime2,1,10) ||' '|| substr(a.communicatetime2,11,8) as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('neimenggu',communicatetime) as call_duration
			from telecom_neimenggu_callhistory a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.calltype = c.source_name left join callinfo_chargetype_item_code d
				on a.correspondencetype = d.source_name 
			where a.taskid = this_id;

--月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select
				a.taskid as task_id,
				monthall as sum_fee,
				mealfee as basic_fee,
				msgfee as message_fee,
				inlandfee as normal_call_fee,
				null as roam_call_fee,
				netphonefee as flow_fee,
				null as function_fee,
				null as other_fee,
--				a.month as bill_month
--				substr(a.month,1,4) ||'-'|| substr(a.month,5,2) as bill_month
--				replace(substr(split_part(paydate,'-',1),1,7),'/','-')
--				to_char(concat(split_part(a.month,'：',2),'01')::timestamp,'yyyy-mm') as bill_month
				replace(replace(month,'年','-'),'月','') as bill_month
			from telecom_neimenggu_monthbillhistory a
			where a.taskid = this_id;

--更新此任务状态

	--		UPDATE task_mobile a set a.etltime = now(),a.etl_status = p_etl_status where a.taskid = this_id;

			RETURN p_etl_status;
		
--异常处理
	EXCEPTION
		WHEN QUERY_CANCELED THEN 
--获取错误详情
			GET STACKED DIAGNOSTICS p_etl_error_detail = MESSAGE_TEXT;
			GET STACKED DIAGNOSTICS p_etl_error_content = PG_EXCEPTION_CONTEXT;
--记录错误到task表
--			UPDATE task_mobile a set a.etl_status = p_etl_status where a.taskid = this_id;
--返回失败
			p_etl_status = p_etl_error_content ||' : '|| p_etl_error_detail;

			RETURN p_etl_status;
			

		WHEN OTHERS THEN 
--获取错误详情
			GET STACKED DIAGNOSTICS p_etl_error_detail = MESSAGE_TEXT;
			GET STACKED DIAGNOSTICS p_etl_error_content = PG_EXCEPTION_CONTEXT;
--记录错误到task表
--			UPDATE task_mobile a set a.etl_status = p_etl_status where a.taskid = this_id;
--返回失败
			p_etl_status = p_etl_error_content ||' : '|| p_etl_error_detail;

			RETURN p_etl_status;

	END;
END;   
$BODY$
  LANGUAGE 'plpgsql' VOLATILE COST 100
;

--ALTER FUNCTION "public"."telecom_neimenggu_etl"("taskid" text) OWNER TO "TXDB";
;;