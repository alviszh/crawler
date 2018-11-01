CREATE OR REPLACE FUNCTION "public"."pro_ceb_credit_etl"("taskid" text)
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
			bill_date,pay_date,balance,cash_limit,credit_limit,user_name,
			card_number,last_number
		)
				select 
					'cebchina_creditcard_userinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					split_part(a.accountant_bill_date,'-',3) as bill_date,
					split_part(a.repayment_date,'-',3) as pay_date,
					replace(a.balance,',','') as balance,
					replace(a.cashadvance,',','') as cash_limit,
					replace(a.line_of_credit,',','') as credit_limit,
					replace(a.name,',','') as user_name,
					a.num as card_number,
					reverse(substr(reverse(replace(a.num,' ','')),1,4)) as last_number
				from cebchina_creditcard_userinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id 
				where a.taskid = this_id;

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			last_number,tran_type
		)
				select 
					'cebchina_creditcard_billing:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					case when position('存入' in a.money) <> 0
								then concat('-',replace(split_part(a.money,')',2),',',''))
								else replace(replace(a.money,'  ',''),',','') end as amount,
					to_char(a.trade_date::timestamp,'yyyy-mm-dd') as tran_date,
					a.state as tran_description,
					replace(a.num,' ','') as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.state) as tran_type
				from cebchina_creditcard_billing a 
				where a.taskid = this_id
				and a.state is not null;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_amount,bill_amount_should,
			pay_date,bill_date,bill_month,last_number
		)
				select 
					'cebchina_creditcard_consumption:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					replace(a.rmb_money,',','') as bill_amount,
					replace(a.rmb_money,',','') as bill_amount_should,
--					replace(a.current_min_balance,',','') as bill_amount_min,
					replace(a.enddate,'/','-') as pay_date,
					replace(a.month,'/','-') as bill_date,
					replace(substr(a.month,1,7),'/','-') as bill_month,
					reverse(substr(reverse(a.num),1,4)) as last_number
				from cebchina_creditcard_consumption a 
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

--ALTER FUNCTION "public"."pro_ceb_credit_etl"("taskid" text) OWNER TO "lvyuxin";