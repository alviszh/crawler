CREATE OR REPLACE FUNCTION "public"."pro_hfb_debit_etl"("taskid" text)
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
			resource,task_id,createtime,basic_id_num,basic_user_name,card_num,
			account_status,balance,user_name,open_bank,tel_num
		)
			select 
				'hfbchina_debitcard_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.account as card_num,
				a.account_state as account_status,
				replace(a.balance,'￥ ','') as balance,
				a.card_holder as user_name,
				a.deposit_bank as open_bank,
				a.mobile as tel_num				
			from hfbchina_debitcard_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id 
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,balance,money,opposite_account,
			tran_description,tran_date
		)
			select
				'hfbchina_debitcard_billdetails:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.item_name as tran_type,
				a.balance as balance,
				case when a.currentaccount_type = '转入' then a.money
						 else concat('-',a.money) end as money,
				a.opposite_card_number as opposite_account,
				a.purpose as tran_description,
				a.tran_date as tran_date
	--			a.currentaccount_type
			from hfbchina_debitcard_billdetails a left join debit_tran_type_item_code b
				on a.currentaccount_type = b.source_name
			where a.taskid = this_id;

--定期存款信息

		INSERT INTO pro_bank_debit_deposit_info
		(
			resource,task_id,createtime,balance,currency,interest_rate,
			storge_period,deposit_type,interest_begindate,interest_enddate
		)
			select
				'hfbchina_debitcard_deposit:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				case when a.account_amount = '' then a.deposit
							else a.account_amount end as balance,
				a.currency as currency,
				a.interest_rate as interest_rate,
				a.storge_period as storge_period,
				a.deposit_type as deposit_type,
				a.deposit_date as interest_begindate,
				a.due_date as interest_enddate
			from hfbchina_debitcard_deposit a 
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

;;--ALTER FUNCTION "public"."pro_hfb_debit_etl"("taskid" text) OWNER TO "lvyuxin";