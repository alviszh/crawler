CREATE OR REPLACE FUNCTION "public"."pro_ecommerce_taobao_etl"("mission_id" text)
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
			resource,task_id,createtime,area,address,telnum,
			consignee,is_default,post_code
		)
			select 
				'e_commerce_taobao_deliver_address:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				replace(a.area,' ','') as area,
				a.address as address,
				a.phone_num as telnum,
				a.receiver as consignee,
				case when is_default = '1' 
							then '是' else '否' end  as is_default,
				a.postcode as post_code
			from e_commerce_taobao_deliver_address a
			where a.taskid = this_id;

--订单信息

		INSERT INTO pro_ecommerce_bill_info
		(
			resource,task_id,createtime,goods_name,amount,trade_time,
			trade_status,seller_name,consignee,telnum,address
		)
			select 
				'e_commerce_tb_order_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.goods_name as goods_name,
				a.goods_price as amount,
				a.order_date as trade_time,
				a.order_status as trade_status,
				a.shop_name as seller_name,
				split_part(a.receiver,',',1) as consignee,
				split_part(a.receiver,',',2) as telnum,
				replace(split_part(a.receiver,',',3),' ','') as address
			from e_commerce_tb_order_info a
			where a.taskid = this_id;


--用户信息

		INSERT INTO pro_ecommerce_user_info
		(
			resource,task_id,createtime,birthday,address,gender,nick_name,
			real_name
		)
			select 
				'e_commerce_taobao_user_info:'||a.id::text as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				to_char(a.birthday::timestamp,'yyyy-mm-dd') as birthday,
				concat(a.domicile_province,a.domicile_city,a.domicile_area) as address,
				a.gerden as gender,
				a.nick_name as nick_name,
				a.realname as real_name
			from e_commerce_taobao_user_info a
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
--ALTER FUNCTION "public"."pro_ecommerce_taobao_etl"("mission_id" text) OWNER TO "lvyuxin";