CREATE OR REPLACE FUNCTION "public"."pro_ecommerce_jd_etl"("mission_id" text)
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
			resource,task_id,createtime,address,area,telnum,email,
			consignee
		)

			select 
				'e_commerce_jd_receiveraddress:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.address as address,
				a.area as area,
				a.cellphone as telnum,
				a.email as email,
				a.name as consignee
			from e_commerce_jd_receiveraddress a
			where a.taskid = this_id;

--银行卡信息

		INSERT INTO pro_ecommerce_card_info
		(
			resource,task_id,createtime,bank_name,bank_type,
			card_holder,phonenum,last_num
		)
			select 
				'e_commerce_jd_querybindcard:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.bank as bank_name,
				a.banktye as bank_type,
				a.name as card_holder,
				a.phone as phonenum,
				replace(a.tailnum,'尾号','') as last_num
			from e_commerce_jd_querybindcard a
			where a.taskid = this_id;

--订单信息

		INSERT INTO pro_ecommerce_bill_info
		(
			resource,task_id,createtime,address,trade_time,goods_name,
			amount,consignee,seller_name,telnum,trade_status
		)
			select 
				'e_commerce_jd_indent:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.indent_address as address,
				a.indent_dealtime as trade_time,
				a.indent_designation as goods_name,
				replace(split_part(a.indent_money,' ',1),'¥','') as amount,
				a.indent_name as consignee,
				a.indent_operate as seller_name,
				a.indent_phone as telnum,
				a.indent_status as trade_status
			from e_commerce_jd_indent a
			where a.taskid = this_id;

--用户信息

		INSERT INTO pro_ecommerce_user_info
		(
			task_id,birthday,nick_name,user_name,gender
		)
			select 
--				'e_commerce_jd_user:'||a.id::text as resource,
			 DISTINCT
				a.taskid as task_id,
--				a.createtime as createtime,
				to_char(a.birthday::timestamp,'yyyy-mm-dd') as birthday,
				a.nickname as nick_name,
				a.username as user_name,
				case when a.sex = '0' then '男'
							else '女' end  as gender
			from e_commerce_jd_user a
			where a.taskid = this_id;

--信用信息

		INSERT INTO pro_ecommerce_credit_info
		(
			task_id,credit_limit,avialable_limit,credit_name
		)
			select 
				DISTINCT
--				'e_commerce_jd_baitiao:'||a.id::text as resource,
				a.taskid as task_id,
--				a.createtime as createtime,
				a.credit_limit as credit_limit,
				a.available_limit as avialable_limit,
				'京东白条' as credit_name
 			from e_commerce_jd_baitiao a
			where a.taskid = this_id;

--理财信息

		INSERT INTO pro_ecommerce_finance_info
		(
			task_id,avialable_balance,accumulated_income,finance_name
		)
			select 
--				'e_commerce_jd_coffers:'||a.id::text as resource,
			DISTINCT
				a.taskid as task_id,
--				a.createtime as createtime,
				a.available as avialable_balance,
				a.all_income as accumulated_income,
				'京东金库' as finance_name
 			from e_commerce_jd_coffers a
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
--ALTER FUNCTION "public"."pro_ecommerce_jd_etl"("mission_id" text) OWNER TO "lvyuxin";