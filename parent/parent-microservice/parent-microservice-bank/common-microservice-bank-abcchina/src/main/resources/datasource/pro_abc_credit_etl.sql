CREATE OR REPLACE FUNCTION "public"."pro_abc_credit_etl"("taskid" text)
  RETURNS "pg_catalog"."text" AS $BODY$BEGIN
  
    DECLARE
		this_id text;
		p_etl_status text DEFAULT 'success';
		p_etl_error_detail text;
		p_etl_error_content text;
		
	BEGIN
		
		this_id = taskid;

		delete from pro_bank_credit_user_info where task_id = this_id;
		delete from pro_bank_credit_tran_detail where task_id = this_id;
		delete from pro_bank_credit_bill_info where task_id = this_id;

--用户基本信息

		INSERT INTO pro_bank_credit_user_info
		(
			resource,task_id,createtime,basic_user_idnum,basic_user_name,
			credit_limit,card_number,last_number,points,user_name
		)
			WITH 
				card_limit as 
			(
				select 
					a.taskid,
					max(amt_crlm::INT) as credit_limit
				from abcchina_credit_transinfo a
				where a.taskid = this_id
				group by a.taskid
			)
				select 
					'abcchina_credit_userinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					d.credit_limit as credit_limit,
					a.card_number as card_number,
					reverse(substr(reverse(replace(a.card_number,' ','')),1,4)) as last_number,
					a.integral as points,
					replace(a.name,'"','') as user_name
				from abcchina_credit_userinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join card_limit d
					on a.taskid = d.taskid
				where a.taskid = this_id;

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			tran_type
		)
				select 
					'abcchina_credit_transflow:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					case when a.post_amt::numeric < 0 then (abs(a.post_amt::numeric))::text
								else (concat('-',a.post_amt)::numeric)::text end as amount,
					to_char(a.post_date::timestamp,'yyyy-mm-dd') as tran_date,
					a.tr_address as tran_description,
					pro_handle_credit_type(tr_txt) as tran_type
				from abcchina_credit_transflow a 
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

--ALTER FUNCTION "public"."pro_abc_credit_etl"("taskid" text) OWNER TO "lvyuxin";