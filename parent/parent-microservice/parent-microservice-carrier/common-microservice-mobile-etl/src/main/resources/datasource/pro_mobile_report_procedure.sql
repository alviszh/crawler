CREATE OR REPLACE FUNCTION "public"."pro_mobile_report"("taskid" text)
  RETURNS "pg_catalog"."text" AS $BODY$DECLARE 
	this_id text;

--记录每个步骤的状态和错误详情
-------------------------------------------
	d_step_result text default 'success';
	d_error_detail text default '';
	d_step_result_2 text default 'success';
	d_error_detail_2 text default '';
	d_step_result_3 text default 'success';
	d_error_detail_3 text default '';
	d_step_result_4 text default 'success';
	d_error_detail_4 text default '';
	d_step_result_5 text default 'success';
	d_error_detail_5 text default '';
	d_step_result_6 text default 'success';
	d_error_detail_6 text default '';
	d_step_result_7 text default 'success';
	d_error_detail_7 text default '';
	d_step_result_8 text default 'success';
	d_error_detail_8 text default '';
	d_step_result_9 text default 'success';
	d_error_detail_9 text default '';
	d_step_result_10 text default 'success';
	d_error_detail_10 text default '';
	d_step_result_11 text default 'success';
	d_error_detail_11 text default '';
	d_step_result_12 text default 'success';
	d_error_detail_12 text default '';
	d_step_result_13 text default 'success';
	d_error_detail_13 text default '';
-------------------------------------------
	d_report_result text default 'finished';

BEGIN

	this_id = taskid;

--清空此taskid历史数据

	delete from pro_mobile_report_baseinfo where task_id = this_id;
	delete from pro_mobile_report_call_detail_statistics where task_id = this_id;
	delete from pro_mobile_report_call_sum_statistics where task_id = this_id;
	delete from pro_mobile_report_calltime_detail_statistics where task_id = this_id;
	delete from pro_mobile_report_carrier_consume where task_id = this_id;
	delete from pro_mobile_report_consumeinfo where task_id = this_id;
	delete from pro_mobile_report_contacts_top where task_id = this_id;
	delete from pro_mobile_report_location_top where task_id = this_id;
	delete from pro_mobile_report_period_statistics where task_id = this_id;
	delete from pro_mobile_report_social_analysis_summary where task_id = this_id;
	delete from pro_mobile_report_stability where task_id = this_id;
	delete from pro_mobile_report_vitality where task_id = this_id;
	delete from pro_mobile_report_vitality_analysis_summary where task_id = this_id;

--清空日志表

	delete from pro_mobile_report_detail_log where task_id = this_id;


  
--运营商报告用户基本信息（pro_mobile_report_baseinfo）

		BEGIN

			INSERT INTO pro_mobile_report_baseinfo
			(
				task_id,createtime,user_name,id_num,basic_user_name,basic_id_num,address,
				contact_num,phone_num,cur_balance,package_name,email,net_in_date,net_in_duration,
				points,postcode,birthday,province,city,education,hobby,occupation,gender,carrier,
				qq,fax,weibo,nick_name,cus_level,cus_status,certificate_type,cus_type,source_name,
				data_type,report_time,idnum_status,email_status,address_status,idnum_carrier,
				name_carrier,callrecord_status,real_name
			)
				select 
					a.task_id,
					a.createtime::timestamp,
					COALESCE(a.user_name,'运营商未提供数据') as user_name,
					COALESCE(a.id_num,'运营商未提供数据') as id_num,
					a.basic_user_name as basic_user_name,
					a.basic_id_num as basic_id_num,
					COALESCE(a.address,'运营商未提供数据') as address,
					COALESCE(a.contact_num,'运营商未提供数据') as contact_num,
					COALESCE(a.phone_num,'运营商未提供数据') as phone_num,
					COALESCE(a.cur_balance,'运营商未提供数据') as cur_balance,
					COALESCE(a.package_name,'运营商未提供数据') as package_name,
					COALESCE(a.email,'运营商未提供数据') as email,
					COALESCE(a.net_in_date,'运营商未提供数据') as net_in_date,
					COALESCE(a.net_in_duration,'运营商未提供数据') as net_in_duration,
					COALESCE(a.points,'运营商未提供数据') as points,
					COALESCE(a.postcode,'运营商未提供数据') as postcode,
					COALESCE(a.birthday,'运营商未提供数据') as birthday,
					COALESCE(a.province,'运营商未提供数据') as province,
					COALESCE(a.city,'运营商未提供数据') as city,
					COALESCE(a.education,'运营商未提供数据') as education,
					COALESCE(a.hobby,'运营商未提供数据') as hobby,
					COALESCE(a.occupation,'运营商未提供数据') as occupation,
					COALESCE(a.gender,'运营商未提供数据') as gender,
					COALESCE(a.carrier,'运营商未提供数据') as carrier,
					COALESCE(a.qq,'运营商未提供数据') as qq,
					COALESCE(a.fax,'运营商未提供数据') as fax,
					COALESCE(a.weibo,'运营商未提供数据') as weibo,
					COALESCE(a.nick_name,'运营商未提供数据') as nick_name,
					COALESCE(a.cus_level,'运营商未提供数据') as cus_level,
					COALESCE(a.cus_status,'运营商未提供数据') as cus_status,
					COALESCE(a.certificate_type,'运营商未提供数据') as certificate_type,
					COALESCE(a.cus_type,'运营商未提供数据') as cus_type,
					'运营商' as source_name,
					'运营商分析报告' as data_type,
					CURRENT_DATE as report_time,
					'有效' as idnum_status,
					case when a.email is not null or a.email <> '' 
								then '有效' else '运营商未提供数据' end as email_status,
					case when a.address is not null or a.address <> '' 
								then '有效' else '运营商未提供数据' end as address_status,
					case when a.id_num = a.basic_id_num
								then '匹配' else '未知' end as idnum_carrier,
					case when a.user_name = a.basic_user_name
								then '匹配' else '未知' end as name_carrier,
					case when b.call_record_status = '200' 
								then '正常' else '未知' end as callrecord_status,
					COALESCE(a.real_name,'运营商未提供数据') as real_name
				from 
					pro_mobile_user_info a left join task_mobile b
				on a.task_id = b.taskid
				where a.task_id = this_id;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'运营商报告用户基本信息','pro_mobile_report_baseinfo',d_error_detail,d_step_result);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'运营商报告用户基本信息','pro_mobile_report_baseinfo',d_error_detail,d_step_result);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'运营商报告用户基本信息','pro_mobile_report_baseinfo',d_error_detail,d_step_result);

		END;

-----------------------------------------------------------------------------------------------
		
--运营商报告社交分析摘要（pro_mobile_report_social_analysis_summary）

	DECLARE

--三月指标
		p_social_range_three text;
		p_social_intimacy_three text;
		p_social_center_three text;
		p_interflow_count_three text;

--六月指标
		p_social_range_six text;
		p_social_intimacy_six text;
		p_social_center_six text;
		p_interflow_count_six text;

		BEGIN

---------------------------------/*以三月统计*/----------------------------------------------

--朋友圈大小

			select
				count(distinct his_num)::text INTO p_social_range_three 
			from pro_mobile_call_info a
			where task_id = this_id
			and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd');

--朋友圈亲密度

			select 
				count(1)::text INTO p_social_intimacy_three 
			from
			( 
				select
					count(1),
					his_num
				from pro_mobile_call_info a
				where task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by his_num having count(1) >= 10) aa;


---朋友圈中心地

			select
				bb.call_location INTO p_social_center_three
			from 
			(
				select 
					aa.task_id,
					aa.times,
					aa.call_location,
					rank() over(order by aa.times desc) as rn
				from
				( 
					select
						a.task_id,
						count(1) as times,
						call_location
					from pro_mobile_call_info a
					where task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by call_location,task_id ) aa 
			) bb 
			where bb.rn = 1;


---互通电话的号码数目

			select
				count(distinct aa.contact_number) INTO  p_interflow_count_three
			from
			(
				select 
					case when a.call_type = '主叫' 
								then a.phone_num
								else a.his_num end as contact_number
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				INTERSECT
				select 
					case when a.call_type = '主叫' 
								then a.his_num
								else a.phone_num end as contact_number
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
			) aa;

--向表中插入数据（pro_mobile_report_social_analysis_summary）

		INSERT INTO pro_mobile_report_social_analysis_summary
		(
			task_id,social_range,social_intimacy,social_center,is_local,interflow_count,data_type
		)
			select 
				a.task_id,
				p_social_range_three,
				p_social_intimacy_three,
				p_social_center_three,
				case when p_social_center_three::text = a.city::text 
							then '是' else '否' end as is_local,
				p_interflow_count_three,
				'三月' as data_type
			from pro_mobile_user_info a
			where a.task_id = this_id;


---------------------------------/*以六月统计*/----------------------------------------------

--朋友圈大小

			select
				count(distinct his_num)::text INTO p_social_range_six 
			from pro_mobile_call_info a
			where task_id = this_id;
--			and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd');

--朋友圈亲密度

			select 
				count(1)::text INTO p_social_intimacy_six 
			from
			( 
				select
					count(1),
					his_num
				from pro_mobile_call_info a
				where task_id = this_id
--				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by his_num having count(1) >= 10) aa;


---朋友圈中心地

			select
				bb.call_location INTO p_social_center_six
			from 
			(
				select 
					aa.task_id,
					aa.times,
					aa.call_location,
					rank() over(order by aa.times desc) as rn
				from
				( 
					select
						a.task_id,
						count(1) as times,
						call_location
					from pro_mobile_call_info a
					where task_id = this_id
--					and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by call_location,task_id ) aa 
			) bb 
			where bb.rn = 1;


---互通电话的号码数目

			select
				count(distinct aa.contact_number) INTO p_interflow_count_six
			from
			(
				select 
					case when a.call_type = '主叫' 
								then a.phone_num
								else a.his_num end as contact_number
				from pro_mobile_call_info a
				where a.task_id = this_id
--				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				INTERSECT
				select 
					case when a.call_type = '主叫' 
								then a.his_num
								else a.phone_num end as contact_number
				from pro_mobile_call_info a
				where a.task_id = this_id
--				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
			) aa;

--向表中插入数据（pro_mobile_report_social_analysis_summary）

		INSERT INTO pro_mobile_report_social_analysis_summary
		(
			task_id,social_range,social_intimacy,social_center,is_local,interflow_count,data_type
		)
			select 
				a.task_id,
				p_social_range_six,
				p_social_intimacy_six,
				p_social_center_six,
				case when p_social_center_six::text = a.city::text 
							then '是' else '否' end as is_local,
				p_interflow_count_three,
				'六月' as data_type
			from pro_mobile_user_info a
			where a.task_id = this_id;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'运营商报告社交分析摘要','pro_mobile_report_social_analysis_summary',d_error_detail_2,d_step_result_2);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_2 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_2 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'运营商报告社交分析摘要','pro_mobile_report_social_analysis_summary',d_error_detail_2,d_step_result_2);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_2 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_2 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'运营商报告社交分析摘要','pro_mobile_report_social_analysis_summary',d_error_detail_2,d_step_result_2);

		END;

-------------------------------------------------------------------------------------------------
	
--联系人Top(3月、6月)
	
		BEGIN 

--三月

			INSERT INTO pro_mobile_report_contacts_top
			(
				task_id,phone_num,city,communicate_counts,communicate_duration,dial_counts,
				called_counts,data_type
			)
				select
					a.task_id,
					a.his_num as phone_num,
					COALESCE(case when b.city is null 
								then c.city else b.city end,'未知') as city,
					count(1) as communicate_counts,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as called_counts,
					'三月' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix left join area_phone_item_code c
					on case when length(c.area_code) = 3 then substr(a.his_num,1,3) = c.area_code
									else substr(a.his_num,1,4) = c.area_code end
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by 
					a.task_id,
					a.his_num,
					b.city,
					c.city
				order by 
					sum(a.call_duration::numeric) desc;

--六月

			INSERT INTO pro_mobile_report_contacts_top
			(
				task_id,phone_num,city,communicate_counts,communicate_duration,dial_counts,
				called_counts,data_type
			)
				select
					a.task_id,
					a.his_num as phone_num,
					COALESCE(case when b.city is null 
								then c.city else b.city end,'未知') as city,
					count(1) as communicate_counts,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as called_counts,
					'六月' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix left join area_phone_item_code c
					on case when length(c.area_code) = 3 then substr(a.his_num,1,3) = c.area_code
									else substr(a.his_num,1,4) = c.area_code end
				where a.task_id = this_id
--				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by 
					a.task_id,
					a.his_num,
					b.city,
					c.city
				order by 
					sum(a.call_duration::numeric) desc;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'联系人Top','pro_mobile_report_contacts_top',d_error_detail_3,d_step_result_3);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_3 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_3 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'联系人Top','pro_mobile_report_contacts_top',d_error_detail_3,d_step_result_3);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_3 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_3 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'联系人Top','pro_mobile_report_contacts_top',d_error_detail_3,d_step_result_3);

		END;

-------------------------------------------------------------------------------------------------

--联系人号码归属地Top(3月、6月)

		BEGIN

--三月

			INSERT INTO pro_mobile_report_location_top
			(
				task_id,location,communicate_counts,phonenum_counts,communicate_duration,
				dial_counts,called_counts,dial_duration,called_duration,avg_dial_duration,
				avg_called_duration,dial_counts_proportion,called_counts_proportion,
				dial_duration_proportion,called_duration_proportion,data_type
			)
				select
					aa.task_id,
					aa.location,
					aa.communicate_counts,
					aa.phonenum_counts,
					aa.communicate_duration,
					aa.dial_counts,
					aa.called_counts,
					aa.dial_duration,
					aa.called_duration,
	    		case when aa.dial_counts = 0 then 0
								else round(aa.dial_duration/aa.dial_counts,2) end as avg_dial_duration,
	    		case when aa.called_counts = 0 then 0
								else round(aa.called_duration/aa.called_counts,2) end as avg_called_duration,
					case when aa.communicate_counts = 0 then 0
								else round(aa.dial_counts/aa.communicate_counts::numeric,2) end as dial_counts_proportion,
					case when aa.communicate_counts = 0 then 0
								else round(aa.called_counts/aa.communicate_counts::numeric,2) end as called_counts_proportion,
					case when aa.communicate_duration = 0 then 0
								else round(aa.dial_duration/aa.communicate_duration::numeric,2) end as dial_duration_proportion,
					case when aa.communicate_duration = 0 then 0
								else round(aa.called_duration/aa.communicate_duration::numeric,2) end as called_duration_proportion,
					'三月' as data_type
				from 
					(
						select
							a.task_id,
							COALESCE(case when b.city is null 
								then c.city else b.city end,'未知') as location,
							count(1) as communicate_counts,
							count(DISTINCT a.his_num) as phonenum_counts,
							sum(a.call_duration::numeric) as communicate_duration,
							sum(case when a.call_type = '被叫' then 1 else 0 end) as dial_counts,
							sum(case when a.call_type = '主叫' then 1 else 0 end) as called_counts,
							sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end) as dial_duration,
							sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end) as called_duration,
							'三月' as data_type
						from pro_mobile_call_info a left join dir_mobile_segment b
							on substr(a.his_num,1,7) = b.prefix left join area_phone_item_code c
							on case when length(c.area_code) = 3 then substr(a.his_num,1,3) = c.area_code
									else substr(a.his_num,1,4) = c.area_code end
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
						group by 
							a.task_id,
							COALESCE(case when b.city is null 
								then c.city else b.city end,'未知')
					) aa 
						order by 
							aa.communicate_duration desc;


--六月

			INSERT INTO pro_mobile_report_location_top
			(
				task_id,location,communicate_counts,phonenum_counts,communicate_duration,
				dial_counts,called_counts,dial_duration,called_duration,avg_dial_duration,
				avg_called_duration,dial_counts_proportion,called_counts_proportion,
				dial_duration_proportion,called_duration_proportion,data_type
			)
				select
					aa.task_id,
					aa.location,
					aa.communicate_counts,
					aa.phonenum_counts,
					aa.communicate_duration,
					aa.dial_counts,
					aa.called_counts,
					aa.dial_duration,
					aa.called_duration,
	    		case when aa.dial_counts = 0 then 0
								else round(aa.dial_duration/aa.dial_counts,2) end as avg_dial_duration,
	    		case when aa.called_counts = 0 then 0
								else round(aa.called_duration/aa.called_counts,2) end as avg_called_duration,
					case when aa.communicate_counts = 0 then 0
								else round(aa.dial_counts/aa.communicate_counts::numeric,2) end as dial_counts_proportion,
					case when aa.communicate_counts = 0 then 0
								else round(aa.called_counts/aa.communicate_counts::numeric,2) end as called_counts_proportion,
					case when aa.communicate_duration = 0 then 0
								else round(aa.dial_duration/aa.communicate_duration::numeric,2) end as dial_duration_proportion,
					case when aa.communicate_duration = 0 then 0
								else round(aa.called_duration/aa.communicate_duration::numeric,2) end as called_duration_proportion,
					'六月' as data_type
				from 
					(
						select
							a.task_id,
							COALESCE(case when b.city is null 
								then c.city else b.city end,'未知') as location,
							count(1) as communicate_counts,
							count(DISTINCT a.his_num) as phonenum_counts,
							sum(a.call_duration::numeric) as communicate_duration,
							sum(case when a.call_type = '被叫' then 1 else 0 end) as dial_counts,
							sum(case when a.call_type = '主叫' then 1 else 0 end) as called_counts,
							sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end) as dial_duration,
							sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end) as called_duration,
							'三月' as data_type
						from pro_mobile_call_info a left join dir_mobile_segment b
							on substr(a.his_num,1,7) = b.prefix left join area_phone_item_code c
							on case when length(c.area_code) = 3 then substr(a.his_num,1,3) = c.area_code
									else substr(a.his_num,1,4) = c.area_code end
						where a.task_id = this_id
--						and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
						group by 
							a.task_id,
							COALESCE(case when b.city is null 
								then c.city else b.city end,'未知')
					) aa 
						order by 
							aa.communicate_duration desc;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'联系人号码归属地Top','pro_mobile_report_location_top',d_error_detail_4,d_step_result_4);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_4 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_4 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'联系人号码归属地Top','pro_mobile_report_location_top',d_error_detail_4,d_step_result_4);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_4 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_4 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'联系人号码归属地Top','pro_mobile_report_location_top',d_error_detail_4,d_step_result_4);

		END;

-------------------------------------------------------------------------------------------------

--活跃分析摘要(3月、6月)
		
		BEGIN

--三月
		
			INSERT INTO pro_mobile_report_vitality_analysis_summary
			(
				task_id,call_days,dial_counts,called_counts,dial_duration,
				called_duration,data_type
			)
				select
					a.task_id,
					count(distinct to_char(a.call_time::timestamp,'yyyy-mm-dd')) as call_days,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end) as dial_duration,
					sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end) as called_duration,
					'三月' as data_type
				from pro_mobile_call_info a 
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by a.task_id;

--六月
		
			INSERT INTO pro_mobile_report_vitality_analysis_summary
			(
				task_id,call_days,dial_counts,called_counts,dial_duration,
				called_duration,data_type
			)
				select
					a.task_id,
					count(distinct to_char(a.call_time::timestamp,'yyyy-mm-dd')) as call_days,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end) as dial_duration,
					sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end) as called_duration,
					'六月' as data_type
				from pro_mobile_call_info a 
				where a.task_id = this_id
--				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by a.task_id;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'活跃分析摘要','pro_mobile_report_vitality_analysis_summary',d_error_detail_5,d_step_result_5);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_5 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_5 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'活跃分析摘要','pro_mobile_report_vitality_analysis_summary',d_error_detail_5,d_step_result_5);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_5 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_5 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'活跃分析摘要','pro_mobile_report_vitality_analysis_summary',d_error_detail_5,d_step_result_5);

		END;

-------------------------------------------------------------------------------------------------

--消费（1月、3月、6月、3月月均、6月月均）

	DECLARE
		p_data_count INT;

		BEGIN

--判断当前数据是否有账单数据

			select
				count(1) INTO STRICT p_data_count 
			from pro_mobile_bill_info a
			where task_id = this_id;

			IF p_data_count = 0 THEN  --如果账单没数据，只查询缴费信息

--一月

				INSERT INTO pro_mobile_report_consumeinfo
				(
					task_id,pay_count,max_pay,sum_pay,data_type
				)
					select 
						a.task_id,
						count(1) as pay_count,
						max(payfee::numeric) as max_pay,
						sum(payfee::numeric) as sum_pay,
						'一月' as data_type
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
					group by a.task_id;

--三月

				INSERT INTO pro_mobile_report_consumeinfo
				(
					task_id,pay_count,max_pay,sum_pay,data_type
				)
					select 
						a.task_id,
						count(1) as pay_count,
						max(payfee::numeric) as max_pay,
						sum(payfee::numeric) as sum_pay,
						'三月' as data_type
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by a.task_id;

--六月

				INSERT INTO pro_mobile_report_consumeinfo
				(
					task_id,pay_count,max_pay,sum_pay,data_type
				)
					select 
						a.task_id,
						count(1) as pay_count,
						max(payfee::numeric) as max_pay,
						sum(payfee::numeric) as sum_pay,
						'六月' as data_type
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by a.task_id;

--三月月均

				INSERT INTO pro_mobile_report_consumeinfo
				(
					task_id,pay_count,max_pay,sum_pay,data_type
				)
					select 
						a.task_id,
						round(count(1)::numeric/3,2) as pay_count,
						round(max(payfee::numeric)/3,2) as max_pay,
						round(sum(payfee::numeric)/3,2) as sum_pay,
						'三月月均' as data_type
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by a.task_id;

--六月月均

				INSERT INTO pro_mobile_report_consumeinfo
				(
					task_id,pay_count,max_pay,sum_pay,data_type
				)
					select 
						a.task_id,
						round(count(1)::numeric/6,2) as pay_count,
						round(max(payfee::numeric)/6,2) as max_pay,
						round(sum(payfee::numeric)/6,2) as sum_pay,
						'六月月均' as data_type
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by a.task_id;

			ELSE --如果账单有数据，正常查数据

--一月

			INSERT INTO pro_mobile_report_consumeinfo
			(
				task_id,newest_bill,sum_fee,flow_fee,voice_fee,message_fee,
				function_fee,other_fee,pay_count,max_pay,sum_pay,data_type
			)
			with payment as 
			(
				select 
					a.task_id,
					count(1) as pay_count,
					max(payfee::numeric) as max_pay,
					sum(payfee::numeric) as sum_pay,
					'一月' as data_type
				from pro_mobile_pay_info a 
				where a.task_id = this_id
				and a.paytime > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
				group by 
					a.task_id
			)
					select 
						a.task_id,
						max(bill_month) as newest_bill,
						sum(a.sum_fee::numeric) as sum_fee,
						sum(a.flow_fee::numeric) as flow_fee,
						sum(a.normal_call_fee::numeric+a.roam_call_fee::numeric) as voice_fee,
						sum(a.message_fee::numeric) as message_fee,
						sum(a.function_fee::numeric) as function_fee,
						sum(a.other_fee::numeric) as other_fee,
						COALESCE(b.pay_count,0) as pay_count,
						COALESCE(b.max_pay,0) as max_pay,
						COALESCE(b.sum_pay,0) as sum_pay,
						'一月' as data_type
					from pro_mobile_bill_info a left join payment b
					on a.task_id = b.task_id
					where a.task_id = this_id
					and a.bill_month >= to_char(now() - INTERVAL '1 month','yyyy-mm')
					group by 
						a.task_id,
						b.pay_count,
						b.max_pay,
						b.sum_pay;
			
--三月

			INSERT INTO pro_mobile_report_consumeinfo
			(
				task_id,newest_bill,sum_fee,flow_fee,voice_fee,message_fee,
				function_fee,other_fee,pay_count,max_pay,sum_pay,data_type
			)
			with payment as 
			(
				select 
					a.task_id,
					count(1) as pay_count,
					max(payfee::numeric) as max_pay,
					sum(payfee::numeric) as sum_pay,
					'三月' as data_type
				from pro_mobile_pay_info a 
				where a.task_id = this_id
				and a.paytime > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by 
					a.task_id
			)
					select 
						a.task_id,
						max(bill_month) as newest_bill,
						sum(a.sum_fee::numeric) as sum_fee,
						sum(a.flow_fee::numeric) as flow_fee,
						sum(a.normal_call_fee::numeric+a.roam_call_fee::numeric) as voice_fee,
						sum(a.message_fee::numeric) as message_fee,
						sum(a.function_fee::numeric) as function_fee,
						sum(a.other_fee::numeric) as other_fee,
						COALESCE(b.pay_count,0) as pay_count,
						COALESCE(b.max_pay,0) as max_pay,
						COALESCE(b.sum_pay,0) as sum_pay,
						'三月' as data_type
					from pro_mobile_bill_info a left join payment b
					on a.task_id = b.task_id
					where a.task_id = this_id
					and a.bill_month >= to_char(now() - INTERVAL '3 month','yyyy-mm')
					group by 
						a.task_id,
						b.pay_count,
						b.max_pay,
						b.sum_pay;

--六月

			INSERT INTO pro_mobile_report_consumeinfo
			(
				task_id,newest_bill,sum_fee,flow_fee,voice_fee,message_fee,
				function_fee,other_fee,pay_count,max_pay,sum_pay,data_type
			)
			with payment as 
			(
				select 
					a.task_id,
					count(1) as pay_count,
					max(payfee::numeric) as max_pay,
					sum(payfee::numeric) as sum_pay,
					'六月' as data_type
				from pro_mobile_pay_info a 
				where a.task_id = this_id
				and a.paytime > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				group by 
					a.task_id
			)
					select 
						a.task_id,
						max(bill_month) as newest_bill,
						sum(a.sum_fee::numeric) as sum_fee,
						sum(a.flow_fee::numeric) as flow_fee,
						sum(a.normal_call_fee::numeric+a.roam_call_fee::numeric) as voice_fee,
						sum(a.message_fee::numeric) as message_fee,
						sum(a.function_fee::numeric) as function_fee,
						sum(a.other_fee::numeric) as other_fee,
						COALESCE(b.pay_count,0) as pay_count,
						COALESCE(b.max_pay,0) as max_pay,
						COALESCE(b.sum_pay,0) as sum_pay,
						'六月' as data_type
					from pro_mobile_bill_info a left join payment b
					on a.task_id = b.task_id
					where a.task_id = this_id
					and a.bill_month >= to_char(now() - INTERVAL '6 month','yyyy-mm')
					group by 
						a.task_id,
						b.pay_count,
						b.max_pay,
						b.sum_pay;

--三月月均

			INSERT INTO pro_mobile_report_consumeinfo
			(
				task_id,newest_bill,sum_fee,flow_fee,voice_fee,message_fee,
				function_fee,other_fee,pay_count,max_pay,sum_pay,data_type
			)
			with payment as 
			(
				select 
					a.task_id,
					round(count(1)::numeric/3,2) as pay_count,
					round(max(payfee::numeric)::numeric/3,2) as max_pay,
					round(sum(payfee::numeric)::numeric/3,2) as sum_pay,
					'三月月均' as data_type
				from pro_mobile_pay_info a 
				where a.task_id = this_id
				and a.paytime > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by 
					a.task_id
			)
					select 
						a.task_id,
						max(bill_month) as newest_bill,
						round(sum(a.sum_fee::numeric)::numeric/3,2) as sum_fee,
						round(sum(a.flow_fee::numeric)::numeric/3,2) as flow_fee,
						round(sum(a.normal_call_fee::numeric+a.roam_call_fee::numeric)::numeric/3,2) as voice_fee,
						round(sum(a.message_fee::numeric)::numeric/3,2) as message_fee,
						round(sum(a.function_fee::numeric)::numeric/3,2) as function_fee,
						round(sum(a.other_fee::numeric)::numeric/3,2) as other_fee,
						COALESCE(b.pay_count,0) as pay_count,
						COALESCE(b.max_pay,0) as max_pay,
						COALESCE(b.sum_pay,0) as sum_pay,
						'三月月均' as data_type
					from pro_mobile_bill_info a left join payment b
					on a.task_id = b.task_id
					where a.task_id = this_id
					and a.bill_month >= to_char(now() - INTERVAL '3 month','yyyy-mm')
					group by 
						a.task_id,
						b.pay_count,
						b.max_pay,
						b.sum_pay;

--六月月均

			INSERT INTO pro_mobile_report_consumeinfo
			(
				task_id,newest_bill,sum_fee,flow_fee,voice_fee,message_fee,
				function_fee,other_fee,pay_count,max_pay,sum_pay,data_type
			)
			with payment as 
			(
				select 
					a.task_id,
					round(count(1)::numeric/6,2) as pay_count,
					round(max(payfee::numeric)::numeric/6,2) as max_pay,
					round(sum(payfee::numeric)::numeric/6,2) as sum_pay,
					'六月月均' as data_type
				from pro_mobile_pay_info a 
				where a.task_id = this_id
				and a.paytime > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				group by 
					a.task_id
			)
					select 
						a.task_id,
						max(bill_month) as newest_bill,
						round(sum(a.sum_fee::numeric)::numeric/6,2) as sum_fee,
						round(sum(a.flow_fee::numeric)::numeric/6,2) as flow_fee,
						round(sum(a.normal_call_fee::numeric+a.roam_call_fee::numeric)::numeric/6,2) as voice_fee,
						round(sum(a.message_fee::numeric)::numeric/6,2) as message_fee,
						round(sum(a.function_fee::numeric)::numeric/6,2) as function_fee,
						round(sum(a.other_fee::numeric)::numeric/6,2) as other_fee,
						COALESCE(b.pay_count,0) as pay_count,
						COALESCE(b.max_pay,0) as max_pay,
						COALESCE(b.sum_pay,0) as sum_pay,
						'六月月均' as data_type
					from pro_mobile_bill_info a left join payment b
					on a.task_id = b.task_id
					where a.task_id = this_id
					and a.bill_month >= to_char(now() - INTERVAL '6 month','yyyy-mm')
					group by 
						a.task_id,
						b.pay_count,
						b.max_pay,
						b.sum_pay;

			END IF;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'消费','pro_mobile_report_consumeinfo',d_error_detail_6,d_step_result_6);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_6 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_6 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'消费','pro_mobile_report_consumeinfo',d_error_detail_6,d_step_result_6);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_6 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_6 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'消费','pro_mobile_report_consumeinfo',d_error_detail_6,d_step_result_6);

		END;

------------------------------------------------------------------------------------------------

--运营商消费数据（每月统计）

		BEGIN

			INSERT INTO pro_mobile_report_carrier_consume
			(
				task_id,call_month,communicate_counts,dial_counts,called_counts,
				dial_duration,called_duration,voice_fee,message_counts,carrier,
				phonenum,city
			)
			WITH message_info as 
			(
				select 
					a.task_id as task_id,
					substr(a.send_time,1,7) as message_month,
					count(1) as message_counts
				from pro_mobile_sms_info a
				where task_id = this_id
				group by 
					a.task_id,
					substr(a.send_time,1,7)
			)
				select 
					a.task_id as task_id,
					substr(a.call_time,1,7) as call_month,
					count(1) as communicate_counts,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end) as dial_duration,
					sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end) as called_duration,
					sum(fee::numeric) as voice_fee,
					b.message_counts,
					c.carrier,
					c.phone_num as phonenum,
					c.city
				from pro_mobile_call_info a left join message_info b 
					on a.task_id = b.task_id and substr(a.call_time,1,7) = b.message_month left join pro_mobile_user_info c
					on a.task_id = c.task_id 
				where a.task_id = this_id
				group by 
					a.task_id,
					substr(a.call_time,1,7),
					b.message_counts,
					c.carrier,
					c.phone_num,
					c.city;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'运营商消费数据','pro_mobile_report_carrier_consume',d_error_detail_7,d_step_result_7);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_7 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_7 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'运营商消费数据','pro_mobile_report_carrier_consume',d_error_detail_7,d_step_result_7);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_7 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_7 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'运营商消费数据','pro_mobile_report_carrier_consume',d_error_detail_7,d_step_result_7);

		END;

-----------------------------------------------------------------------------------------------

--总体统计（1月、3月、6月、3月月均、6月月均）

		BEGIN

--一月

			INSERT INTO pro_mobile_report_call_sum_statistics
			(
				task_id,communicate_counts,contact_counts,city_counts,dial_counts,
				called_counts,called_nums,dial_nums,duration_sum,data_type
			)
				select 
					a.task_id,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_counts,
					count(distinct b.city) as city_counts,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					(count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1) as called_nums,
					(count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1) as dial_nums,
					sum(a.call_duration::numeric) as duration_sum,
					'一月' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix 
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
				group by a.task_id;

--三月

			INSERT INTO pro_mobile_report_call_sum_statistics
			(
				task_id,communicate_counts,contact_counts,city_counts,dial_counts,
				called_counts,called_nums,dial_nums,duration_sum,data_type
			)
				select 
					a.task_id,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_counts,
					count(distinct b.city) as city_counts,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					(count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1) as called_nums,
					(count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1) as dial_nums,
					sum(a.call_duration::numeric) as duration_sum,
					'三月' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix 
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by a.task_id;

--六月

			INSERT INTO pro_mobile_report_call_sum_statistics
			(
				task_id,communicate_counts,contact_counts,city_counts,dial_counts,
				called_counts,called_nums,dial_nums,duration_sum,data_type
			)
				select 
					a.task_id,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_counts,
					count(distinct b.city) as city_counts,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					(count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1) as called_nums,
					(count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1) as dial_nums,
					sum(a.call_duration::numeric) as duration_sum,
					'六月' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix 
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				group by a.task_id;

--三月月均

			INSERT INTO pro_mobile_report_call_sum_statistics
			(
				task_id,communicate_counts,contact_counts,city_counts,dial_counts,
				called_counts,called_nums,dial_nums,duration_sum,data_type
			)
				select 
					a.task_id,
					round(count(1)::numeric/3,2) as communicate_counts,
					round(count(distinct a.his_num)::numeric/3,2) as contact_counts,
					round(count(distinct b.city)::numeric/3,2) as city_counts,
					round(sum(case when a.call_type = '主叫' then 1 else 0 end)::numeric/3,2) as dial_counts,
					round(sum(case when a.call_type = '被叫' then 1 else 0 end)::numeric/3,2) as called_counts,
					round((count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1)::numeric/3,2) as called_nums,
					round((count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1)::numeric/3,2) as dial_nums,
					round(sum(a.call_duration::numeric)::numeric/3,2) as duration_sum,
					'三月月均' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix 
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by a.task_id;


--六月月均

			INSERT INTO pro_mobile_report_call_sum_statistics
			(
				task_id,communicate_counts,contact_counts,city_counts,dial_counts,
				called_counts,called_nums,dial_nums,duration_sum,data_type
			)
				select 
					a.task_id,
					round(count(1)::numeric/6,2) as communicate_counts,
					round(count(distinct a.his_num)::numeric/6,2) as contact_counts,
					round(count(distinct b.city)::numeric/6,2) as city_counts,
					round(sum(case when a.call_type = '主叫' then 1 else 0 end)::numeric/6,2) as dial_counts,
					round(sum(case when a.call_type = '被叫' then 1 else 0 end)::numeric/6,2) as called_counts,
					round((count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1)::numeric/6,2) as called_nums,
					round((count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1)::numeric/6,2) as dial_nums,
					round(sum(a.call_duration::numeric)::numeric/6,2) as duration_sum,
					'六月月均' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix 
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				group by a.task_id;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'总体统计','pro_mobile_report_call_sum_statistics',d_error_detail_8,d_step_result_8);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_8 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_8 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'总体统计','pro_mobile_report_call_sum_statistics',d_error_detail_8,d_step_result_8);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_8 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_8 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'总体统计','pro_mobile_report_call_sum_statistics',d_error_detail_8,d_step_result_8);

		END;

--------------------------------------------------------------------------------------------------
		
--通话详细统计（三月，六月）

		BEGIN

--三月

			INSERT INTO pro_mobile_report_call_detail_statistics
			(
				task_id,phonenum,phonenum_flag,phonenum_type,city,communicate_counts,
				communicate_duration,dial_counts,called_counts,morning_counts,noon_counts,
				afternoon_counts,night_counts,midnight_counts,weekdays_counts,weekends_counts,
				holiday_counts,data_type
			)
				select 
					a.task_id as task_id,
					a.his_num as phonenum,
					'' as phonenum_flag,
					'' as phonenum_type,
					COALESCE(case when b.city is null 
								then d.city else b.city end,'未知') as city,
					count(1) as communicate_counts,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as called_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','05:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','11:30:00')::timestamp
								then 1 else 0 end) as morning_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','11:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','13:30:00')::timestamp
								then 1 else 0 end) as noon_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','13:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','17:30:00')::timestamp
								then 1 else 0 end) as afternoon_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','17:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','23:30:00')::timestamp
								then 1 else 0 end) as night_counts,	
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','23:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(to_char(CURRENT_DATE + INTERVAL '1 day','yyyy-mm-dd'),' ','05:30:00')::timestamp
								then 1 else 0 end) as midnight_counts,
					sum(case when c.is_work = '1' then 1 else 0 end) as weekdays_counts,
					sum(case when c.day_of_week in ('星期六','星期日') and c.is_work = '0' then 1 else 0 end) as weekends_counts,
					sum(case when c.is_work = '0' then 1 else 0 end) as holiday_counts,
					'三月' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix left join area_phone_item_code d
					on case when length(d.area_code) = 3 then substr(a.his_num,1,3) = d.area_code
									else substr(a.his_num,1,4) = d.area_code end left join calendar_table c
					on to_char(a.call_time::timestamp,'yyyy-mm-dd') = c.cal_date
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by 
					a.task_id,
					a.his_num,
					COALESCE(case when b.city is null 
								then d.city else b.city end,'未知');

--六月

			INSERT INTO pro_mobile_report_call_detail_statistics
			(
				task_id,phonenum,phonenum_flag,phonenum_type,city,communicate_counts,
				communicate_duration,dial_counts,called_counts,morning_counts,noon_counts,
				afternoon_counts,night_counts,midnight_counts,weekdays_counts,weekends_counts,
				holiday_counts,data_type
			)
				select 
					a.task_id as task_id,
					a.his_num as phonenum,
					'' as phonenum_flag,
					'' as phonenum_type,
					COALESCE(case when b.city is null 
								then d.city else b.city end,'未知') as city,
					count(1) as communicate_counts,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as called_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','05:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','11:30:00')::timestamp
								then 1 else 0 end) as morning_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','11:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','13:30:00')::timestamp
								then 1 else 0 end) as noon_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','13:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','17:30:00')::timestamp
								then 1 else 0 end) as afternoon_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','17:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','23:30:00')::timestamp
								then 1 else 0 end) as night_counts,	
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','23:30:00')::timestamp
									and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(to_char(CURRENT_DATE + INTERVAL '1 day','yyyy-mm-dd'),' ','05:30:00')::timestamp
								then 1 else 0 end) as midnight_counts,
					sum(case when c.is_work = '1' then 1 else 0 end) as weekdays_counts,
					sum(case when c.day_of_week in ('星期六','星期日') and c.is_work = '0' then 1 else 0 end) as weekends_counts,
					sum(case when c.is_work = '0' then 1 else 0 end) as holiday_counts,
					'六月' as data_type
				from pro_mobile_call_info a left join dir_mobile_segment b
					on substr(a.his_num,1,7) = b.prefix left join area_phone_item_code d
					on case when length(d.area_code) = 3 then substr(a.his_num,1,3) = d.area_code
									else substr(a.his_num,1,4) = d.area_code end left join calendar_table c
					on to_char(a.call_time::timestamp,'yyyy-mm-dd') = c.cal_date
				where a.task_id = this_id
--				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by 
					a.task_id,
					a.his_num,
					COALESCE(case when b.city is null 
								then d.city else b.city end,'未知');

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'通话详细统计','pro_mobile_report_call_detail_statistics',d_error_detail_9,d_step_result_9);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_9 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_9 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'通话详细统计','pro_mobile_report_call_detail_statistics',d_error_detail_9,d_step_result_9);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_9 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_9 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'通话详细统计','pro_mobile_report_call_detail_statistics',d_error_detail_9,d_step_result_9);

		END;

--------------------------------------------------------------------------------------------------

--通话时段（三月、六月）

		BEGIN

--三月

			INSERT INTO pro_mobile_report_period_statistics
			(
				task_id,communicate_period,communicate_counts,contact_nums,communicate_duration,
				dial_counts,called_counts,last_call,first_call,data_type
			)
				select 
					a.task_id,
					'5:30-11:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'三月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','05:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','11:30:00')::timestamp
				group by 
					a.task_id
				UNION
				select 
					a.task_id,
					'11:30-13:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'三月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','11:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','13:30:00')::timestamp
				group by 
					a.task_id
				UNION
				select 
					a.task_id,
					'13:30-17:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'三月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','13:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','17:30:00')::timestamp
				group by 
					a.task_id
				UNION
				select 
					a.task_id,
					'17:30-23:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'三月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','17:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','23:30:00')::timestamp
				group by 
					a.task_id
				UNION
				select 
					a.task_id,
					'23:30-05:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'三月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','23:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(to_char(CURRENT_DATE + INTERVAL '1 day','yyyy-mm-dd'),' ','05:30:00')::timestamp
				group by
					a.task_id;

--六月

			INSERT INTO pro_mobile_report_period_statistics
			(
				task_id,communicate_period,communicate_counts,contact_nums,communicate_duration,
				dial_counts,called_counts,last_call,first_call,data_type
			)
				select 
					a.task_id,
					'5:30-11:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'六月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','05:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','11:30:00')::timestamp
				group by 
					a.task_id
				UNION
				select 
					a.task_id,
					'11:30-13:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'六月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','11:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','13:30:00')::timestamp
				group by 
					a.task_id
				UNION
				select 
					a.task_id,
					'13:30-17:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'六月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','13:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','17:30:00')::timestamp
				group by 
					a.task_id
				UNION
				select 
					a.task_id,
					'17:30-23:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'六月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','17:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','23:30:00')::timestamp
				group by 
					a.task_id
				UNION
				select 
					a.task_id,
					'23:30-05:30' as communicate_period,
					count(1) as communicate_counts,
					count(distinct a.his_num) as contact_nums,
					sum(a.call_duration::numeric) as communicate_duration,
					sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
					sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
					max(a.call_time::timestamp) as last_call,
					min(a.call_time::timestamp) as first_call,
					'六月' as data_type
				from pro_mobile_call_info a
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','23:30:00')::timestamp
				and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(to_char(CURRENT_DATE + INTERVAL '1 day','yyyy-mm-dd'),' ','05:30:00')::timestamp
				group by
					a.task_id;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'通话时段','pro_mobile_report_period_statistics',d_error_detail_10,d_step_result_10);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_10 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_10 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'通话时段','pro_mobile_report_period_statistics',d_error_detail_10,d_step_result_10);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_10 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_10 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'通话时段','pro_mobile_report_period_statistics',d_error_detail_10,d_step_result_10);

		END;

--------------------------------------------------------------------------------------------------

--通话时间详细统计（1月、3月、6月、3月月均、6月月均）

		BEGIN 

--一月

			INSERT INTO pro_mobile_report_calltime_detail_statistics
			(
				task_id,longgest_call,shortest_call,call_within_oneminute,call_between_oneminute_fiveminutes,
				call_between_fiveminutes_tenminutes,call_over_tenminutes,daytime_counts,night_counts,
				daytime_duration,night_duration,in_local_counts,out_local_counts,in_local_duration,
				out_local_duration,data_type
			)
				select 
					a.task_id,
					max(a.call_duration::numeric) as longgest_call,
					min(a.call_duration::numeric) as shortest_call,
					sum(case when a.call_duration::numeric < 60 then 1 else 0 end) as call_within_oneminute,
					sum(case when a.call_duration::numeric >= 60 and a.call_duration::numeric <300
							then 1 else 0 end) as call_between_oneminute_fiveminutes,
					sum(case when a.call_duration::numeric >= 300 and a.call_duration::numeric <600
										then 1 else 0 end) as call_between_fiveminutes_tenminutes,
					sum(case when a.call_duration::numeric >= 600 then 1 else 0 end) as call_over_tenminutes,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
										then 1 else 0 end) as daytime_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
										then 1 else 0 end) as night_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
										then a.call_duration::numeric else 0 end) as daytime_duration,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
										then a.call_duration::numeric else 0 end) as night_duration,
					sum(case when a.call_location = c.city and a.call_location <> '' then 1 else 0 end) as in_local_counts,
					sum(case when a.call_location <> c.city and a.call_location <> '' then 1 else 0 end) as out_local_counts,
					sum(case when a.call_location = c.city and a.call_location <> '' then a.call_duration::numeric else 0 end) as in_local_duration,
					sum(case when a.call_location <> c.city and a.call_location <> '' then a.call_duration::numeric else 0 end) as out_local_duration,
					'一月' as data_type
				from pro_mobile_call_info a left join pro_mobile_user_info c
				on a.task_id = c.task_id
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
				group by 
					a.task_id;

--三月

			INSERT INTO pro_mobile_report_calltime_detail_statistics
			(
				task_id,longgest_call,shortest_call,call_within_oneminute,call_between_oneminute_fiveminutes,
				call_between_fiveminutes_tenminutes,call_over_tenminutes,daytime_counts,night_counts,
				daytime_duration,night_duration,in_local_counts,out_local_counts,in_local_duration,
				out_local_duration,data_type
			)
				select 
					a.task_id,
					max(a.call_duration::numeric) as longgest_call,
					min(a.call_duration::numeric) as shortest_call,
					sum(case when a.call_duration::numeric < 60 then 1 else 0 end) as call_within_oneminute,
					sum(case when a.call_duration::numeric >= 60 and a.call_duration::numeric <300
							then 1 else 0 end) as call_between_oneminute_fiveminutes,
					sum(case when a.call_duration::numeric >= 300 and a.call_duration::numeric <600
										then 1 else 0 end) as call_between_fiveminutes_tenminutes,
					sum(case when a.call_duration::numeric >= 600 then 1 else 0 end) as call_over_tenminutes,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
										then 1 else 0 end) as daytime_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
										then 1 else 0 end) as night_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
										then a.call_duration::numeric else 0 end) as daytime_duration,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
										then a.call_duration::numeric else 0 end) as night_duration,
					sum(case when a.call_location = c.city and a.call_location <> '' then 1 else 0 end) as in_local_counts,
					sum(case when a.call_location <> c.city and a.call_location <> '' then 1 else 0 end) as out_local_counts,
					sum(case when a.call_location = c.city and a.call_location <> '' then a.call_duration::numeric else 0 end) as in_local_duration,
					sum(case when a.call_location <> c.city and a.call_location <> '' then a.call_duration::numeric else 0 end) as out_local_duration,
					'三月' as data_type
				from pro_mobile_call_info a left join pro_mobile_user_info c
				on a.task_id = c.task_id
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by 
					a.task_id;

--六月

			INSERT INTO pro_mobile_report_calltime_detail_statistics
			(
				task_id,longgest_call,shortest_call,call_within_oneminute,call_between_oneminute_fiveminutes,
				call_between_fiveminutes_tenminutes,call_over_tenminutes,daytime_counts,night_counts,
				daytime_duration,night_duration,in_local_counts,out_local_counts,in_local_duration,
				out_local_duration,data_type
			)
				select 
					a.task_id,
					max(a.call_duration::numeric) as longgest_call,
					min(a.call_duration::numeric) as shortest_call,
					sum(case when a.call_duration::numeric < 60 then 1 else 0 end) as call_within_oneminute,
					sum(case when a.call_duration::numeric >= 60 and a.call_duration::numeric <300
							then 1 else 0 end) as call_between_oneminute_fiveminutes,
					sum(case when a.call_duration::numeric >= 300 and a.call_duration::numeric <600
										then 1 else 0 end) as call_between_fiveminutes_tenminutes,
					sum(case when a.call_duration::numeric >= 600 then 1 else 0 end) as call_over_tenminutes,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
										then 1 else 0 end) as daytime_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
										then 1 else 0 end) as night_counts,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
										then a.call_duration::numeric else 0 end) as daytime_duration,
					sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
											and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
										then a.call_duration::numeric else 0 end) as night_duration,
					sum(case when a.call_location = c.city and a.call_location <> '' then 1 else 0 end) as in_local_counts,
					sum(case when a.call_location <> c.city and a.call_location <> '' then 1 else 0 end) as out_local_counts,
					sum(case when a.call_location = c.city and a.call_location <> '' then a.call_duration::numeric else 0 end) as in_local_duration,
					sum(case when a.call_location <> c.city and a.call_location <> '' then a.call_duration::numeric else 0 end) as out_local_duration,
					'六月' as data_type
				from pro_mobile_call_info a left join pro_mobile_user_info c
				on a.task_id = c.task_id
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				group by 
					a.task_id;

--三月月均

			INSERT INTO pro_mobile_report_calltime_detail_statistics
			(
				task_id,longgest_call,shortest_call,call_within_oneminute,call_between_oneminute_fiveminutes,
				call_between_fiveminutes_tenminutes,call_over_tenminutes,daytime_counts,night_counts,
				daytime_duration,night_duration,in_local_counts,out_local_counts,in_local_duration,
				out_local_duration,data_type
			)
				select 
					a.task_id,
					max(a.call_duration::numeric) as longgest_call,
					min(a.call_duration::numeric) as shortest_call,
					round(sum(case when a.call_duration::numeric < 60 then 1 else 0 end)::numeric/3,2) as call_within_oneminute,
					round(sum(case when a.call_duration::numeric >= 60 and a.call_duration::numeric <300
							then 1 else 0 end)::numeric/3,2) as call_between_oneminute_fiveminutes,
					round(sum(case when a.call_duration::numeric >= 300 and a.call_duration::numeric <600
											then 1 else 0 end)::numeric/3,2) as call_between_fiveminutes_tenminutes,
					round(sum(case when a.call_duration::numeric >= 600 then 1 else 0 end)::numeric/3,2) as call_over_tenminutes,
					round(sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
															and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
														then 1 else 0 end)::numeric/3,2) as daytime_counts,
					round(sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
															and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
														then 1 else 0 end)::numeric/3,2) as night_counts,
					round(sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
															and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
														then a.call_duration::numeric else 0 end)::numeric/3,2) as daytime_duration,
					round(sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
															and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
														then a.call_duration::numeric else 0 end)::numeric/3,2) as night_duration,
					round(sum(case when a.call_location = c.city and a.call_location <> '' then 1 else 0 end)::numeric/3,2) as in_local_counts,
					round(sum(case when a.call_location <> c.city and a.call_location <> '' then 1 else 0 end)::numeric/3,2) as out_local_counts,
					round(sum(case when a.call_location = c.city and a.call_location <> '' then a.call_duration::numeric else 0 end)/3,2) as in_local_duration,
					round(sum(case when a.call_location <> c.city and a.call_location <> '' then a.call_duration::numeric else 0 end)/3,2) as out_local_duration,
					'三月月均' as data_type
				from pro_mobile_call_info a left join pro_mobile_user_info c
					on a.task_id = c.task_id
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				group by 
					a.task_id;


--六月月均

			INSERT INTO pro_mobile_report_calltime_detail_statistics
			(
				task_id,longgest_call,shortest_call,call_within_oneminute,call_between_oneminute_fiveminutes,
				call_between_fiveminutes_tenminutes,call_over_tenminutes,daytime_counts,night_counts,
				daytime_duration,night_duration,in_local_counts,out_local_counts,in_local_duration,
				out_local_duration,data_type
			)
				select 
					a.task_id,
					max(a.call_duration::numeric) as longgest_call,
					min(a.call_duration::numeric) as shortest_call,
					round(sum(case when a.call_duration::numeric < 60 then 1 else 0 end)::numeric/6,2) as call_within_oneminute,
					round(sum(case when a.call_duration::numeric >= 60 and a.call_duration::numeric <300
							then 1 else 0 end)::numeric/6,2) as call_between_oneminute_fiveminutes,
					round(sum(case when a.call_duration::numeric >= 300 and a.call_duration::numeric <600
											then 1 else 0 end)::numeric/6,2) as call_between_fiveminutes_tenminutes,
					round(sum(case when a.call_duration::numeric >= 600 then 1 else 0 end)::numeric/6,2) as call_over_tenminutes,
					round(sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
															and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
														then 1 else 0 end)::numeric/6,2) as daytime_counts,
					round(sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
															and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
														then 1 else 0 end)::numeric/6,2) as night_counts,
					round(sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','07:00:00')::timestamp
															and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','24:00:00')::timestamp
														then a.call_duration::numeric else 0 end)::numeric/6,2) as daytime_duration,
					round(sum(case when concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp >= concat(CURRENT_DATE,' ','00:00:00')::timestamp
															and concat(CURRENT_DATE,' ',split_part(a.call_time,' ',2))::timestamp < concat(CURRENT_DATE,' ','07:00:00')::timestamp
														then a.call_duration::numeric else 0 end)::numeric/6,2) as night_duration,
					round(sum(case when a.call_location = c.city and a.call_location <> '' then 1 else 0 end)::numeric/6,2) as in_local_counts,
					round(sum(case when a.call_location <> c.city and a.call_location <> '' then 1 else 0 end)::numeric/6,2) as out_local_counts,
					round(sum(case when a.call_location = c.city and a.call_location <> '' then a.call_duration::numeric else 0 end)/6,2) as in_local_duration,
					round(sum(case when a.call_location <> c.city and a.call_location <> '' then a.call_duration::numeric else 0 end)/6,2) as out_local_duration,
					'六月月均' as data_type
				from pro_mobile_call_info a left join pro_mobile_user_info c
					on a.task_id = c.task_id
				where a.task_id = this_id
				and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				group by 
					a.task_id;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'通话时间详细统计','pro_mobile_report_calltime_detail_statistics',d_error_detail_11,d_step_result_11);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_11 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_11 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'通话时间详细统计','pro_mobile_report_calltime_detail_statistics',d_error_detail_11,d_step_result_11);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_11 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_11 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'通话时间详细统计','pro_mobile_report_calltime_detail_statistics',d_error_detail_11,d_step_result_11);

		END;

-------------------------------------------------------------------------------------------------------

--稳定性（1月、3月、6月）

	DECLARE
		p_family_call_record INT;
		p_call_record INT;
		p_pay_record INT;
		p_is_family text;
		p_is_family_host text default '未知';
		p_pay_continuity_month_one_month text default '';
		p_pay_continuity_month_three_month text default '';
		p_pay_continuity_month_six_month text default '';
		
		BEGIN

--判断亲情号

			select
				count(1) INTO STRICT p_family_call_record 
			from pro_mobile_call_info a
			where task_id = this_id
			and (position('亲情' in a.comment) <> 0 or position('亲友' in a.comment) <> 0);
			
			IF p_family_call_record > 0 THEN 
				p_is_family = '是';
			ELSE 
				p_is_family = '否';
			END IF;
			

--获取连续缴费月数

			select
				count(1) INTO STRICT p_pay_record 
			from pro_mobile_pay_info a
			where task_id = this_id;

			IF p_pay_record = 0 THEN 

				p_pay_continuity_month_one_month = '0';
				p_pay_continuity_month_three_month = '0';
				p_pay_continuity_month_six_month = '0';

			ELSE 

--一月数据

				with tt as 
				(
					SELECT 
						DISTINCT
							a.task_id as task_id,
							concat(a.paymonth,'-01') AS paytime
					FROM
						pro_mobile_pay_info a
					WHERE a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
				)
					select
						COALESCE(max(ge),0) as pay_continuity_month INTO p_pay_continuity_month_one_month
					from 
					(
						select 
							task_id,
							count(1) over(partition by task_id, he) ge 
						from
							(
								select 
									task_id,
									sum(fen) over(partition by task_id order by paytime) he from 
								(
									select 
										task_id,
										paytime,
										lag(paytime) over(partition by task_id order by paytime)::TIMESTAMP,
										(case
											when paytime::timestamp - INTERVAL '1 month' = lag(paytime) over(partition by task_id order by paytime)::TIMESTAMP 
											then 0 else 1 end) fen
									from tt
								) aa ) bb ) cc 
						group by cc.task_id;

--三月数据

				with tt as 
				(
					SELECT 
						DISTINCT
							a.task_id as task_id,
							concat(a.paymonth,'-01') AS paytime
					FROM
						pro_mobile_pay_info a
					WHERE a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
				)
					select
						COALESCE(max(ge),0) as pay_continuity_month INTO p_pay_continuity_month_three_month
					from 
					(
						select 
							task_id,
							count(1) over(partition by task_id, he) ge 
						from
							(
								select 
									task_id,
									sum(fen) over(partition by task_id order by paytime) he from 
								(
									select 
										task_id,
										paytime,
										lag(paytime) over(partition by task_id order by paytime)::TIMESTAMP,
										(case
											when paytime::timestamp - INTERVAL '1 month' = lag(paytime) over(partition by task_id order by paytime)::TIMESTAMP 
											then 0 else 1 end) fen
									from tt
								) aa ) bb ) cc 
						group by cc.task_id;

--六月数据

				with tt as 
				(
					SELECT 
						DISTINCT
							a.task_id as task_id,
							concat(a.paymonth,'-01') AS paytime
					FROM
						pro_mobile_pay_info a
					WHERE a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
				)
					select
						COALESCE(max(ge),0) as pay_continuity_month INTO p_pay_continuity_month_six_month
					from 
					(
						select 
							task_id,
							count(1) over(partition by task_id, he) ge 
						from
							(
								select 
									task_id,
									sum(fen) over(partition by task_id order by paytime) he from 
								(
									select 
										task_id,
										paytime,
										lag(paytime) over(partition by task_id order by paytime)::TIMESTAMP,
										(case
											when paytime::timestamp - INTERVAL '1 month' = lag(paytime) over(partition by task_id order by paytime)::TIMESTAMP 
											then 0 else 1 end) fen
									from tt
								) aa ) bb ) cc 
						group by cc.task_id;

			END IF;


--插入数据

--一月

			INSERT INTO pro_mobile_report_stability
			(
				task_id,communicate_counts_equal_month,communicate_counts_equal_months,
				is_addr_equal_city,is_family,is_family_host,pay_continuity_month,data_type
			)
				select 
					aa.task_id,
					case when aa.communicate_counts::numeric > used_month::numeric*1 
								then '是' else '否' end as communicate_counts_equal_month,
					case when aa.communicate_counts::numeric > used_month::numeric*300 
								then '是' else '否' end as communicate_counts_equal_months,
					case when position(aa.city in aa.address) <> 0 
								then '是' else '否' end as is_addr_equal_city,
					p_is_family as is_family,
					p_is_family_host as is_family_host,
					COALESCE(p_pay_continuity_month_one_month,'0') as pay_continuity_month,
					'一月' as data_type
				from
				(
					select
						a.task_id,
						count(1) as communicate_counts,
						round(b.net_in_duration::numeric/30) as used_month,
						b.address,
						b.city
					from pro_mobile_call_info a left join pro_mobile_user_info b
					on a.task_id = b.task_id
					where a.task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
					group by 
						a.task_id,
						b.net_in_duration,
						b.address,
						b.city
					) aa;

--三月

			INSERT INTO pro_mobile_report_stability
			(
				task_id,communicate_counts_equal_month,communicate_counts_equal_months,
				is_addr_equal_city,is_family,is_family_host,pay_continuity_month,data_type
			)
				select 
					aa.task_id,
					case when aa.communicate_counts::numeric > used_month::numeric*1 
								then '是' else '否' end as communicate_counts_equal_month,
					case when aa.communicate_counts::numeric > used_month::numeric*300 
								then '是' else '否' end as communicate_counts_equal_months,
					case when position(aa.city in aa.address) <> 0 
								then '是' else '否' end as is_addr_equal_city,
					p_is_family as is_family,
					p_is_family_host as is_family_host,
					COALESCE(p_pay_continuity_month_three_month,'0') as pay_continuity_month,
					'三月' as data_type
				from
				(
					select
						a.task_id,
						count(1) as communicate_counts,
						round(b.net_in_duration::numeric/30) as used_month,
						b.address,
						b.city
					from pro_mobile_call_info a left join pro_mobile_user_info b
					on a.task_id = b.task_id
					where a.task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by 
						a.task_id,
						b.net_in_duration,
						b.address,
						b.city
					) aa;

--六月

			INSERT INTO pro_mobile_report_stability
			(
				task_id,communicate_counts_equal_month,communicate_counts_equal_months,
				is_addr_equal_city,is_family,is_family_host,pay_continuity_month,data_type
			)
				select 
					aa.task_id,
					case when aa.communicate_counts::numeric > used_month::numeric*1 
								then '是' else '否' end as communicate_counts_equal_month,
					case when aa.communicate_counts::numeric > used_month::numeric*300 
								then '是' else '否' end as communicate_counts_equal_months,
					case when position(aa.city in aa.address) <> 0 
								then '是' else '否' end as is_addr_equal_city,
					p_is_family as is_family,
					p_is_family_host as is_family_host,
					COALESCE(p_pay_continuity_month_six_month,'0') as pay_continuity_month,
					'六月' as data_type
				from
				(
					select
						a.task_id,
						count(1) as communicate_counts,
						round(b.net_in_duration::numeric/30) as used_month,
						b.address,
						b.city
					from pro_mobile_call_info a left join pro_mobile_user_info b
					on a.task_id = b.task_id
					where a.task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by 
						a.task_id,
						b.net_in_duration,
						b.address,
						b.city
					) aa;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'稳定性','pro_mobile_report_stability',d_error_detail_12,d_step_result_12);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_12 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_12 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'稳定性','pro_mobile_report_stability',d_error_detail_12,d_step_result_12);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_12 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_12 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'稳定性','pro_mobile_report_stability',d_error_detail_12,d_step_result_12);

		END;

--------------------------------------------------------------------------------------------------

--活跃程度（1月、3月、6月、3月月均、6月月均）

		BEGIN 

--一月

			INSERT INTO pro_mobile_report_vitality
			(
				task_id,communicate_days,communicate_counts,contact_nums,location_counts,dial_counts,
				called_counts,called_nums,dial_nums,communicate_duration,dial_duration,called_duration,
				avg_duration,message_days,message_counts,pay_counts,without_dial_call_days,without_call_days,
				without_dial_call_days_proportion,without_call_days_proportion,data_type
			)
				with message_info as 
				(
					select
						a.task_id as task_id,
						count(distinct to_char(a.send_time::timestamp,'yyyy-mm-dd')) as message_days,
						count(1) as message_counts
					from pro_mobile_sms_info a 
					where a.task_id = this_id
					and a.send_time > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
					group by a.task_id
				),
				pay_info as 
				(
					select 
						a.task_id as task_id,
						count(1) as pay_counts
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
					group by a.task_id
				),
				without_dial_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_dialcall_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as dial_call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
						and a.call_type = '主叫'
					) aa 
					group by aa.task_id
				),
				without_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_call_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
					) aa 
					group by aa.task_id
				)
					select 
						a.task_id as task_id,
						count(distinct to_char(a.call_time::timestamp,'yyyy-mm-dd')) as communicate_days,
						count(1) as communicate_counts,
						count(distinct a.his_num) as contact_nums,
						count(distinct b.city) as location_counts,
						sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
						sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
						(count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1) as called_nums,
						(count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1) as dial_nums,
						sum(a.call_duration::numeric) as communicate_duration,
						sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end) as dial_duration,
						sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end) as called_duration,
						round(case when count(1) <> 0 then sum(a.call_duration::numeric)/count(1) else 0 end,2) as avg_duration,
						c.message_days as message_days,
						c.message_counts as message_counts,
						d.pay_counts as pay_counts,
						COALESCE(e.without_dialcall_days,0) as without_dialcall_days,
						COALESCE(f.without_call_days,0) as without_call_days,
					--	date_part('day',now()-(now() - INTERVAL '1 month')),
						round(COALESCE(e.without_dialcall_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '1 month'))::numeric,2) as without_dialcall_days_proportion,
						round(COALESCE(f.without_call_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '1 month'))::numeric,2) as without_call_days_proportion,
						'一月' as data_type
					from pro_mobile_call_info a left join dir_mobile_segment b
						on substr(a.his_num,1,7) = b.prefix left join message_info c
						on a.task_id = c.task_id left join pay_info d
						on a.task_id = d.task_id left join without_dial_call_day e
						on a.task_id = e.task_id left join without_call_day f
						on a.task_id = f.task_id
					where a.task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '1 month','yyyy-mm-dd')
					group by 
						a.task_id,
						c.message_days,
						c.message_counts,
						d.pay_counts,
						e.without_dialcall_days,
						f.without_call_days;


--三月

			INSERT INTO pro_mobile_report_vitality
			(
				task_id,communicate_days,communicate_counts,contact_nums,location_counts,dial_counts,
				called_counts,called_nums,dial_nums,communicate_duration,dial_duration,called_duration,
				avg_duration,message_days,message_counts,pay_counts,without_dial_call_days,without_call_days,
				without_dial_call_days_proportion,without_call_days_proportion,data_type
			)
				with message_info as 
				(
					select
						a.task_id as task_id,
						count(distinct to_char(a.send_time::timestamp,'yyyy-mm-dd')) as message_days,
						count(1) as message_counts
					from pro_mobile_sms_info a 
					where a.task_id = this_id
					and a.send_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by a.task_id
				),
				pay_info as 
				(
					select 
						a.task_id as task_id,
						count(1) as pay_counts
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by a.task_id
				),
				without_dial_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_dialcall_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as dial_call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
						and a.call_type = '主叫'
					) aa 
					group by aa.task_id
				),
				without_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_call_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					) aa 
					group by aa.task_id
				)
					select 
						a.task_id as task_id,
						count(distinct to_char(a.call_time::timestamp,'yyyy-mm-dd')) as communicate_days,
						count(1) as communicate_counts,
						count(distinct a.his_num) as contact_nums,
						count(distinct b.city) as location_counts,
						sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
						sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
						(count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1) as called_nums,
						(count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1) as dial_nums,
						sum(a.call_duration::numeric) as communicate_duration,
						sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end) as dial_duration,
						sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end) as called_duration,
						round(case when count(1) <> 0 then sum(a.call_duration::numeric)/count(1) else 0 end,2) as avg_duration,
						c.message_days as message_days,
						c.message_counts as message_counts,
						d.pay_counts as pay_counts,
						COALESCE(e.without_dialcall_days,0) as without_dialcall_days,
						COALESCE(f.without_call_days,0) as without_call_days,
					--	date_part('day',now()-(now() - INTERVAL '1 month')),
						round(COALESCE(e.without_dialcall_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '3 month'))::numeric,2) as without_dialcall_days_proportion,
						round(COALESCE(f.without_call_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '3 month'))::numeric,2) as without_call_days_proportion,
						'三月' as data_type
					from pro_mobile_call_info a left join dir_mobile_segment b
						on substr(a.his_num,1,7) = b.prefix left join message_info c
						on a.task_id = c.task_id left join pay_info d
						on a.task_id = d.task_id left join without_dial_call_day e
						on a.task_id = e.task_id left join without_call_day f
						on a.task_id = f.task_id
					where a.task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by 
						a.task_id,
						c.message_days,
						c.message_counts,
						d.pay_counts,
						e.without_dialcall_days,
						f.without_call_days;

--六月

			INSERT INTO pro_mobile_report_vitality
			(
				task_id,communicate_days,communicate_counts,contact_nums,location_counts,dial_counts,
				called_counts,called_nums,dial_nums,communicate_duration,dial_duration,called_duration,
				avg_duration,message_days,message_counts,pay_counts,without_dial_call_days,without_call_days,
				without_dial_call_days_proportion,without_call_days_proportion,data_type
			)
				with message_info as 
				(
					select
						a.task_id as task_id,
						count(distinct to_char(a.send_time::timestamp,'yyyy-mm-dd')) as message_days,
						count(1) as message_counts
					from pro_mobile_sms_info a 
					where a.task_id = this_id
					and a.send_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by a.task_id
				),
				pay_info as 
				(
					select 
						a.task_id as task_id,
						count(1) as pay_counts
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by a.task_id
				),
				without_dial_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_dialcall_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as dial_call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
						and a.call_type = '主叫'
					) aa 
					group by aa.task_id
				),
				without_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_call_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					) aa 
					group by aa.task_id
				)
					select 
						a.task_id as task_id,
						count(distinct to_char(a.call_time::timestamp,'yyyy-mm-dd')) as communicate_days,
						count(1) as communicate_counts,
						count(distinct a.his_num) as contact_nums,
						count(distinct b.city) as location_counts,
						sum(case when a.call_type = '主叫' then 1 else 0 end) as dial_counts,
						sum(case when a.call_type = '被叫' then 1 else 0 end) as called_counts,
						(count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1) as called_nums,
						(count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1) as dial_nums,
						sum(a.call_duration::numeric) as communicate_duration,
						sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end) as dial_duration,
						sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end) as called_duration,
						round(case when count(1) <> 0 then sum(a.call_duration::numeric)/count(1) else 0 end,2) as avg_duration,
						c.message_days as message_days,
						c.message_counts as message_counts,
						d.pay_counts as pay_counts,
						COALESCE(e.without_dialcall_days,0) as without_dialcall_days,
						COALESCE(f.without_call_days,0) as without_call_days,
					--	date_part('day',now()-(now() - INTERVAL '1 month')),
						round(COALESCE(e.without_dialcall_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '6 month'))::numeric,2) as without_dialcall_days_proportion,
						round(COALESCE(f.without_call_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '6 month'))::numeric,2) as without_call_days_proportion,
						'六月' as data_type
					from pro_mobile_call_info a left join dir_mobile_segment b
						on substr(a.his_num,1,7) = b.prefix left join message_info c
						on a.task_id = c.task_id left join pay_info d
						on a.task_id = d.task_id left join without_dial_call_day e
						on a.task_id = e.task_id left join without_call_day f
						on a.task_id = f.task_id
					where a.task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by 
						a.task_id,
						c.message_days,
						c.message_counts,
						d.pay_counts,
						e.without_dialcall_days,
						f.without_call_days;

--三月月均

			INSERT INTO pro_mobile_report_vitality
			(
				task_id,communicate_days,communicate_counts,contact_nums,location_counts,dial_counts,
				called_counts,called_nums,dial_nums,communicate_duration,dial_duration,called_duration,
				avg_duration,message_days,message_counts,pay_counts,without_dial_call_days,without_call_days,
				without_dial_call_days_proportion,without_call_days_proportion,data_type
			)
				with message_info as 
				(
					select
						a.task_id as task_id,
						count(distinct to_char(a.send_time::timestamp,'yyyy-mm-dd')) as message_days,
						count(1) as message_counts
					from pro_mobile_sms_info a 
					where a.task_id = this_id
					and a.send_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by a.task_id
				),
				pay_info as 
				(
					select 
						a.task_id as task_id,
						count(1) as pay_counts
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by a.task_id
				),
				without_dial_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_dialcall_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as dial_call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
						and a.call_type = '主叫'
					) aa 
					group by aa.task_id
				),
				without_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_call_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					) aa 
					group by aa.task_id
				)
					select 
						a.task_id as task_id,
						round(count(distinct to_char(a.call_time::timestamp,'yyyy-mm-dd'))::numeric/3,2) as communicate_days,
						round(count(1)::numeric/3,2) as communicate_counts,
						round(count(distinct a.his_num)::numeric/3,2) as contact_nums,
						round(count(distinct b.city)::numeric/3,2) as location_counts,
						round(sum(case when a.call_type = '主叫' then 1 else 0 end)::numeric/3,2) as dial_counts,
						round(sum(case when a.call_type = '被叫' then 1 else 0 end)::numeric/3,2) as called_counts,
						round((count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1)::numeric/3,2) as called_nums,
						round((count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1)::numeric/3,2) as dial_nums,
						round(sum(a.call_duration::numeric)::numeric/3,2) as communicate_duration,
						round(sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end)::numeric/3,2) as dial_duration,
						round(sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end)::numeric/3,2) as called_duration,
						round(case when count(1) <> 0 then sum(a.call_duration::numeric)/count(1) else 0 end,2) as avg_duration,
						round(c.message_days::numeric/3,2) as message_days,
						round(c.message_counts::numeric/3,2) as message_counts,
						round(d.pay_counts::numeric/3,2) as pay_counts,
						round(COALESCE(e.without_dialcall_days,0)::numeric/3,2) as without_dialcall_days,
						round(COALESCE(f.without_call_days,0)::numeric/3,2) as without_call_days,
					--	date_part('day',now()-(now() - INTERVAL '1 month')),
						round(round(COALESCE(e.without_dialcall_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '3 month'))::numeric,2)/3,2) as without_dialcall_days_proportion,
						round(round(COALESCE(f.without_call_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '3 month'))::numeric,2)/3,2) as without_call_days_proportion,
						'三月月均' as data_type
					from pro_mobile_call_info a left join dir_mobile_segment b
						on substr(a.his_num,1,7) = b.prefix left join message_info c
						on a.task_id = c.task_id left join pay_info d
						on a.task_id = d.task_id left join without_dial_call_day e
						on a.task_id = e.task_id left join without_call_day f
						on a.task_id = f.task_id
					where a.task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '3 month','yyyy-mm-dd')
					group by 
						a.task_id,
						c.message_days,
						c.message_counts,
						d.pay_counts,
						e.without_dialcall_days,
						f.without_call_days;

--六月月均

			INSERT INTO pro_mobile_report_vitality
			(
				task_id,communicate_days,communicate_counts,contact_nums,location_counts,dial_counts,
				called_counts,called_nums,dial_nums,communicate_duration,dial_duration,called_duration,
				avg_duration,message_days,message_counts,pay_counts,without_dial_call_days,without_call_days,
				without_dial_call_days_proportion,without_call_days_proportion,data_type
			)
				with message_info as 
				(
					select
						a.task_id as task_id,
						count(distinct to_char(a.send_time::timestamp,'yyyy-mm-dd')) as message_days,
						count(1) as message_counts
					from pro_mobile_sms_info a 
					where a.task_id = this_id
					and a.send_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by a.task_id
				),
				pay_info as 
				(
					select 
						a.task_id as task_id,
						count(1) as pay_counts
					from pro_mobile_pay_info a 
					where a.task_id = this_id
					and a.paytime > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by a.task_id
				),
				without_dial_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_dialcall_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as dial_call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
						and a.call_type = '主叫'
					) aa 
					group by aa.task_id
				),
				without_call_day as 
				(
					select 
						aa.task_id,
						count(1) as without_call_days
					from 
					(
						select 
							this_id as task_id,
							a.cal_date
						from 
							calendar_table a
						where a.cal_date >= to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
						and a.cal_date < to_char(now(),'yyyy-mm-dd')	
					EXCEPT
						select
							distinct
								a.task_id,
								to_char(a.call_time::timestamp,'yyyy-mm-dd') as call_days
						from pro_mobile_call_info a
						where a.task_id = this_id
						and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					) aa 
					group by aa.task_id
				)
					select 
						a.task_id as task_id,
						round(count(distinct to_char(a.call_time::timestamp,'yyyy-mm-dd'))::numeric/6,2) as communicate_days,
						round(count(1)::numeric/6,2) as communicate_counts,
						round(count(distinct a.his_num)::numeric/6,2) as contact_nums,
						round(count(distinct b.city)::numeric/6,2) as location_counts,
						round(sum(case when a.call_type = '主叫' then 1 else 0 end)::numeric/6,2) as dial_counts,
						round(sum(case when a.call_type = '被叫' then 1 else 0 end)::numeric/6,2) as called_counts,
						round((count(distinct case when a.call_type = '主叫' then a.his_num else '1' end) -1)::numeric/6,2) as called_nums,
						round((count(distinct case when a.call_type = '被叫' then a.his_num else '1' end) -1)::numeric/6,2) as dial_nums,
						round(sum(a.call_duration::numeric)::numeric/6,2) as communicate_duration,
						round(sum(case when a.call_type = '主叫' then a.call_duration::numeric else 0 end)::numeric/6,2) as dial_duration,
						round(sum(case when a.call_type = '被叫' then a.call_duration::numeric else 0 end)::numeric/6,2) as called_duration,
						round(case when count(1) <> 0 then sum(a.call_duration::numeric)/count(1) else 0 end,2) as avg_duration,
						round(c.message_days::numeric/6,2) as message_days,
						round(c.message_counts::numeric/6,2) as message_counts,
						round(d.pay_counts::numeric/6,2) as pay_counts,
						round(COALESCE(e.without_dialcall_days,0)::numeric/6,2) as without_dialcall_days,
						round(COALESCE(f.without_call_days,0)::numeric/6,2) as without_call_days,
					--	date_part('day',now()-(now() - INTERVAL '1 month')),
						round(round(COALESCE(e.without_dialcall_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '6 month'))::numeric,2)/6,2) as without_dialcall_days_proportion,
						round(round(COALESCE(f.without_call_days,0)::numeric/date_part('day',now()-(now() - INTERVAL '6 month'))::numeric,2)/6,2) as without_call_days_proportion,
					'六月月均' as data_type
					from pro_mobile_call_info a left join dir_mobile_segment b
						on substr(a.his_num,1,7) = b.prefix left join message_info c
						on a.task_id = c.task_id left join pay_info d
						on a.task_id = d.task_id left join without_dial_call_day e
						on a.task_id = e.task_id left join without_call_day f
						on a.task_id = f.task_id
					where a.task_id = this_id
					and a.call_time > to_char(now() - INTERVAL '6 month','yyyy-mm-dd')
					group by 
						a.task_id,
						c.message_days,
						c.message_counts,
						d.pay_counts,
						e.without_dialcall_days,
						f.without_call_days;

--没出错，在日志表中记录成功

			INSERT INTO pro_mobile_report_detail_log
				(task_id,createtime,step_name,table_name,error_detail,step_result)
			VALUES (this_id,now(),'活跃程度','pro_mobile_report_vitality',d_error_detail_13,d_step_result_13);

--异常处理

			EXCEPTION
				WHEN QUERY_CANCELED THEN 
--记录具体错误
					GET STACKED DIAGNOSTICS d_error_detail_13 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_13 = 'fail';
--错误情况插入日志表
				INSERT INTO pro_mobile_report_detail_log
					(task_id,createtime,step_name,table_name,error_detail,step_result)
				VALUES (this_id,now(),'活跃程度','pro_mobile_report_vitality',d_error_detail_13,d_step_result_13);

				WHEN OTHERS THEN 
					GET STACKED DIAGNOSTICS d_error_detail_13 = PG_EXCEPTION_CONTEXT;
--将状态改成失败
					d_step_result_13 = 'fail';
--错误情况插入日志表
					INSERT INTO pro_mobile_report_detail_log
						(task_id,createtime,step_name,table_name,error_detail,step_result)
					VALUES (this_id,now(),'活跃程度','pro_mobile_report_vitality',d_error_detail_13,d_step_result_13);

		END;


-------------------------------------------------------------------------------------------------

--更新task表状态

	UPDATE task_mobile a set a.report_status = d_report_result where a.taskid = this_id;
	
	RETURN d_report_result;

	EXCEPTION
		WHEN QUERY_CANCELED THEN 
			d_report_result = 'fail';
			UPDATE task_mobile a set a.report_status = d_report_result where a.taskid = this_id;
			RETURN d_report_result;
		WHEN OTHERS THEN 
			d_report_result = 'fail';
			UPDATE task_mobile a set a.report_status = d_report_result where a.taskid = this_id;
			RETURN d_report_result;
		
END;   
$BODY$
  LANGUAGE 'plpgsql' VOLATILE COST 100
;

;;--ALTER FUNCTION "public"."pro_mobile_report"("taskid" text) OWNER TO "lvyuxin";