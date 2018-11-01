CREATE OR REPLACE FUNCTION "public"."pro_ecommerce_suning_etl"("mission_id" text)
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

--收货地址

		INSERT INTO pro_ecommerce_address_info
		(
			resource,task_id,createtime,address,telnum,
			consignee,is_default
		)
			select 
				'e_commerce_suning_address_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.address as address,
				a.tel_num as telnum,
				a.name as consignee,
				case when position('[默认]' in a.address) <> 0 
					then '是' else '否' end as is_default
			from e_commerce_suning_address_info a
			where a.taskid = this_id;

--银行卡

		INSERT INTO pro_ecommerce_card_info
		(
			resource,task_id,createtime,bank_name,bank_type,
			last_num,effect_time
		)
			select 
				'e_commerce_suning_bank_card:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				split_part(a.card_type,' ',2) as  bank_name,
				split_part(a.card_type,' ',1) as bank_type,
				replace(a.last_num,'尾号','') as last_num,
				a.bind_time as effect_time
			from e_commerce_suning_bank_card a
			where a.taskid = this_id;

--订单信息

		INSERT INTO pro_ecommerce_bill_info
		(
			resource,task_id,createtime,address,trade_time,goods_name,
			amount,consignee,seller_name,telnum,trade_status
		)
			select 
				'e_commerce_suning_order_detail:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.receiver_addr as address,
				a.submit_time as trade_time,
				a.product_name as goods_name,
				a.price as amount,
				a.receiver_name as consignee,
				a.vendor_name as seller_name,
				a.receiver_tel as telnum,
				a.trans_status as trade_status
			from e_commerce_suning_order_detail a
			where a.taskid = this_id;

--用户信息

		INSERT INTO pro_ecommerce_user_info
		(
			resource,task_id,createtime,address,birthday,gender,
			real_name,nick_name,user_name
		)
			select 
				'e_commerce_suning_user_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.address as address,
				case when a.birthday = '请选择' then ''
							else birthday end as birthday,
				a.gender as gender,
				a.name as real_name,
				a.nickname as nick_name,
				a.username as user_name
			from e_commerce_suning_user_info a
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
--ALTER FUNCTION "public"."pro_ecommerce_suning_etl"("mission_id" text) OWNER TO "lvyuxin";