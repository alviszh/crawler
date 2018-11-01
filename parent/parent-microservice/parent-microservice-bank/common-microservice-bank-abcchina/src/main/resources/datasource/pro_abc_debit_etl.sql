CREATE OR REPLACE FUNCTION "public"."pro_abc_debit_etl"("taskid" text)
  RETURNS "pg_catalog"."text" AS $BODY$BEGIN 
  DECLARE
		this_id text;
		p_etl_status text DEFAULT 'success';
		p_etl_error_detail text;
		p_etl_error_content text;		
	
	BEGIN

		this_id = taskid;

		delete from pro_bank_debit_user_info where task_id = this_id;
		delete from pro_bank_debit_tran_detail where task_id = this_id;
		delete from pro_bank_debit_deposit_info where task_id = this_id;


--用户基本信息

		INSERT INTO pro_bank_debit_user_info
		(
			resource,task_id,createtime,basic_id_num,basic_user_name,birthday,
			tel_num,industry,zip_code,email,nation,card_num,user_name,open_bank,
			account_status,open_date,balance
		)
			select 
				'abcchina_userinfo:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.idnum as basic_id_num,
				c.name as basic_user_name,
				a.csrq as birthday,
				case when a.dwdh = '86---' then '' else a.dwdh end as tel_num,
				a.dwmc as industry,
				a.dwyb as zip_code,
				a.email as email,
				a.gj as nation,
				a.khh as card_num,
				a.khxm as user_name, 
				a.wyzch as open_bank,
				--a.zjlx as cert_type,
				d.accountstatusmeaning as account_status,
				to_char(d.opendate::timestamp,'yyyy-mm-dd') as open_date,
				d.yue as balance
			from abcchina_userinfo a left join task_bank b
				on a.taskid = b.taskid left join basic_user_bank c
				on b.basic_user_bank_id = c.id left join abcchina_accounttype d
				on a.taskid = d.taskid
			where a.taskid = this_id;

--交易流水信息

		INSERT INTO pro_bank_debit_tran_detail
		(
			resource,task_id,createtime,account_num,tran_date,tran_description,
			tran_type,money,balance
		)
			select
				'abcchina_transflow:'|| a.id::TEXT as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.account as account_num,
				a.deal_time as tran_date,
				a.deal_digest as tran_description,
			--	a.deal_type,
				b.item_name as tran_type,
				replace(a.deal_money,',','') as money,
				replace(a.this_yue,',','') as balance
			from abcchina_transflow a left join debit_tran_type_item_code b
				on a.deal_type = b.source_name
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

--ALTER FUNCTION "public"."pro_abc_debit_etl"("taskid" text) OWNER TO "lvyuxin";