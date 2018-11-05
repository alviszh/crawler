CREATE OR REPLACE FUNCTION "public"."pro_citic_debit_etl"("taskid" text)
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
			zip_code,email,id_num,user_name,tel_num,card_num,account_status,
			balance
		)
			select 
				'citicchina_debitcard_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.addr as address,
				a.code as zip_code,
				case when split_part(a.email,'     ',1) = '未认证' 
							then '' else split_part(a.email,'     ',1) end as email,
				a.idcard as id_num,
				a.name as user_name,
				a.num as tel_num,
				d.card_num as card_num,
				d.status as account_status,
				d.use_fee as balance
			from citicchina_debitcard_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id left join citicchina_debitcard_regular d
				on a.taskid = d.taskid
			where a.taskid = this_id
			and d.type = '活期';

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,tran_type,account_num,tran_date,
			balance,money,tran_description
		)
			select
				'citicchina_debitcard_account:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				pro_handle_debit_type(a.remark) as tran_type,
				a.card_num as account_num,
				a.datea as tran_date,
				replace(replace(a.fee,',',''),'  ','') as balance,
				case when a.get_money = '--  ' then replace(replace(concat('-',a.set_money),',',''),'  ','')
							else replace(replace(a.get_money,',',''),'  ','') end as money,
				a.remark as tran_description			
			from citicchina_debitcard_account a 
			where a.taskid = this_id;

--定期存款信息

		INSERT INTO pro_bank_debit_deposit_info
		(
			resource,task_id,createtime,balance,currency,deposit_type,
			interest_begindate,interest_enddate,card_num,interest_rate,
			storge_period
		)
			select
				'citicchina_debitcard_regular:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				replace(a.account_fee,',','') as balance,
				a.currency as currency,
				a.type as deposit_type,
				a.start_date as interest_begindate,
				a.end_date as interest_enddate,
				a.card_num as card_num,
				a.do_ratio as interest_rate,
				a.time as storge_period
			from citicchina_debitcard_regular a 
			where a.taskid = this_id
			and a.type <> '活期';

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
--ALTER FUNCTION "public"."pro_citic_debit_etl"("taskid" text) OWNER TO "lvyuxin";