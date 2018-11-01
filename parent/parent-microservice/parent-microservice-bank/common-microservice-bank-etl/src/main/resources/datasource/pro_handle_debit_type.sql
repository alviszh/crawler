CREATE OR REPLACE FUNCTION "public"."pro_handle_debit_type"("in_str" text)
  RETURNS "pg_catalog"."text" AS $BODY$BEGIN
		
  DECLARE
		out_str text DEFAULT '';

	BEGIN
		
		IF position('转账' in in_str) <> 0 THEN 

			out_str = '转账';

		ELSEIF position('消费' in in_str) <> 0 THEN
	
			out_str = '支出';

		ELSEIF position('续存' in in_str) <> 0 THEN

			out_str = '存款';

		ELSEIF position('结息' in in_str) <> 0 THEN

			out_str = '收入';

		ELSEIF position('充值' in in_str) <> 0 THEN

			out_str = '支出';

		ELSEIF position('支付' in in_str) <> 0 THEN

			out_str = '支出';

		ELSEIF position('汇款' in in_str) <> 0 THEN

			out_str = '转账';

		ELSEIF position('房款' in in_str) <> 0 THEN

			out_str = '还款';

		ELSEIF position('工资' in in_str) <> 0 THEN

			out_str = '收入';

		ELSEIF position('工资' in in_str) <> 0 THEN

			out_str = '收入';

		ELSEIF position('理财' in in_str) <> 0 THEN

			out_str = '理财';

		ELSEIF position('还款' in in_str) <> 0 THEN

			out_str = '还款';

		ELSEIF position('提现' in in_str) <> 0 THEN

			out_str = '转账';

		ELSEIF position('存款' in in_str) <> 0 THEN

			out_str = '存款';

		ELSEIF position('退款' in in_str) <> 0 THEN

			out_str = '收入';

		ELSEIF position('退款' in in_str) <> 0 THEN

			out_str = '收入';

		ELSEIF position('代付' in in_str) <> 0 THEN

			out_str = '收入';

		ELSEIF position('贷' in in_str) <> 0 THEN

			out_str = '贷款';

		ELSEIF position('代收' in in_str) <> 0 THEN

			out_str = '支出';

		ELSEIF position('代扣' in in_str) <> 0 THEN

			out_str = '支出';

		ELSEIF position('收入' in in_str) <> 0 THEN

			out_str = '收入';

		ELSEIF position('转出' in in_str) <> 0 THEN

			out_str = '转账';

		ELSEIF position('取款' in in_str) <> 0 THEN

			out_str = '取现';

		ELSEIF position('汇入' in in_str) <> 0 THEN

			out_str = '转账';

		ELSEIF position('季息' in in_str) <> 0 THEN

			out_str = '收入';

		ELSEIF position('转存' in in_str) <> 0 THEN

			out_str = '理财';

		ELSE 

			out_str = '其他';

		END IF;

			RETURN out_str;

	END;
END;   
$BODY$
  LANGUAGE 'plpgsql' VOLATILE COST 100
;

--ALTER FUNCTION "public"."pro_handle_debit_type"("in_str" text) OWNER TO "lvyuxin";