CREATE OR REPLACE FUNCTION "public"."pro_bocom_credit_etl"("taskid" text)
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
			credit_limit,balance,credit_limit_usd,cash_limit,card_number,
			last_number,email,phonenum,user_name
		)
				select 
					'bocom_creditcard_billnow:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					a.crd_lmt as credit_limit,
					a.con_can_use_money as balance,
					replace(available_withdrawal_limits_mei,'＄','') as credit_limit_usd,
					available_withdrawal_limits_ren as cash_limit,
					a.card_no as card_number,
					reverse(substr(reverse(replace(a.card_no,' ','')),1,4)) as last_number,
					d.email as email,
					d.mobile as phonenum,
					d.user_name as user_name
				from bocom_creditcard_billnow a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join bocom_creditcard_user d
					on a.taskid = d.taskid 
				where a.taskid = this_id;

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			tran_type
		)
				select 
					'bocom_creditcard_transflow:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					case when pro_handle_credit_type(a.explain) = '还款' 
								then concat('-',split_part(a.trade_currency_amount,' ',2))
								else split_part(a.trade_currency_amount,' ',2) end as amount,
					replace(a.account_date,'/','-') as tran_date,
					a.explain as tran_description,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.explain) as tran_type
				from bocom_creditcard_transflow a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_amount,bill_amount_should,
			bill_amount_min,pay_date
		)
				select 
					'bocom_creditcard_balance:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					replace(a.current_rmb,'¥ ','') as bill_amount,
					replace(a.current_rmb,'¥ ','') as bill_amount_should,
					replace(a.minimal_pay_rmb,'¥ ','') as bill_amount_min,
					replace(replace(a.pay_date,' ',''),'/','-') as pay_date
				from bocom_creditcard_balance a 
				where a.taskid = this_id
				and a.pay_date is not null 
				and a.pay_date <> '--- ';


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

--ALTER FUNCTION "public"."pro_bocom_credit_etl"("taskid" text) OWNER TO "lvyuxin";