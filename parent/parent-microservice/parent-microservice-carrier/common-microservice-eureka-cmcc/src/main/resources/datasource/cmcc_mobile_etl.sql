CREATE OR REPLACE FUNCTION "public"."cmcc_mobile_etl"("taskid" text)
  RETURNS "pg_catalog"."text" AS $BODY$
  BEGIN
  DECLARE
		this_id text;
		p_etl_status text DEFAULT 'success';
		p_etl_error_detail text;
		p_etl_error_content text;

	BEGIN
		
		this_id = taskid;


--清除此taskid的数据 12

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
			resource,task_id,basic_user_name,basic_id_num,user_name,phone_num,
			city,province,createtime,address,package_name,contact_num,cur_balance,
			net_in_date,net_in_duration,points,cus_level,cus_status,postcode,carrier,
			real_name
		)
			select
				'cmcc_userinfo:'|| a.id::TEXT as resource,
				a.task_id as task_id,
				c.name as basic_user_name,
				c.idnum as basic_id_num,
				a.name as user_name,
				b.phonenum as phone_num,
				b.city as city,
				b.province as province,
				a.createtime as createtime,
				a.address as address,
				a.next_plan_name as package_name,
				case when a.contact_num = '未登记' then '' else a.contact_num end as contact_num,
				a.cur_fee_total as cur_balance,
				to_char(substr(a.in_net_date,1,8)::timestamp,'yyyy-mm-dd') as net_in_date,
				date_part('day',CURRENT_DATE-substr(a.in_net_date,1,8)::timestamp) as net_in_duration,
				a.point_value as points,
				d.item_name as star_level,
				e.item_name as status,
				a.zip_code as postcode,
				'中国移动' as carrier,
				case when a.real_name_info = '1' then '未登记'
						 when a.real_name_info = '2' then '已登记'
						 when a.real_name_info = '3' then '已审核'
						 else '未知' end as real_name
			from cmcc_userinfo a left join task_mobile b
				on a.task_id = b.taskid left join basic_user c
				on b.basic_user_id = c.id left join userinfo_cuslevel_item_code d
				on a.star_level = d.source_name left join userinfo_cusstatus_item_code e
				on a.status = e.source_name
			where a.task_id = this_id;

--缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'cmcc_paymsg_result:' || a.id::text as resource,
				a.task_id as task_id,
				a.createtime as createtime,
				a.pay_fee as payfee,
				to_char(substr(a.pay_date,1,8)::TIMESTAMP,'yyyy-mm-dd') as paytime,
				to_char(substr(a.pay_date,1,8)::TIMESTAMP,'yyyy-mm') as paymonth,
				b.item_name as payway
			from cmcc_paymsg_result a left join payinfo_paytype_item_code b
				on a.pay_type_name = b.source_name
			where a.task_id = this_id;

--短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'cmcc_smsmsg_result:' || a.id::text as resource,
				a.task_id as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.another_nm as other_num,
				a.comm_fee as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				case when length(a.start_time) = 14
							then replace(concat(a.start_year,'-',a.start_time),'/','') 
							else replace(a.start_time,'/','') end as send_time
			from cmcc_smsmsg_result a left join task_mobile b
				on a.task_id = b.taskid left join smsinfo_smstype_item_code c
				on a.info_type = c.source_name left join smsinfo_smsway_item_code d
				on a.comm_mode = d.source_name
			where a.task_id = this_id;

--通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,createtime,task_id,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration,comment
		)
			select 
				'cmcc_user_call:'||a.id::text as resource,
				a.createtime as createtime,
				a.task_id as task_id,
				b.phonenum as phone_num,
				a.another_nm as his_num,
				a.comm_fee as fee,
				case when position(']' in a.comm_plac) <> 0 
							then split_part(a.comm_plac,']',2) 
							else a.comm_plac end as call_location,
				case when length(a.start_time) = 14 
							then concat(a.start_year,'-',a.start_time) 
							else a.start_time end as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('cmcc',comm_time) as call_duration,
				a.meal_favorable as comment
			from cmcc_user_call a left join task_mobile b
				on a.task_id = b.taskid left join callinfo_calltype_item_code c
				on a.comm_mode = c.source_name left join callinfo_chargetype_item_code d
				on a.comm_type = d.source_name
			where a.task_id = this_id;

--月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select
				a.task_id as task_id,
				a.bill_fee as sum_fee,
				null as basic_fee,
				null as message_fee,
				null as normal_call_fee,
				null as roam_call_fee,
				null as flow_fee,
				null as function_fee,
				null as other_fee,
				substr(a.bill_month,1,4) ||'-'|| substr(a.bill_month,5,2) as bill_month				
			from cmcc_checkmsg_result a
			where a.task_id = this_id;


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

--ALTER FUNCTION "public"."cmcc_mobile_etl"("taskid" text) OWNER TO "TXDB";
;;--请不要删除这一行，前面的两个分号和application.yaml文件中的配置spring.datasource.separator的分隔符对应