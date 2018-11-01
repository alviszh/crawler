CREATE OR REPLACE FUNCTION "public"."pro_icbc_credit_etl"("taskid" text)
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
			balance,bill_date,card_number,last_number,pay_date,credit_limit
		)
				select 
					'icbcchina_creditcard_userinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					replace(a.balance,',','') as balance,
					replace(a.bill_day,'日','') as bill_date,
					a.card_num as card_number,
					reverse(substr(reverse(a.card_num),1,4)) as last_number,
					split_part(replace(replace(replace(a.repay_date,'年','-'),'月','-'),'日',''),'-',3) as pay_date,
					replace(split_part(d.credit_line,'/',1),',','') as credit_limit
				from icbcchina_creditcard_userinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join icbcchina_creditcard_monthbill d
					on a.taskid = d.taskid
				where a.taskid = this_id
				and replace(replace(replace(d.bill_day,'年','-'),'月','-'),'日','')
							=  (select max(replace(replace(replace(bill_day,'年','-'),'月','-'),'日','')) from icbcchina_creditcard_monthbill bb 
																			where bb.taskid = this_id);

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			last_number,tran_type
		)
				select 
					'icbcchina_creditcard_transflow:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					case when position('支出' in a.account_amount) <> 0 
								then replace(split_part(a.account_amount,'/',1),',','')
								else replace(concat('-',split_part(a.account_amount,'/',1)),',','') end as amount,
					a.trading_day as tran_date,
					a.merchant_name as tran_description,
					a.card_last_num as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.trading_type) as tran_type
				from icbcchina_creditcard_transflow a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			task_id,createtime,bill_date,pay_date,bill_amount,bill_amount_should,
			bill_amount_min,bill_month
		)
				select 
					distinct
--					'icbcchina_creditcard_monthbill:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					to_char(now(),'yyyy-mm-dd hh:mi:ss')::timestamp as createtime,
--					to_char(a.createtime,'yyyy-mm-dd hh:mi:ss')::timestamp as createtime,
					replace(replace(replace(a.bill_day,'年','-'),'月','-'),'日  ','') as bill_date,
					replace(replace(replace(a.repay_date,'年','-'),'月','-'),'日  ','') as pay_date,
--					reverse(substr(reverse(replace(a.card_num,' ','')),1,4)) as last_number,
					split_part(replace(a.repay,',',''),'/',1) as bill_amount,
					split_part(replace(a.repay,',',''),'/',1) as bill_amount_should,
					split_part(replace(a.repay_min,',',''),'/',1) as bill_amount_min,
					substr(replace(replace(replace(a.bill_day,'年','-'),'月','-'),'日  ',''),1,7) as bill_month
				from icbcchina_creditcard_monthbill a left join cmbchina_creditcard_userinfo b
				on a.taskid = b.taskid
				where a.taskid = this_id
				and a.repay is not null and a.repay <> '';


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

--ALTER FUNCTION "public"."pro_icbc_credit_etl"("taskid" text) OWNER TO "lvyuxin";