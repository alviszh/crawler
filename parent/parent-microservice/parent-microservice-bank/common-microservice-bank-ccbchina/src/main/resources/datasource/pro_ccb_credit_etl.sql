CREATE OR REPLACE FUNCTION "public"."pro_ccb_credit_etl"("taskid" text)
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
			credit_limit,cash_limit,bill_date,pay_date
		)
				select 
					'ccbchina_creditcard_account_type:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					replace(a.credit_line,',','') as credit_limit,
					replace(a.cash_amount,',','') as cash_limit,
--					replace(a.current_balance,',','') as balance,
					split_part(a.tally_date,'/',3) as bill_date,
					split_part(a.due_date,'/',3) as pay_date
				from ccbchina_creditcard_account_type a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id 
				where a.taskid = this_id
				and a.due_date = 
						(select max(due_date) from ccbchina_creditcard_account_type bb
							where bb.taskid = this_id)
				and currency = '人民币';

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			last_number,tran_type
		)
				select 
					'ccbchina_creditcard_transflow:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					a.deal_money as amount,
					replace(a.deal_date,'/','-') as tran_date,
					a.deal_description as tran_description,
					a.four_card_num as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.deal_description) as tran_type
				from ccbchina_creditcard_transflow a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_amount,bill_amount_should,
			bill_amount_min,pay_date,bill_date,bill_month,last_number
		)
				select 
					'ccbchina_creditcard_account_type:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					replace(a.current_balance,',','') as bill_amount,
					replace(a.current_balance,',','') as bill_amount_should,
					replace(a.current_min_balance,',','') as bill_amount_min,
					replace(a.due_date,'/','-') as pay_date,
					replace(a.tally_date,'/','-') as bill_date,
					replace(substr(a.tally_date,1,7),'/','-') as bill_month,
					reverse(substr(reverse(a.card_num),1,4)) as last_number
				from ccbchina_creditcard_account_type a 
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

;;--ALTER FUNCTION "public"."pro_ccb_credit_etl"("taskid" text) OWNER TO "lvyuxin";