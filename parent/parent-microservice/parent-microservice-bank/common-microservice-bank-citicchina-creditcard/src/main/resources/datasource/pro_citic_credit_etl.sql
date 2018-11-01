CREATE OR REPLACE FUNCTION "public"."pro_citic_credit_etl"("taskid" text)
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
			address,phonenum,user_name,balance,email,last_number,card_number,
			pay_date,bill_date
		)
				select 
					'citicchina_creditcard_userinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					a.addr as address,
					a.phone_num as phonenum,
					a.name as user_name,
					a.available as balance,
					a.email as email,
					a.last_number as last_number,
					d.card_num as card_number,
					split_part(replace(replace(replace(d.repay_datea,'年','/'),'月','/'),'日 ',''),'/',3) as pay_date,
					split_part(replace(replace(replace(d.this_datea,'年','/'),'月','/'),'日 ',''),'/',3) as bill_date
				from citicchina_creditcard_userinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join citicchina_creditcard_bill d
					on a.taskid = d.taskid
				where a.taskid = this_id
				and replace(replace(replace(d.repay_datea,'年','/'),'月','/'),'日 ','') 
							= (select max(replace(replace(replace(repay_datea,'年','/'),'月','/'),'日 ','')) 
									from citicchina_creditcard_bill bb
									where bb.taskid = this_id);

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			tran_type
		)
				select 
					'citicchina_creditcard_account:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					replace(split_part(a.sum,' /',1),',','') as amount,
					a.datea as tran_date,
					a.description as tran_description,
--					a.last_number as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.description) as tran_type
				from citicchina_creditcard_account a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_date,pay_date,bill_amount_min,
			bill_amount,bill_amount_should,last_number,bill_month,available_limit
		)
				select 
					'citicchina_creditcard_bill:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					replace(replace(replace(replace(a.this_datea,'年','-'),'月','-'),'日',''),' ','') as bill_date,
					replace(replace(replace(replace(a.repay_datea,'年','-'),'月','-'),'日',''),' ','') as pay_date,
					replace(a.lowst_repay,',','') as bill_amount_min,
					replace(a.repay,',','') as bill_amount,
					replace(a.repay,',','') as bill_amount_should,
					reverse(substr(reverse(a.card_num),1,4)) as last_number,
					replace(replace(replace(replace(substr(a.this_datea,1,7),'年','-'),'月','-'),'日',''),' ','') as bill_month,
					a.credit_avail_1 as available_limit
				from citicchina_creditcard_bill a 
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

--ALTER FUNCTION "public"."pro_citic_credit_etl"("taskid" text) OWNER TO "lvyuxin";