CREATE OR REPLACE FUNCTION "public"."pro_cmb_credit_etl"("taskid" text)
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
			card_number,last_number,pay_date,bill_date,user_name,credit_limit,
			credit_limit_usd,balance
		)
				select 
					'cmbchina_creditcard_userinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					a.id_num as card_number,
					reverse(substr(reverse(a.id_num),1,4)) as last_number,
					split_part(a.lbqdqhk,'-',3) as pay_date,
					replace(a.lmyzd,'日','') as bill_date,
					a.name as user_name,
					replace(replace(a.trxyedrmb,' ￥',''),',','') as credit_limit,
					replace(replace(a.trxyed_dollar,' ＄',''),',','') as credit_limit_usd,
					replace(replace(a.tryjxjedrmb,' ￥',''),',','') as balance
				from cmbchina_creditcard_userinfo a left join task_bank b
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
					'cmbchina_creditcard_billdetails:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					replace(replace(a.amount_transaction,' ',''),',','') as amount,
					case when concat(extract(year from now())::text,a.billing_day)::timestamp <= a.createtime::timestamp
								then to_char(concat(extract(year from now())::text,a.billing_day)::timestamp,'yyyy-mm-dd') 
								else to_char(concat(extract(year from now()-INTERVAL '1 year')::text,a.billing_day)::timestamp,'yyyy-mm-dd') 
								end as tran_date,
					a.summary as tran_description,
					a.endfour_num as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.summary) as tran_type
				from cmbchina_creditcard_billdetails a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_month,bill_amount_min,bill_amount,
			bill_amount_should,bill_date,pay_date
		)
				select 
					DISTINCT
					'cmbchina_creditcard_billgeneral:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					to_char(a.createtime,'yyyy-mm-dd hh:mi:ss')::timestamp as createtime,
					a.bill_month as bill_month,
					replace(a.repayment_minrmb,',','') as bill_amount_min,
					replace(a.repayment_sumrmb,',','') as bill_amount,
					replace(a.repayment_sumrmb,',','') as bill_amount_should,
					concat(a.bill_month,'-',replace(b.lmyzd,'日','')) as bill_date,
					to_char(concat(a.bill_month,'-',replace(b.lmyzd,'日',''))::timestamp + INTERVAL '18 days','yyyy-mm-dd') as pay_date
				from cmbchina_creditcard_billgeneral a left join cmbchina_creditcard_userinfo b
				on a.taskid = b.taskid
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

;;--ALTER FUNCTION "public"."pro_cmb_credit_etl"("taskid" text) OWNER TO "lvyuxin";