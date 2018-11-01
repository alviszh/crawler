CREATE OR REPLACE FUNCTION "public"."pro_cmbc_credit_etl"("taskid" text)
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
			card_number,last_number,user_name,open_date,balance,credit_limit,
			cash_limit,pay_date,bill_date
		)
				select 
					'cmbcchina_creditcard_generalinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					a.account_num as card_number,
					reverse(substr(reverse(a.account_num),1,4)) as last_number,
					a.card_owner as user_name,
					to_char(a.open_date::timestamp,'yyyy-mm-dd') as open_date,
					d.available_lmit as balance,
					d.credit_lmit as credit_limit,
					d.cash_advance_cre_lmit as cash_limit,
					split_part(e.repay_limit_date,'-',3) as pay_date,
					split_part(e.bill_date,'-',3) as bill_date
				from cmbcchina_creditcard_generalinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join cmbcchina_creditcard_myaccount d
					on a.taskid = d.taskid left join cmbcchina_creditcard_billgeneral e
					on a.taskid = e.taskid
				where a.taskid = this_id
				and  e.repay_limit_date =  (select max(repay_limit_date) from cmbcchina_creditcard_billgeneral bb 
																			where bb.taskid = this_id);

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			last_number,tran_type
		)
				select 
					'cmbcchina_creditcard_billdetail:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					case when position('+' in a.trans_amt) <> 0
								then replace(a.trans_amt,'+','') 
								else concat('-',replace(a.trans_amt,'-','')) end as amount,
					a.record_date as tran_date,
					a.trans_describe as tran_description,
					a.cardno_rear_four as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.trans_describe) as tran_type
				from cmbcchina_creditcard_billdetail a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_date,pay_date,bill_amount_min,
			bill_amount,bill_amount_should,bill_month
		)
				select 
					'cmbcchina_creditcard_billgeneral:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					a.bill_date as bill_date,
					a.repay_limit_date as pay_date,
					a.min_repay_limit as bill_amount_min,
					a.current_need_pay as bill_amount,
					a.current_need_pay as bill_amount_should,
--					reverse(substr(reverse(a.card_num),1,4)) as last_number,
					substr(a.bill_date,1,7) as bill_month
--					a.credit_avail_1 as available_limit
				from cmbcchina_creditcard_billgeneral a 
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

--ALTER FUNCTION "public"."pro_cmbc_credit_etl"("taskid" text) OWNER TO "lvyuxin";