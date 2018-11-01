CREATE OR REPLACE FUNCTION "public"."handle_timeformat"("province" text, "timestr" text)
  RETURNS "pg_catalog"."text" AS $BODY$   
BEGIN  
	DECLARE format_time text;
	DECLARE test_str text;
	DECLARE minites text;
	DECLARE seconds text;
	DECLARE hours text;
	DECLARE execute_result text;

	BEGIN

		IF province = 'cmcc' THEN 

--43分10秒,6分钟17秒,00小时07分52秒,0时20分12秒

			IF timestr LIKE '%分%秒' THEN
 				
				test_str = replace(replace(replace(replace(replace(timestr,'小时',':'),'时',':'),'分钟',':'),'分',':'),'秒','');

				IF length(test_str) - length(replace(test_str,':','')) = 2 THEN 

					hours = split_part(test_str,':',1);
					minites = split_part(test_str,':',2);			
					seconds = split_part(test_str,':',3);
					format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

					RETURN format_time;
				
				ELSEIF length(test_str) - length(replace(test_str,':','')) = 1 THEN 

					minites = split_part(test_str,':',1);			
					seconds = split_part(test_str,':',2);
					format_time = (minites::numeric*60+seconds::numeric)::text;	

					RETURN format_time;

				ELSE 

					RETURN '0';

				END IF;				
			END IF;	

--231秒

			IF timestr LIKE '%秒' THEN 

					test_str = replace(timestr,'秒','');
					format_time = test_str;

					RETURN format_time;

			END IF;

--00:06:09

			IF timestr LIKE '%:%:%' THEN 

					hours = split_part(timestr,':',1);
					minites = split_part(timestr,':',2);			
					seconds = split_part(timestr,':',3);
					format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

					RETURN format_time;

			END IF;
		
		END IF;

----------------------------------cmcc(移动)..... 未完待续------------------------------------

		IF province = 'unicom' THEN 

--19分21秒,1小时14分12秒,35秒

			test_str = replace(replace(replace(timestr,'小时',':'),'分',':'),'秒','');

			IF length(test_str) - length(replace(test_str,':','')) = 2 THEN
				
					hours = split_part(test_str,':',1);
					minites = split_part(test_str,':',2);			
					seconds = split_part(test_str,':',3);
					format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

					RETURN format_time;

			ELSEIF length(test_str) - length(replace(test_str,':','')) = 1 THEN 

					minites = split_part(test_str,':',1);			
					seconds = split_part(test_str,':',2);
					format_time = (minites::numeric*60+seconds::numeric)::text;	

					RETURN format_time;
			
			ELSE 

					format_time = test_str;

					RETURN format_time;

			END IF;
		END IF;

-------------------------------unicom(联通)..... 未完待续------------------------------------

		IF province = 'anhui' THEN

				hours = split_part(timestr,':',1);
				minites = split_part(timestr,':',2);			
				seconds = split_part(timestr,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------安徽电信..... 未完待续------------------------------------

		IF province = 'beijing' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------北京电信..... 未完待续------------------------------------

		IF province = 'chongqing' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------重庆电信..... 未完待续------------------------------------

		IF province = 'fujian' THEN

--0时1分41秒,0时0分51秒

				test_str = replace(replace(replace(timestr,'时',':'),'分',':'),'秒','');

				hours = split_part(test_str,':',1);
				minites = split_part(test_str,':',2);			
				seconds = split_part(test_str,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------福建电信..... 未完待续------------------------------------

		IF province = 'gansu' THEN

				hours = split_part(timestr,':',1);
				minites = split_part(timestr,':',2);			
				seconds = split_part(timestr,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------甘肃电信..... 未完待续------------------------------------

		IF province = 'guangdong' THEN

				hours = split_part(timestr,':',1);
				minites = split_part(timestr,':',2);			
				seconds = split_part(timestr,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------广东电信..... 未完待续------------------------------------

		IF province = 'guangxi' THEN

				hours = split_part(timestr,':',1);
				minites = split_part(timestr,':',2);			
				seconds = split_part(timestr,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------广西电信..... 未完待续------------------------------------

		IF province = 'guizhou' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------贵州电信..... 未完待续------------------------------------

		IF province = 'hainan' THEN

				minites = split_part(timestr,':',1);			
				seconds = split_part(timestr,':',2);
				format_time = (minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------海南电信..... 未完待续------------------------------------

		IF province = 'hebei' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------河北电信..... 未完待续------------------------------------

		IF province = 'heilongjiang' THEN

				hours = split_part(timestr,':',1);
				minites = split_part(timestr,':',2);			
				seconds = split_part(timestr,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------黑龙江电信..... 未完待续------------------------------------

		IF province = 'henan' THEN

				hours = split_part(timestr,':',1);
				minites = split_part(timestr,':',2);			
				seconds = split_part(timestr,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------河南电信..... 未完待续------------------------------------

		IF province = 'hubei' THEN
			IF timestr LIKE '%:%:%' THEN 
	
				hours = split_part(timestr,':',1);
				minites = split_part(timestr,':',2);			
				seconds = split_part(timestr,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

			ELSE

				format_time = timestr;

				RETURN format_time;

			END IF;
		END IF;

-------------------------------湖北电信..... 未完待续------------------------------------

		IF province = 'hunan' THEN

				test_str = replace(replace(replace(timestr,'时',':'),'分',':'),'秒','');

				IF length(test_str) - length(replace(test_str,':','')) = 2 THEN 
					
					hours = split_part(test_str,':',1);
					minites = split_part(test_str,':',2);			
					seconds = split_part(test_str,':',3);
					format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;
				
					RETURN format_time;

				ELSEIF length(test_str) - length(replace(test_str,':','')) = 1 THEN

					minites = split_part(test_str,':',1);			
					seconds = split_part(test_str,':',2);
					format_time = (minites::numeric*60+seconds::numeric)::text;

					RETURN format_time;

				ELSE 

					RETURN test_str;

				END IF;
		END IF;

-------------------------------湖南电信..... 未完待续------------------------------------

		IF province = 'jiangsu' THEN

				hours = split_part(timestr,':',1);
				minites = split_part(timestr,':',2);			
				seconds = split_part(timestr,':',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------江苏电信..... 未完待续------------------------------------

		IF province = 'jiangxi' THEN

			IF timestr LIKE '%秒' THEN 

--3分钟1秒,55秒

				test_str = replace(replace(replace(timestr,'小时',':'),'分钟',':'),'秒','');

				IF length(test_str) - length(replace(test_str,':','')) = 2 THEN
 
					hours = split_part(test_str,':',1);
					minites = split_part(test_str,':',2);			
					seconds = split_part(test_str,':',3);
					format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;
				
					RETURN format_time;
					
				ELSEIF length(test_str) - length(replace(test_str,':','')) = 1 THEN

					minites = split_part(test_str,':',1);			
					seconds = split_part(test_str,':',2);
					format_time = (minites::numeric*60+seconds::numeric)::text;
				
					RETURN format_time;
	
				ELSE 
			
					RETURN test_str;

				END IF;

			ELSE

--3分钟

				test_str = replace(timestr,'分钟','');
				format_time = test_str;

				RETURN format_time;
			
			END IF;
		END IF;

-------------------------------江西电信..... 未完待续------------------------------------

		IF province = 'jilin' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------吉林电信..... 未完待续------------------------------------

		IF province = 'liaoning' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------辽宁电信..... 未完待续------------------------------------

		IF province = 'neimenggu' THEN

--				test_str = split_part(timestr,'''',1);
				hours = split_part(timestr,'''',1);
				minites = split_part(timestr,'''',2);			
				seconds = split_part(timestr,'''',3);
				format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

		END IF;

-------------------------------内蒙古电信..... 未完待续------------------------------------

		IF province = 'ningxia' THEN

--1'45",34"

			IF timestr LIKE '%"' THEN 

				test_str = replace(timestr,'"','');

				IF length(test_str) - length(replace(test_str,'''','')) = 2 THEN 

					hours = split_part(test_str,'''',1);
					minites = split_part(test_str,'''',2);			
					seconds = split_part(test_str,'''',3);
					format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

					RETURN format_time;
		
				ELSEIF length(test_str) - length(replace(test_str,'''','')) = 1 THEN 

					minites = split_part(test_str,'''',1);			
					seconds = split_part(test_str,'''',2);
					format_time = (minites::numeric*60+seconds::numeric)::text;

					RETURN format_time;

				ELSE 

					RETURN test_str;

				END IF;

--0:8:28,0:2:9

			ELSE 

					hours = split_part(timestr,':',1);
					minites = split_part(timestr,':',2);			
					seconds = split_part(timestr,':',3);
					format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

				RETURN format_time;

			END IF;
		END IF;

-------------------------------宁夏电信..... 未完待续------------------------------------

		IF province = 'qinghai' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------青海电信..... 未完待续------------------------------------

		IF province = 'shandong' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------山东电信..... 未完待续------------------------------------

		IF province = 'shanghai' THEN

--0小时42分51秒

			test_str = replace(replace(replace(timestr,'小时',':'),'分',':'),'秒','');
			hours = split_part(test_str,':',1);
			minites = split_part(test_str,':',2);			
			seconds = split_part(test_str,':',3);
			format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

			RETURN format_time;

		END IF;

-------------------------------上海电信..... 未完待续------------------------------------

		IF province = 'shanxi1' THEN

--00:00:14

			hours = split_part(timestr,':',1);
			minites = split_part(timestr,':',2);			
			seconds = split_part(timestr,':',3);
			format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

			RETURN format_time;

		END IF;

-------------------------------山西电信..... 未完待续------------------------------------

		IF province = 'shanxi3' THEN

--00:00:14

			hours = split_part(timestr,':',1);
			minites = split_part(timestr,':',2);			
			seconds = split_part(timestr,':',3);
			format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

			RETURN format_time;

		END IF;

-------------------------------陕西电信..... 未完待续------------------------------------

		IF province = 'sichuan' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------四川电信..... 未完待续------------------------------------

		IF province = 'tianjin' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------天津电信..... 未完待续------------------------------------

		IF province = 'xinjiang' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

-------------------------------新疆电信..... 未完待续------------------------------------

		IF province = 'yunnan' THEN

--00:00:14

			hours = split_part(timestr,':',1);
			minites = split_part(timestr,':',2);			
			seconds = split_part(timestr,':',3);
			format_time = (hours::numeric*3600+minites::numeric*60+seconds::numeric)::text;

			RETURN format_time;

		END IF;

-------------------------------云南电信..... 未完待续------------------------------------

		IF province = 'zhejiang' THEN

				format_time = timestr;

				RETURN format_time;

		END IF;

--走到这儿，就是失败了。记录新的时间格式

			INSERT INTO pro_mobile_new_timeformat(createtime,province,timeformat) 
			VALUES (now(),province,timestr);

			RETURN '0';

-------------------------------浙江电信..... 未完待续------------------------------------

	EXCEPTION
		WHEN QUERY_CANCELED THEN 

--将新时间格式插入记录表
			INSERT INTO pro_mobile_new_timeformat(createtime,province,timeformat) 
			VALUES (now(),province,timestr);

			RETURN '0';

		WHEN OTHERS THEN 

--将新时间格式插入记录表
			INSERT INTO pro_mobile_new_timeformat(createtime,province,timeformat) 
			VALUES (now(),province,timestr);

			RETURN '0';

	END;
END;   
$BODY$
  LANGUAGE 'plpgsql' VOLATILE COST 100
;

;;--ALTER FUNCTION "public"."handle_timeformat"("province" text, "timestr" text) OWNER TO "TXDB";