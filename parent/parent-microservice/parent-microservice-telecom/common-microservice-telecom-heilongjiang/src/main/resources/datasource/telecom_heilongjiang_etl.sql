CREATE OR REPLACE FUNCTION "public"."telecom_heilongjiang_etl"("taskid" text)
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
			province,phone_num,cur_balance,points,cus_level
		)
			select 
				'task_mobile:'|| b.id::TEXT as resource,
				b.taskid as task_id,
				b.createtime as createtime,
				'中国电信' as carrier,
				e.name as basic_user_name,
				e.idnum as basic_id_num,
				b.city as city,
				b.province as province,
				b.phonenum as phone_num,
				d.month_charge as cur_balance,
				d.myjifen as points,
				f.item_name as cus_level
			from 
				task_mobile b left join telecom_common_starlevel c
				on b.taskid = c.taskid left join telecom_common_pointsandcharges d
				on b.taskid = d.taskid left join basic_user e
				on b.basic_user_id = e.id left join userinfo_cuslevel_item_code f
				on c.membership_level = f.source_name 
				where b.taskid = this_id;

--缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'telecom_heilongjiang_payresult:' || a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				paynum as payfee,
				to_char(substr(a.paydate,1,8)::TIMESTAMP,'yyyy-mm-dd') as paytime,
				to_char(substr(a.paydate,1,8)::TIMESTAMP,'yyyy-mm') as paymonth,
				b.item_name as payway
			from telecom_heilongjiang_payresult a left join payinfo_paytype_item_code b
				on a.type = b.source_name
			where a.taskid = this_id;


--通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,
			call_duration
		)
			select 
				'telecom_heilongjiang_callresult:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.othernum as his_num,
				'' as fee,
				a.guishudi as call_location,
				concat(substr(a.date,1,4),'-',substr(a.date,5,2),'-',substr(a.date,7,2),' ',
							 substr(a.date,9,2),':',substr(a.date,11,2),':',substr(a.date,13,2)) as call_time,
				c.item_name as call_type,
				handle_timeformat('heilongjiang',time) as call_duration
			from telecom_heilongjiang_callresult a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.teletype = c.source_name 
			where a.guishudi is not null
			and a.taskid = this_id;

--月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select
				a.taskid as task_id,
				round(replace(a.bussrent,'  ','')::NUMERIC+replace(a.internetchinapay,'  ','')::NUMERIC,2) as sum_fee,
				replace(a.bussrent,'  ','') as basic_fee,
				null as message_fee,
				null as normal_call_fee,
				null as roam_call_fee,
				replace(a.internetchinapay,'  ','') as flow_fee,
				null as function_fee,
				null as other_fee,
--				a.month as bill_month
--				substr(a.month,1,4) ||'-'|| substr(a.month,5,2) as bill_month
				replace(substr(split_part(paydate,'-',1),1,7),'/','-') as bill_month
			from telecom_heilongjiang_customresult a
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

--ALTER FUNCTION "public"."telecom_heilongjiang_etl"("taskid" text) OWNER TO "TXDB";
;;