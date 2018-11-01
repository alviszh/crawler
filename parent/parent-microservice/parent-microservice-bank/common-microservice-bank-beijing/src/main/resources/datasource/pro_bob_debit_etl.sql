CREATE OR REPLACE FUNCTION "public"."pro_bob_debit_etl"("taskid" text)
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
			resource,task_id,createtime,basic_id_num,basic_user_name,account_status,
			card_num,balance,open_bank,open_date,user_name
		)
			select 
				'beijingbank_debitcard_account:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.account_status as account_status,
				a.acct_no as card_num,
				a.balances as balance,
				a.bank_name as open_bank,
				replace(a.open_date,'/','-') as open_date,
				a.username as user_name
			from beijingbank_debitcard_account a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id 
			where a.taskid = this_id
			and a.product_name = '活期';

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,account_num,balance,tran_description,
			money,tran_date
		)
			select
				'beijingbank_debitcard_transflow:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.item_name as tran_type,
				a.acct_no as account_num,
				replace(a.balance,',','') as balance,
				a.summary as tran_description,
				case when b.item_name = '支出' then replace(concat('-',a.trans_amount),',','')
						 else replace(a.trans_amount,',','') end as money,
				replace(a.trans_time,'/','-') as tran_date
--				a.trans_type
			from beijingbank_debitcard_transflow a left join debit_tran_type_item_code b
				on a.trans_type = b.source_name
			where a.taskid = this_id;

--定期存款信息

		INSERT INTO pro_bank_debit_deposit_info
		(
			resource,task_id,createtime,card_num,balance,storge_period,interest_begindate,
			interest_enddate,deposit_type,interest_rate,currency
		)
			select
				'beijingbank_debitcard_account:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.acct_no as card_num,
				replace(a.balances,',','') as balance,
				a.deposit_term as storge_period, 
				replace(a.open_date,'/','-') as interest_begindate,
				replace(a.due_date,'/','-') as interest_enddate,
				a.product_name as deposit_type,
				a.strike_rate as interest_rate,
				a.currency as currency
			from beijingbank_debitcard_account a 
			where a.taskid = this_id
			and a.product_name <> '活期';

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

--ALTER FUNCTION "public"."pro_bob_debit_etl"("taskid" text) OWNER TO "lvyuxin";