CREATE OR REPLACE FUNCTION "public"."pro_boc_credit_etl"("taskid" text)
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
			cash_limit_usd,credit_limit_usd,cash_limit,credit_limit,balance,
			user_name,card_number,last_number,address,bill_date
		)
			WITH 
				card_limit_usd as 
			(
				select 
					a.taskid,
					a.cash_limit as cash_limit_usd,
					a.total_limt as credit_limit_usd
				from bocchina_cebitcard_userbill a
				where a.taskid = this_id
				and a.currency = '014'
			),
				card_limit as 
			(
				select 
					a.taskid,
					a.cash_limit as cash_limit,
					a.total_limt as credit_limit,
					a.toltal_balance as balance
				from bocchina_cebitcard_userbill a
				where a.taskid = this_id
				and a.currency = '001'
			),
				repay_date_info as 
			(
				select 
					a.taskid,
					a.address1||a.address2||a.address3 as address
--					split_part(max(repay_date),'/',3) as pay_date
				from bocchina_cebitcard_customerinfo a
				where a.taskid = this_id
				group by 
					a.taskid,
					a.address1||a.address2||a.address3
			)
				select 
					'bocchina_cebitcard_userinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					d.cash_limit_usd,
					d.credit_limit_usd,
					e.cash_limit,
					e.credit_limit,
					e.balance,
					a.acct_name as user_name,
					a.acct_num as card_number,
					reverse(substr(reverse(replace(a.acct_num,' ','')),1,4)) as last_number,
					f.address as address,
--					f.pay_date as pay_date,
					a.bill_date as bill_date					
				from bocchina_cebitcard_userinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join card_limit_usd d
					on a.taskid = d.taskid left join card_limit e
					on a.taskid = e.taskid left join repay_date_info f
					on a.taskid = f.taskid
				where a.taskid = this_id;

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			last_number,tran_type
		)
				select 
					'bocchina_cebitcard_trans_list:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					case when a.loan_sign = 'DEBT' then concat('-',a.deal_cnt)
							 else a.deal_cnt end as amount,
					replace(a.check_dt,'/','-') as tran_date,
					a.deal_desc as tran_description,
					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.deal_desc) as tran_type
				from bocchina_cebitcard_trans_list a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,available_limit,bill_date,bill_month,
			bill_amount,bill_amount_should,bill_amount_min,last_number
		)
				select 
					'bocchina_cebitcard_accountinfolist:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					a.avai_bal as available_limit,
					replace(a.bill_dt,'/','-') as bill_date,
					replace(substr(a.bill_dt,1,7),'/','-') as bill_month,
					a.period_availble_credit_limit as bill_amount,
					a.period_availble_credit_limit as bill_amount_should,
					a.lowest_repay_amount as bill_amount_min,
					reverse(substr(reverse(a.card_no),1,4)) as last_number
				from bocchina_cebitcard_accountinfolist a 
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

--ALTER FUNCTION "public"."pro_boc_credit_etl"("taskid" text) OWNER TO "lvyuxin";