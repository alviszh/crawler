CREATE OR REPLACE FUNCTION "public"."pro_bohc_debit_etl"("taskid" text)
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
			resource,task_id,createtime,basic_id_num,basic_user_name,user_name,
			card_num,open_bank,open_date,balance,account_status
		)
			select 
				'bohcchina_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.acname as user_name,
				a.card_no as card_num,
				a.city_name as open_bank,
				a.open_date as open_date,
				a.usableyue as balance,
				d.call_stat as account_status
			from bohcchina_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id left join bohcchina_accounttype d
				on a.taskid = d.taskid
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,balance,opposite_account,
			tran_date,money,tran_description
		)
			select
				'bohcchina_transflow:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.item_name as tran_type,
				replace(a.bal,',','') as balance,
				case when a.opp_acct_no = 'null' then '' 
							else a.opp_acct_no end as opposite_account,
				substr(a.deal_time,1,10) as tran_date,
				replace(a.tran_amt,',','') as money,
				a.tran_brief as tran_description
--				a.trans_type
			from bohcchina_transflow a left join debit_tran_type_item_code b
				on a.memo = b.source_name
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

--ALTER FUNCTION "public"."pro_bohc_debit_etl"("taskid" text) OWNER TO "lvyuxin";