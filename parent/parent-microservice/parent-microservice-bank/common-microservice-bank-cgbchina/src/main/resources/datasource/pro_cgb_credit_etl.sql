CREATE OR REPLACE FUNCTION "public"."pro_cgb_credit_etl"("taskid" text)
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
			address,email,user_name,phonenum,credit_limit,bill_date,pay_date,
			card_number,last_number
		)
				select 
					'cgb_credit_china_userinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					c.idnum as basic_user_idnum,
					c.name as basic_user_name,
					case when a.attr = 'null' then '' else a.attr end as address,
					case when a.email = 'null' then '' else a.email end as email,
					a.name as user_name,
					a.phone2 as phonenum,
					replace(d.creditlimit,',','') as credit_limit,
					split_part(split_part(d.period,' - ',2),'/',3) as bill_date,
					split_part(d.paymentdate,'/',3) as pay_date,
					d.cardnumber as card_number,
					reverse(substr(reverse(replace(d.cardnumber,' ','')),1,4)) as last_number
				from cgb_credit_china_userinfo a left join task_bank b
					on a.taskid = b.taskid left join basic_user_bank c
					on b.basic_user_bank_id = c.id left join cgb_credit_china_transinfo d
					on a.taskid = d.taskid
				where a.taskid = this_id
				and d.paymentdate = (select max(paymentdate) from cgb_credit_china_transinfo bb
																where bb.taskid = this_id);

--信用卡交易流水

		INSERT INTO pro_bank_credit_tran_detail
		(
			resource,task_id,createtime,amount,tran_date,tran_description,
			last_number,tran_type
		)
				select 
					'cgb_credit_china_transflow:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					case when pro_handle_credit_type(a.note_code) = '还款' 
								then concat('-',a.transamt)
								else a.transamt end as amount,
					to_char(a.transfer_date::timestamp,'yyyy-mm-dd') as tran_date,
					a.note_code as tran_description,
					a.last4of_card_no as last_number,
--					reverse(substr(reverse(a.card_no),1,4)) as last_number,
					pro_handle_credit_type(a.note_code) as tran_type
				from cgb_credit_china_transflow a 
				where a.taskid = this_id;

--信用卡月账单信息

		INSERT INTO pro_bank_credit_bill_info
		(
			resource,task_id,createtime,bill_amount_min,bill_amount,bill_amount_should,
			pay_date,last_number,bill_date,bill_month
		)
				select 
					'cgb_credit_china_transinfo:'|| a.id::TEXT as resource,
					a.taskid as task_id,
					a.createtime as createtime,
					replace(a.minpayment,',','') as bill_amount_min,
					replace(a.newbalance,',','') as bill_amount,
					replace(a.newbalance,',','') as bill_amount_should,
					replace(a.paymentdate,'/','-') as pay_date,
					reverse(substr(reverse(a.cardnumber),1,4)) as last_number,
					replace(split_part(a.period,' - ',2),'/','-') as bill_date,
					substr(replace(split_part(a.period,' - ',2),'/','-'),1,7) as bill_month
				from cgb_credit_china_transinfo a 
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

--ALTER FUNCTION "public"."pro_cgb_credit_etl"("taskid" text) OWNER TO "lvyuxin";