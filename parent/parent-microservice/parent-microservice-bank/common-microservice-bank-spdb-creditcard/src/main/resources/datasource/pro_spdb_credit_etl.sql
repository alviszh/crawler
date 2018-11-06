CREATE OR REPLACE FUNCTION "public"."pro_spdb_credit_etl"("taskid" text)
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
			card_number,last_number,credit_limit,cash_limit,user_name,bill_date,
			pay_date
		)
				select 
					'spdb_creditcard_newgeneralinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					a.card_nbr as card_number,
					reverse(substr(reverse(a.card_nbr),1,4)) as last_number,
					replace(replace(a.credit_limit,'￥',''),',','') as credit_limit,
					replace(replace(a.essay_limit,'￥',''),',','') as cash_limit,
					split_part(a.name,' ',1) as user_name,
					reverse(substr(reverse(d.account_day),1,2)) as bill_date,
					reverse(substr(reverse(d.last_back_date),1,2)) as pay_date		
				from spdb_creditcard_newgeneralinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join spdb_creditcard_newbillgeneral d
					on a.taskid = d.taskid
				where a.taskid = this_id
				and d.account_day = (select max(account_day) from spdb_creditcard_newbillgeneral bb
																			where bb.taskid = this_id);	

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			last_number,tran_type
		)
				select 
					'spdb_creditcard_newbilldetail:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					replace(replace(a.trans_sum,'¥',''),',','') as amount,
					to_char(a.trade_date::timestamp,'yyyy-mm-dd') as tran_date,
					a.tran_summary as tran_description,
					a.card_num as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.tran_summary) as tran_type
				from spdb_creditcard_newbilldetail a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_date,bill_month,pay_date,bill_amount_min,
			bill_amount_should,bill_amount
		)
				select 
					'spdb_creditcard_newbillgeneral:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					to_char(a.account_day::timestamp,'yyyy-mm-dd') as bill_date,
					substr(a.bill_month,1,4) ||'-'|| substr(a.bill_month,5,2) as bill_month,
					to_char(a.last_back_date::timestamp,'yyyy-mm-dd') as pay_date,
					replace(replace(a.min_pay,',',''),'¥','') as bill_amount_min,
	--				replace(replace(a.back_money,',',''),'¥','') as bill_amount,
					replace(replace(a.new_charges,',',''),'¥','') as bill_amount_should,
					replace(replace(a.new_charges,',',''),'¥','') as bill_amount
				from spdb_creditcard_newbillgeneral a 
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
;;
--ALTER FUNCTION "public"."pro_spdb_credit_etl"("taskid" text) OWNER TO "lvyuxin";