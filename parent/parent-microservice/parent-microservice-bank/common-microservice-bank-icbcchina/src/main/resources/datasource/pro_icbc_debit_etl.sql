CREATE OR REPLACE FUNCTION "public"."pro_icbc_debit_etl"("taskid" text)
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
			resource,task_id,createtime,basic_id_num,basic_user_name,birthday,
			industry,user_name,nation
		)
			select 
				'icbcchina_debitcard_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.birthday as birthday,
				a.industry as industry,
				a.name as user_name,
				a.nationality	as nation		
			from icbcchina_debitcard_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id 
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,money,balance,account_num,
			tran_date,tran_description
		)
			select
				'icbcchina_debitcard_transflow:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				COALESCE(b.item_name,'其他') as tran_type,
				case when a.account_in = '' then replace(concat('-',a.account_out),',','')
							else replace(a.account_in,',','') end as money,
				replace(a.balance,',','') as balance,
				a.card_num as account_num,
				a.trans_date as tran_date,
				a.trans_place as tran_description
--				a.stract
			from icbcchina_debitcard_transflow a left join debit_tran_type_item_code b
				on a.stract = b.source_name
			where a.taskid = this_id;

--定期存款信息

		INSERT INTO pro_bank_debit_deposit_info
		(
			resource,task_id,createtime,card_num,currency,interest_enddate,
			deposit_type,storge_period,interest_rate,balance
		)
			select
				'icbcchina_debitcard_timedeposit:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.card_num as card_num,
				a.currency as currency,
				a.interest_enddate as interest_enddate,
				a.save_type as deposit_type,
				case when a.storge_period::numeric/12 > 1 
							then concat((a.storge_period::int/12)::text,'年')
							else concat(a.storge_period,'个月') end as storge_period,
				concat(round(a.interest_rate::numeric/1000000,2)::text,'%') as interest_rate,
				round(a.principal::numeric/1000,2) as balance
			from icbcchina_debitcard_timedeposit a 
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

--ALTER FUNCTION "public"."pro_icbc_debit_etl"("taskid" text) OWNER TO "lvyuxin";