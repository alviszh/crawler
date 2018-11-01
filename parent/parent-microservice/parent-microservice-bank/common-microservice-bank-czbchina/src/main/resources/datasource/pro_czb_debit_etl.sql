CREATE OR REPLACE FUNCTION "public"."pro_czb_debit_etl"("taskid" text)
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
			resource,task_id,createtime,basic_id_num,basic_user_name,balance,
			card_num,open_date
		)
			select 
				'czbchina_debitcard_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.balance as balance,
				replace(a.card_number,' ','') as card_num,
				replace(replace(replace(a.deposit_time,'年','-'),'月','-'),'日','') as open_date
			from czbchina_debitcard_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id 
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,balance,money,
			opposite_account,tran_date
		)
			select
				'czbchina_debitcard_billdetails:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.item_name as tran_type,
				a.balance as balance,
				a.fee as money,
				a.opposite_card_number as opposite_account,
				substr(a.tran_date,1,10) as tran_date
--				a.digest
			from czbchina_debitcard_billdetails a left join debit_tran_type_item_code b
				on a.trans_decription = b.source_name
			where a.taskid = this_id;

--定期存款信息

		INSERT INTO pro_bank_debit_deposit_info
		(
			resource,task_id,createtime,balance,deposit_type,currency,
			interest_begindate,interest_enddate,interest_rate,storge_period
		)
			select
				'czbchina_debitcard_deposit:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.balance as balance,
				a.card_type as deposit_type,
				a.currency as currency,
				a.deposit_time as interest_begindate,
				a.interest_dnddate as interest_enddate,
				concat(round(a.interest_rate::numeric,2)::text,'%')as interest_rate,
				a.storge_period as storge_period
			from czbchina_debitcard_deposit a 
			where a.taskid = this_id
			and a.num <> '1';

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

;;--ALTER FUNCTION "public"."pro_czb_debit_etl"("taskid" text) OWNER TO "lvyuxin";