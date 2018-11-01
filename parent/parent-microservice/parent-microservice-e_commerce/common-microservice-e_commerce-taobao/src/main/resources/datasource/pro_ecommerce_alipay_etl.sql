CREATE OR REPLACE FUNCTION "public"."pro_ecommerce_alipay_etl"("mission_id" text)
  RETURNS "pg_catalog"."text" AS $BODY$BEGIN  

	DECLARE
		this_id text;
		p_etl_status text DEFAULT 'success';
		p_etl_error_detail text;
		p_etl_error_content text;

	BEGIN

		this_id = mission_id;


--清除此taskid的数据

		delete from pro_ecommerce_address_info where task_id = this_id;
		delete from pro_ecommerce_bill_info where task_id = this_id;
		delete from pro_ecommerce_credit_info where task_id = this_id;
		delete from pro_ecommerce_card_info where task_id = this_id;
		delete from pro_ecommerce_finance_info where task_id = this_id;
		delete from pro_ecommerce_tran_detail where task_id = this_id;
		delete from pro_ecommerce_user_info where task_id = this_id;



-------------------------开始插入数据--------------------------------

--银行卡

		INSERT INTO pro_ecommerce_card_info
		(
			resource,task_id,createtime,bank_name,bank_type,
			last_num,card_holder,phonenum
		)
			select 
				'e_commerce_taobao_alipay_bankcard_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.bank_name as bank_name,
				a.card_type as bank_type,
				a.card_no as last_num,
				a.cardholder as card_holder,
				a.reserved_phone as phonenum
			from e_commerce_taobao_alipay_bankcard_info a
			where a.taskid = this_id;


--交易明细

		INSERT INTO pro_ecommerce_tran_detail
		(
			resource,task_id,createtime,amount,trade_status,seller_name,
			trade_time,trade_comment
		)
			select 
				'e_commerce_taobao_alipay_payment_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.amount as amount,
				a.status as trade_status,
				a.trader as seller_name,
				case when a.transaction_date = '今天' 
							then concat(CURRENT_DATE,' ',a.transaction_time,':00')
						 when a.transaction_date = '昨天'
							then concat(CURRENT_DATE - 1,' ',a.transaction_time,':00')
						 else concat(a.transaction_date,' ',a.transaction_time,':00') 
				end as trade_time,
				a.transaction_name as trade_comment  				
			from e_commerce_taobao_alipay_payment_info a
			where a.taskid = this_id;	

--用户信息

		INSERT INTO pro_ecommerce_user_info
		(
			resource,task_id,createtime,user_name,account_balance,real_name,
			idnum
		)
			select 
				'e_commerce_taobao_user_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.account as user_name,
				replace(a.account_balance,'元','') as account_balance,
				split_part(a.real_name,' ',1) as real_name,
				split_part(a.real_name,' ',3) as idnum
 			from e_commerce_taobao_alipay_info a
			where a.taskid = this_id;

--信用信息

		INSERT INTO pro_ecommerce_credit_info
		(
			resource,task_id,createtime,credit_limit,avialable_limit,credit_name
		)
			select 
				'e_commerce_taobao_alipay_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				replace(a.huabei_total_credit,'元','') as credit_limit,
				replace(a.huabei_available_credit,'元','') as avialable_limit,
				'蚂蚁花呗' as credit_name
 			from e_commerce_taobao_alipay_info a
			where a.taskid = this_id;

--理财信息

		INSERT INTO pro_ecommerce_finance_info
		(
			resource,task_id,createtime,avialable_balance,accumulated_income,finance_name
		)
			select 
				'e_commerce_taobao_alipay_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				replace(a.yu_e_bao,'元','') as avialable_balance,
				a.yu_e_bao_accumulated_earnings as accumulated_income,
				'余额宝' as finance_name
 			from e_commerce_taobao_alipay_info a
			where a.taskid = this_id;


--更新此任务状态

	--		UPDATE task_mobile a set a.etltime = now(),a.etl_status = p_etl_status where a.taskid = this_id;

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
--ALTER FUNCTION "public"."pro_ecommerce_alipay_etl"("mission_id" text) OWNER TO "lvyuxin";