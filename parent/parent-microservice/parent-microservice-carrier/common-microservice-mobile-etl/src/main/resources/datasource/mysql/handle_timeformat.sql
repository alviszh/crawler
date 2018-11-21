DROP FUNCTION IF EXISTS handle_timeformat;

CREATE FUNCTION handle_timeformat(province varchar(100),timestr varchar(100)) RETURNS varchar(100) CHARSET utf8
BEGIN

	DECLARE format_time varchar(100);
	DECLARE test_str varchar(100);
	DECLARE minites varchar(100);
	DECLARE seconds varchar(100);
	DECLARE hours varchar(100);
	DECLARE execute_result varchar(100);

	DECLARE EXIT HANDLER FOR SQLWARNING SET @info='ERROR'; 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION SET @info='ERROR';

		IF province = 'cmcc' THEN 

#43分10秒,6分钟17秒,00小时07分52秒,0时20分12秒

			IF timestr LIKE '%分%秒' THEN
 				
				set test_str = replace(replace(replace(replace(replace(timestr,'小时',':'),'时',':'),'分钟',':'),'分',':'),'秒','');
				
				IF length(test_str) - length(replace(test_str,':','')) = 2 THEN 
	
					set hours = func_get_split_string(test_str,':',1);
					set minites = func_get_split_string(test_str,':',2);			
					set seconds = func_get_split_string(test_str,':',3);
					set format_time = (hours*3600+minites*60+seconds);

					RETURN format_time;

				ELSEIF length(test_str) - length(replace(test_str,':','')) = 1 THEN 

					set minites = func_get_split_string(test_str,':',1);			
					set seconds = func_get_split_string(test_str,':',2);
					set format_time = (minites*60+seconds);	

					RETURN format_time;

				ELSE 

					RETURN '0';

				END IF;
			END IF;
	
#231秒		
			IF timestr LIKE '%秒' THEN 

					set test_str = replace(timestr,'秒','');
					set format_time = test_str;

					RETURN format_time;	
		
			END IF;

#00:06:09
			IF timestr LIKE '%:%:%' THEN 

					set hours = func_get_split_string(timestr,':',1);
					set minites = func_get_split_string(timestr,':',2);			
					set seconds = func_get_split_string(timestr,':',3);
					set format_time = (hours*3600+minites*60+seconds);

					RETURN format_time;

			END IF;

		END IF;

#################################cmcc(移动)..... 未完待续#################################


		IF province = 'unicom' THEN 

#19分21秒,1小时14分12秒,35秒

			set test_str = replace(replace(replace(timestr,'小时',':'),'分',':'),'秒','');

			IF length(test_str) - length(replace(test_str,':','')) = 2 THEN
				
					set hours = func_get_split_string(test_str,':',1);
					set minites = func_get_split_string(test_str,':',2);			
					set seconds = func_get_split_string(test_str,':',3);
					set format_time = (hours*3600+minites*60+seconds);

					RETURN format_time;

			ELSEIF length(test_str) - length(replace(test_str,':','')) = 1 THEN 

					set minites = func_get_split_string(test_str,':',1);			
					set seconds = func_get_split_string(test_str,':',2);
					set format_time = (minites*60+seconds);	

					RETURN format_time;
			
			ELSE 

					set format_time = test_str;

					RETURN format_time;

			END IF;
		END IF;

#################################unicom(联通)..... 未完待续#################################

		IF province = 'anhui' THEN

				set hours = func_get_split_string(timestr,':',1);
				set minites = func_get_split_string(timestr,':',2);			
				set seconds = func_get_split_string(timestr,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################安徽电信..... 未完待续#################################

		IF province = 'beijing' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################北京电信..... 未完待续#################################

		IF province = 'chongqing' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################重庆电信..... 未完待续#################################

		IF province = 'fujian' THEN

#0时1分41秒,0时0分51秒

				set test_str = replace(replace(replace(timestr,'时',':'),'分',':'),'秒','');

				set hours = func_get_split_string(test_str,':',1);
				set minites = func_get_split_string(test_str,':',2);			
				set seconds = func_get_split_string(test_str,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################福建电信..... 未完待续#################################

		IF province = 'gansu' THEN

				set hours = func_get_split_string(timestr,':',1);
				set minites = func_get_split_string(timestr,':',2);			
				set seconds = func_get_split_string(timestr,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################甘肃电信..... 未完待续#################################

		IF province = 'guangdong' THEN

				set hours = func_get_split_string(timestr,':',1);
				set minites = func_get_split_string(timestr,':',2);			
				set seconds = func_get_split_string(timestr,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################广东电信..... 未完待续#################################

		IF province = 'guangxi' THEN

				set hours = func_get_split_string(timestr,':',1);
				set minites = func_get_split_string(timestr,':',2);			
				set seconds = func_get_split_string(timestr,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################广西电信..... 未完待续#################################

		IF province = 'guizhou' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################贵州电信..... 未完待续#################################

		IF province = 'hainan' THEN

				set minites = func_get_split_string(timestr,':',1);			
				set seconds = func_get_split_string(timestr,':',2);
				set format_time = (minites*60+seconds);

				RETURN format_time;

		END IF;

#################################海南电信..... 未完待续#################################

		IF province = 'hebei' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################河北电信..... 未完待续#################################

		IF province = 'heilongjiang' THEN

				set hours = func_get_split_string(timestr,':',1);
				set minites = func_get_split_string(timestr,':',2);			
				set seconds = func_get_split_string(timestr,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################黑龙江电信..... 未完待续#################################

		IF province = 'henan' THEN

				set hours = func_get_split_string(timestr,':',1);
				set minites = func_get_split_string(timestr,':',2);			
				set seconds = func_get_split_string(timestr,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################河南电信..... 未完待续#################################

		IF province = 'hubei' THEN

			IF timestr LIKE '%:%:%' THEN 
	
				set hours = func_get_split_string(timestr,':',1);
				set minites = func_get_split_string(timestr,':',2);			
				set seconds = func_get_split_string(timestr,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

			ELSE

				set format_time = timestr;

				RETURN format_time;

			END IF;
		END IF;

#################################湖北电信..... 未完待续#################################

		IF province = 'hunan' THEN

				set test_str = replace(replace(replace(timestr,'时',':'),'分',':'),'秒','');

				IF length(test_str) - length(replace(test_str,':','')) = 2 THEN 
					
					set hours = func_get_split_string(test_str,':',1);
					set minites = func_get_split_string(test_str,':',2);			
					set seconds = func_get_split_string(test_str,':',3);
					set format_time = (hours*3600+minites*60+seconds);
				
					RETURN format_time;

				ELSEIF length(test_str) - length(replace(test_str,':','')) = 1 THEN

					set minites = func_get_split_string(test_str,':',1);			
					set seconds = func_get_split_string(test_str,':',2);
					set format_time = (minites*60+seconds);

					RETURN format_time;

				ELSE 

					RETURN test_str;

				END IF;
		END IF;

#################################湖南电信..... 未完待续#################################

		IF province = 'jiangsu' THEN

				set hours = func_get_split_string(timestr,':',1);
				set minites = func_get_split_string(timestr,':',2);			
				set seconds = func_get_split_string(timestr,':',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################江苏电信..... 未完待续#################################

		IF province = 'jiangxi' THEN

			IF timestr LIKE '%秒' THEN 

#3分钟1秒,55秒

				set test_str = replace(replace(replace(timestr,'小时',':'),'分钟',':'),'秒','');

				IF length(test_str) - length(replace(test_str,':','')) = 2 THEN
 
					set hours = func_get_split_string(test_str,':',1);
					set minites = func_get_split_string(test_str,':',2);			
					set seconds = func_get_split_string(test_str,':',3);
					set format_time = (hours*3600+minites*60+seconds);
				
					RETURN format_time;
					
				ELSEIF length(test_str) - length(replace(test_str,':','')) = 1 THEN

					set minites = func_get_split_string(test_str,':',1);			
					set seconds = func_get_split_string(test_str,':',2);
					set format_time = (minites*60+seconds);
				
					RETURN format_time;
	
				ELSE 
			
					RETURN test_str;

				END IF;

			ELSE

#3分钟

				set test_str = replace(timestr,'分钟','');
				set format_time = test_str;

				RETURN format_time;
			
			END IF;
		END IF;

#################################江西电信..... 未完待续#################################

		IF province = 'jilin' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################吉林电信..... 未完待续#################################

		IF province = 'liaoning' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################辽宁电信..... 未完待续#################################

		IF province = 'neimenggu' THEN

				set test_str = func_get_split_string(timestr,'''',1);
				set hours = func_get_split_string(timestr,'''',1);
				set minites = func_get_split_string(timestr,'''',2);			
				set seconds = func_get_split_string(timestr,'''',3);
				set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

		END IF;

#################################内蒙古电信..... 未完待续#################################

		IF province = 'ningxia' THEN

#1'45",34"

			IF timestr LIKE '"' THEN 

				set test_str = replace(timestr,'"','');

				IF length(test_str) - length(replace(test_str,'''','')) = 2 THEN 

					set hours = func_get_split_string(test_str,'''',1);
					set minites = func_get_split_string(test_str,'''',2);			
					set seconds = func_get_split_string(test_str,'''',3);
					set format_time = (hours*3600+minites*60+seconds);

					RETURN format_time;
		
				ELSEIF length(test_str) - length(replace(test_str,'''','')) = 1 THEN 

					set minites = func_get_split_string(test_str,'''',1);			
					set seconds = func_get_split_string(test_str,'''',2);
					set format_time = (minites*60+seconds);

					RETURN format_time;

				ELSE 

					RETURN test_str;

				END IF;

#0:8:28,0:2:9

			ELSE 

					set hours = func_get_split_string(timestr,':',1);
					set minites = func_get_split_string(timestr,':',2);			
					set seconds = func_get_split_string(timestr,':',3);
					set format_time = (hours*3600+minites*60+seconds);

				RETURN format_time;

			END IF;
		END IF;

#################################宁夏电信..... 未完待续#################################

		IF province = 'qinghai' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################青海电信..... 未完待续#################################

		IF province = 'shandong' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################山东电信..... 未完待续#################################

		IF province = 'shanghai' THEN

#0小时42分51秒

			set test_str = replace(replace(replace(timestr,'小时',':'),'分',':'),'秒','');
			set hours = func_get_split_string(test_str,':',1);
			set minites = func_get_split_string(test_str,':',2);			
			set seconds = func_get_split_string(test_str,':',3);
			set format_time = (hours*3600+minites*60+seconds);

			RETURN format_time;

		END IF;

#################################上海电信..... 未完待续#################################

		IF province = 'shanxi1' THEN

#00:00:14

			set hours = func_get_split_string(timestr,':',1);
			set minites = func_get_split_string(timestr,':',2);			
			set seconds = func_get_split_string(timestr,':',3);
			set format_time = (hours*3600+minites*60+seconds);

			RETURN format_time;

		END IF;

#################################山西电信..... 未完待续#################################

		IF province = 'shanxi3' THEN

#00:00:14

			set hours = func_get_split_string(timestr,':',1);
			set minites = func_get_split_string(timestr,':',2);			
			set seconds = func_get_split_string(timestr,':',3);
			set format_time = (hours*3600+minites*60+seconds);

			RETURN format_time;

		END IF;

#################################陕西电信..... 未完待续#################################

		IF province = 'sichuan' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################四川电信..... 未完待续#################################

		IF province = 'tianjin' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################天津电信..... 未完待续#################################

		IF province = 'xinjiang' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#################################新疆电信..... 未完待续#################################

		IF province = 'yunnan' THEN

#00:00:14

			set hours = func_get_split_string(timestr,':',1);
			set minites = func_get_split_string(timestr,':',2);			
			set seconds = func_get_split_string(timestr,':',3);
			set format_time = (hours*3600+minites*60+seconds);

			RETURN format_time;

		END IF;

#################################云南电信..... 未完待续#################################

		IF province = 'zhejiang' THEN

				set format_time = timestr;

				RETURN format_time;

		END IF;

#走到这儿，就是失败了。记录新的时间格式

			INSERT INTO pro_mobile_new_timeformat(createtime,province,timeformat) 
			VALUES (now(),province,timestr);

			RETURN '0';
		
END
;;