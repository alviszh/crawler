CREATE OR REPLACE FUNCTION "public"."pro_cib_credit_etl"("taskid" text)
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
			balance,cash_limit,cash_limit_usd,user_name,credit_limit,credit_limit_usd,
			card_number,last_number,email
		)
				select 
					'cibchina_creditcard_userinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					replace(a.available_limit,',','') as balance,
					replace(a.cash_limit,',','') as cash_limit,
					replace(a.cash_limit_dollar,',','') as cash_limit_usd,
					a.card_holder as user_name,
					replace(a.credit_limit,',','') as credit_limit,
					replace(a.credit_line_dollar,',','') as credit_limit_usd,
					replace(a.card_number,' ','') as card_number,
					reverse(substr(reverse(replace(a.card_number,' ','')),1,4)) as last_number,
					a.email as email
				from cibchina_creditcard_userinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join cibchina_creditcard_bill_info d
					on a.taskid = d.taskid
				where a.taskid = this_id
				and d.bill_end_date = (select max(bill_end_date) from cibchina_creditcard_bill_info bb 
																where bb.taskid = this_id);

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			last_number,tran_type
		)
				select 
					'cibchina_creditcard_trans_detail:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					a.fee as amount,
					case when concat(extract(year from now())::text,'-',a.charge_date)::timestamp <= a.createtime::timestamp
								then concat(extract(year from now())::text,'-',a.charge_date) 
								else concat(extract(year from now()-INTERVAL '1 year')::text,'-',a.charge_date) 
								end as tran_date,
					a.abstracts as tran_description,
					a.last_number as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.abstracts) as tran_type
				from cibchina_creditcard_trans_detail a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_date,pay_date,bill_amount_min,
			bill_amount,bill_amount_should,last_number,bill_month
		)
				select 
					'cibchina_creditcard_bill_info:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					a.bill_date as bill_date,
					a.bill_end_date as pay_date,
					a.bill_least_pay as bill_amount_min,
					a.bill_pay as bill_amount,
					a.bill_should_pay as bill_amount_should,
					reverse(substr(reverse(replace(a.card_number,' ','')),1,4)) as last_number,
					substr(a.bill_date,1,7) as bill_month
				from cibchina_creditcard_bill_info a 
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

--ALTER FUNCTION "public"."pro_cib_credit_etl"("taskid" text) OWNER TO "lvyuxin";