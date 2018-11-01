CREATE OR REPLACE FUNCTION "public"."pro_cgb_debit_etl"("taskid" text)
  RETURNS "pg_catalog"."text" AS $BODY$BEGIN  
  
  DECLARE
		this_id text;
		p_etl_status text DEFAULT 'success';
		p_etl_error_detail text;
		p_etl_error_content text;
			
	BEGIN

		this_id = taskid;

		delete from pro_bank_debit_user_info where task_id = this_id;
		delete from pro_bank_debit_tran_detail where task_id = this_id;
		delete from pro_bank_debit_deposit_info where task_id = this_id;


--用户基本信息

		INSERT INTO pro_bank_debit_user_info
		(
			resource,task_id,createtime,basic_id_num,basic_user_name,address,
			birthday,email,user_name,tel_num,balance,card_num
		)
			select 
				'cgbchina_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				case when a.attr = 'null' then '' else a.attr end as address,
				case when a.date = 'null' then '' else a.date end as birthday,
				case when a.email = 'null' then '' else a.email end as email,
				a.name as user_name,
				case when a.phone = 'null' then '' else a.phone end as tel_num,
				d.balance::numeric as balance,
				d.number as card_num
			from cgbchina_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id left join cgbchina_accounttype d
				on a.taskid = d.taskid
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,tran_date,opposite_account,
			money,balance
		)
			select
				'cgbchina_transflow:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.item_name as tran_type,
				to_char(substr(a.deal_time,1,8)::timestamp,'yyyy-mm-dd') as tran_date,
				a.opp_number as opposite_account,
				case when a.roll_out is null then replace(a.shift_to,'分','')::numeric/100
						 else replace(concat('-',a.roll_out),'分','')::numeric/100 end as money,
				replace(a.yue,'分','')::numeric/100 as balance
--				a.digest
			from cgbchina_transflow a left join debit_tran_type_item_code b
				on a.digest = b.source_name
			where a.taskid = this_id;

--更新此任务状态

--			UPDATE task_mobile a set a.etltime = now(),a.etl_status = p_etl_status where a.taskid = this_id;

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

--ALTER FUNCTION "public"."pro_cgb_debit_etl"("taskid" text) OWNER TO "lvyuxin";