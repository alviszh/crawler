CREATE OR REPLACE FUNCTION "public"."telecom_qinghai_etl"("taskid" text)
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
			province,phone_num,cur_balance,points,cus_level,user_name,package_name
		)
			select 
				'telecom_qinghai_userinfo:'|| a.id::TEXT as resource,
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


--缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_qinghai_payresult:' || a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				num as payfee,
				to_char(substr(a.bookedtime,1,8)::TIMESTAMP,'yyyy-mm-dd') as paytime,
				to_char(substr(a.bookedtime,1,8)::TIMESTAMP,'yyyy-mm') as paymonth,
				b.item_name as payway
			from telecom_qinghai_payresult a left join payinfo_paytype_item_code b
				on a.paymethod = b.source_name
			where a.taskid = this_id;

--短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'telecom_qinghai_smsresult:' || a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.callphoneother as other_num,
				round(a.callcosts::numeric/100,2) as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				concat(to_char(substr(a.date,1,8)::timestamp,'yyyy-mm-dd'),substr(a.date,9,9)) as send_time
			from telecom_qinghai_smsresult a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.calltype = c.source_name left join smsinfo_smsway_item_code d
				on a.type = d.source_name
			where a.taskid = this_id;

--通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration,his_location
		)
			select 
				'telecom_qinghai_callresult:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				case when a.callphoneother = b.phonenum then a.callphone else a.callphoneother end as his_num,
--				a.oppositephone as his_num,
				a.callcosts as fee,
				e.city as call_location,
				concat(to_char(split_part(a.date,' ',1)::timestamp,'yyyy-mm-dd'),' ',split_part(a.date,' ',2)) as call_time,
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

--ALTER FUNCTION "public"."telecom_qinghai_etl"("taskid" text) OWNER TO "TXDB";
;;