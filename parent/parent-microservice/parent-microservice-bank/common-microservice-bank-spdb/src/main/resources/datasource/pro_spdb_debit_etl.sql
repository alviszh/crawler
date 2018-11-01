CREATE OR REPLACE FUNCTION "public"."pro_spdb_debit_etl"("taskid" text)
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
			user_name,zip_code,tel_num,account_status,balance
		)
			select 
				'spdb_debitcard_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.address as address,
				a.customer_name as user_name,
				a.postal_code as zip_code,
				a.telephone_no as tel_num,
				d.account_status as account_status, 
				d.can_use_balance	as balance		
			from spdb_debitcard_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id left join spdb_debitcard_accountinfo d
				on a.taskid = d.taskid
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,tran_description,account_num,
			balance,money,tran_date
		)
			select
				'spdb_debitcard_transflow:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				pro_handle_debit_type(a.summary) as tran_type,
				a.summary as tran_description,
				a.acct_no as account_num,
				a.balance as balance,
				case when a.deposit_amount = '' then concat('-',a.take_amount)
							else a.deposit_amount end as money,
				replace(substr(a.trans_time,1,10),'/','-') as tran_date
			from spdb_debitcard_transflow a 
			where a.taskid = this_id;

--定期存款信息

		INSERT INTO pro_bank_debit_deposit_info
		(
			resource,task_id,createtime,card_num,balance,currency,interest_enddate,
			interest_rate,storge_period,deposit_type,interest_begindate
		)
			select
				'spdb_debitcard_regular:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.acct_no as card_num,
				a.balance as balance,
				a.currency as currency,
				replace(a.due_date,'/','-') as interest_enddate,
				concat(round(replace(a.rate,'%','')::numeric,2)::text,'%') as interest_rate,
				replace(a.save_term,' ','') as storge_period,
				split_part(a.savings_type,' ',2) as deposit_type,
				replace(replace(a.start_data,'/','-'),'  ','') as interest_begindate
			from spdb_debitcard_regular a 
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

--ALTER FUNCTION "public"."pro_spdb_debit_etl"("taskid" text) OWNER TO "lvyuxin";