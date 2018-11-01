CREATE OR REPLACE FUNCTION "public"."pro_ceb_debit_etl"("taskid" text)
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
			resource,task_id,createtime,basic_id_num,basic_user_name,native_place,
			id_num,open_date,address,tel_num,user_name,zip_code,open_bank,card_num		
		)
			select 
				'cebchina_debitcard_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.address as native_place,
				a.idcare as id_num,
				to_char(a.khrq::timestamp,'yyyy-mm-dd') as open_date,
				a.lxaddress as address,
				a.lxphone as tel_num,
				a.name as user_name,
				a.postal as zip_code,
				a.kainame as open_bank,
				replace(a.kanum,' ','') as card_num
			from cebchina_debitcard_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id 
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,tran_description,
			balance,money,tran_date,account_num,opposite_account
		)
			select
				'cebchina_debitcard_transflow:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				pro_handle_debit_type(a.abstracts) as tran_type,
				a.abstracts as tran_description,
				replace(a.balance,',','') as balance,
				case when a.amount <> '--' then replace(concat('-',a.amount),',','')
							else replace(a.debit,',','') end money,
				a.jydate as tran_date,
				replace(a.num,' ','') as account_num,
				a.reciprocal_account as opposite_account
			from cebchina_debitcard_transflow a 
			where a.taskid = this_id;

--定期存款信息

		INSERT INTO pro_bank_debit_deposit_info
		(
			resource,task_id,createtime,balance,currency,deposit_type,
			interest_begindate,interest_enddate
		)
			select
				'cebchina_debitcard_deadline:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				replace(a.balance,',','') as balance,
				a.currency as currency,
				a.deadline as deposit_type,
				to_char(a.kdate::timestamp,'yyyy-mm-dd') as interest_begindate,
				to_char(a.enddate::timestamp,'yyyy-mm-dd') as interest_enddate
			from cebchina_debitcard_deadline a 
			where a.taskid = this_id
			and a.type <> '普通活期'
			and a.type <> '电子现金';

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

--ALTER FUNCTION "public"."pro_ceb_debit_etl"("taskid" text) OWNER TO "lvyuxin";