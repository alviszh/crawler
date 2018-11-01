CREATE OR REPLACE FUNCTION "public"."pro_handle_credit_type"("in_str" text)
  RETURNS "pg_catalog"."text" AS $BODY$BEGIN  

--通过判断关键字，来识别交易类型。持续更新
		
  DECLARE
		out_str text DEFAULT '';

	BEGIN
	
		IF position('转入' in in_str) <> 0 THEN
	
			out_str = '还款';
			
		ELSEIF position('转账' in in_str) <> 0 THEN
	
			out_str = '还款';	

		ELSEIF position('转帐' in in_str) <> 0 THEN
	
			out_str = '还款';	
			
		ELSEIF position('汇款' in in_str) <> 0 THEN
	
			out_str = '还款';

		ELSEIF position('还款' in in_str) <> 0 THEN
	
			out_str = '还款';

		ELSEIF position('抵扣' in in_str) <> 0 THEN
	
			out_str = '还款';

		ELSEIF position('退货' in in_str) <> 0 THEN
	
			out_str = '还款';				

		ELSEIF position('退单' in in_str) <> 0 THEN
	
			out_str = '还款';		

		ELSEIF position('款项' in in_str) <> 0 THEN
	
			out_str = '还款';

		ELSEIF position('退款' in in_str) <> 0 THEN
	
			out_str = '还款';

		ELSEIF position('手续费' in in_str) <> 0 THEN
	
			out_str = '手续费';

		ELSEIF position('现金' in in_str) <> 0 THEN
	
			out_str = '提现';

		ELSEIF position('转出' in in_str) <> 0 THEN
	
			out_str = '提现';

		ELSEIF position('取' in in_str) <> 0 THEN
	
			out_str = '提现';

		ELSEIF position('提现' in in_str) <> 0 THEN
	
			out_str = '提现';

		ELSEIF position('分期手续费' in in_str) <> 0 THEN
	
			out_str = '分期手续费';
			
		ELSEIF position('年费' in in_str) <> 0 THEN
	
			out_str = '年费';
		
		ELSEIF position('分期' in in_str) <> 0 THEN
	
			out_str = '分期';

		ELSEIF position('利息' in in_str) <> 0 THEN
	
			out_str = '利息';

		ELSEIF position('违约金' in in_str) <> 0 THEN
	
			out_str = '违约金';

		ELSEIF position('消费' in in_str) <> 0 THEN 

			out_str = '消费';

		ELSEIF position('支付宝' in in_str) <> 0 THEN
	
			out_str = '消费';

		ELSEIF position('支付' in in_str) <> 0 THEN
	
			out_str = '消费';

		ELSEIF position('财付通' in in_str) <> 0 THEN
	
			out_str = '消费';

		ELSEIF position('付费' in in_str) <> 0 THEN
	
			out_str = '消费';

		ELSEIF position('商城' in in_str) <> 0 THEN
	
			out_str = '消费';

		ELSEIF position('缴费' in in_str) <> 0 THEN
	
			out_str = '消费';

		ELSE 

			out_str = '消费';

		END IF;

		RETURN out_str;

	END;
END;   


$BODY$
  LANGUAGE 'plpgsql' VOLATILE COST 100
;

--ALTER FUNCTION "public"."pro_handle_credit_type"("in_str" text) OWNER TO "lvyuxin";