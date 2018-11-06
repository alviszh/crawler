CREATE OR REPLACE FUNCTION "public"."pro_cib_debit_etl"("taskid" text)
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
			balance,open_bank,user_name,open_date
		)
			select 
				'cibchina_debitcard_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.acc_number as card_num,
				a.balance as balance,
				a.mechanism as open_bank,
				a.name as user_name,
				a.opening as open_date
			from cibchina_debitcard_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id 
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,account_num,tran_date,
			opposite_account,tran_description,money,balance
		)
			select
				'cibchina_debitcard_transflow:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.item_name as tran_type,
				a.acc_number as account_num,
				a.billing_day as tran_date,
				a.opp_number as opposite_account,
				a.purpose as tran_description,
				case when a.roll_out = '' then concat('-',a.shift_to)
							else a.roll_out end as money,
				a.yue as balance
--				a.digest
			from cibchina_debitcard_transflow a left join debit_tran_type_item_code b
				on a.digest = b.source_name
			where a.taskid = this_id;

--定期存款信息

		INSERT INTO pro_bank_debit_deposit_info
		(
			resource,task_id,createtime,card_num,balance,currency,interest_enddate,
			deposit_type,interest_begindate,storge_period
		)
			select
				'cibchina_debitcard_regular:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.acc_number as card_num,
				a.ava_balance as balance,
				a.currency as currency,
				a.expira_date as interest_enddate,
				a.kindof as deposit_type,
				a.opening as interest_begindate,
				a.period as storge_period
			from cibchina_debitcard_regular a 
			where a.taskid = this_id;
--			and a.type <> '活期'

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
;;
--ALTER FUNCTION "public"."pro_cib_debit_etl"("taskid" text) OWNER TO "lvyuxin";