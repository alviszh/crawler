DROP FUNCTION IF EXISTS unicom_mobile_etl;

CREATE FUNCTION unicom_mobile_etl(mission_id varchar(50)) RETURNS text CHARSET utf8
BEGIN
	#Routine body goes here...
	DECLARE	this_id text;
	DECLARE	p_etl_status text DEFAULT 'success';
	DECLARE	p_etl_error_detail text;
	DECLARE	p_etl_error_content text;

	DECLARE EXIT HANDLER FOR SQLWARNING SET @info='ERROR'; 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION SET @info='ERROR';

	set this_id = taskid;

	delete from pro_mobile_bill_info where task_id = this_id;
	delete from pro_mobile_call_info where task_id = this_id;
	delete from pro_mobile_pay_info where task_id = this_id;
	delete from pro_mobile_service_info where task_id = this_id;
	delete from pro_mobile_sms_info where task_id = this_id;
	delete from pro_mobile_user_info where task_id = this_id;

#用户基本信息	

		INSERT INTO pro_mobile_user_info
		(
			resource,task_id,createtime,basic_user_name,basic_id_num,carrier,id_num,user_name,
			city,province,cus_level,nick_name,net_in_date,net_in_duration,package_name,
			cus_status,phone_num,cur_balance,points
		)
			select
				'unicom_userinfo:'|| a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				c.name as basic_user_name,
				c.idnum as basic_id_num,
				'中国联通' as carrier,
				a.certnum as id_num,
				a.cust_name as user_name,
				b.city as city,
				b.province as province,
				d.item_name as cus_level,
				a.nick_name as nick_name,
				date_format(substr(a.opendate,1,8),'%y-%m-%d') as net_in_date,
				DATEDIFF(now(),substr(a.opendate,1,8)) as net_in_duration,
				a.package_name as package_name,
				e.item_name as cus_status,
				a.usernumber as phone_num,
				f.balance as cur_balance,
				case when g.totalintegral is null then g.totalscore else g.totalintegral end as points
			from unicom_userinfo a left join task_mobile b
				on a.taskid = b.taskid left join basic_user c
				on b.basic_user_id = c.id left join userinfo_cuslevel_item_code d
				on a.custlvl = d.source_name left join userinfo_cusstatus_item_code e
				on a.subscrbstat = e.source_name left join unicom_balance f
				on a.taskid = f.taskid left join unicom_integrategralresult g
				on a.taskid = g.taskid
			where a.taskid = this_id;

#缴费信息

		INSERT INTO pro_mobile_pay_info
		(
			resource,task_id,createtime,payfee,paytime,paymonth,payway
		)
			select 
				'unicom_paymsgstatus:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				a.payfee as payfee,
				date_format(a.paydate,'%y-%m-%d') as paytime,
				date_format(a.paydate,'%y-%m') as paymonth,
				b.item_name as payway
			from unicom_paymsgstatus a left join payinfo_paytype_item_code b
				on case when a.payment is null then a.paychannel = b.source_name
								else a.payment = b.source_name end 
			where a.taskid = this_id;

#短信信息

		INSERT INTO pro_mobile_sms_info
		(
			resource,task_id,createtime,host_num,other_num,fee,sms_type,sms_way,send_time
		)
			select 
				'unicom_noteresult:' || a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as host_num,
				a.othernum as other_num,
				a.fee as fee,
				c.item_name as sms_type,
				d.item_name as sms_way,
				concat(substr(a.smsdate,1,10),' ',smstime) as send_time
			from unicom_noteresult a left join task_mobile b
				on a.taskid = b.taskid left join smsinfo_smstype_item_code c
				on a.businesstype = c.source_name left join smsinfo_smsway_item_code d
				on a.smstype = d.source_name
			where a.taskid = this_id;

#通话记录信息

		INSERT INTO pro_mobile_call_info
		(
			resource,task_id,createtime,phone_num,his_num,fee,call_location,call_time,call_type,charge_type,
			call_duration,his_location
		)
			select 
				'unicom_callresult:'||a.id as resource,
				a.taskid as task_id,
				a.createtime as createtime,
				b.phonenum as phone_num,
				a.othernum as his_num,
				a.totalfee as fee,
				a.homearea_name as call_location,
				concat(a.calldate,' ',a.calltime) as call_time,
				c.item_name as call_type,
				d.item_name as charge_type,
				handle_timeformat('unicom',calllonghour) as call_duration,
				a.calledhome as his_location
			from unicom_callresult a left join task_mobile b
				on a.taskid = b.taskid left join callinfo_calltype_item_code c
				on a.calltype_name = c.source_name left join callinfo_chargetype_item_code d
				on a.romatype_name = d.source_name
			where a.taskid = this_id;

#月账单信息

		INSERT INTO pro_mobile_bill_info
		(
			task_id,sum_fee,basic_fee,message_fee,normal_call_fee,roam_call_fee,flow_fee,
			function_fee,other_fee,bill_month
		)
			select 
				a.taskid as task_id,
				sum(replace(a.value,' ','')) as sum_fee,
				sum(case when b.item_name = '基本套餐费' then replace(a.value,' ','') else 0 end) as basic_fee,
				sum(case when b.item_name = '短信彩信费' then replace(a.value,' ','') else 0 end) as message_fee,
				sum(case when b.item_name = '国内通话费' then replace(a.value,' ','') else 0 end) as normal_call_fee,
				sum(case when b.item_name = '异地通话费' then replace(a.value,' ','') else 0 end) as roam_call_fee,
				sum(case when b.item_name = '互联网流量费' then replace(a.value,' ','') else 0 end) as flow_fee,
				sum(case when b.item_name = '业务功能费' then replace(a.value,' ','') else 0 end) as function_fee,
				sum(case when b.item_name = '其他费用' then replace(a.value,' ','') else 0 end) as other_fee,
#				substr(a.month,1,4) ||'-'|| substr(a.month,5,2) as bill_month
#				a.yearmonth as bill_month
#				replace(replace(querymonth,'年','-'),'月','') as bill_month
				substr(a.month,1,7) as bill_month
			from unicom_historyresult a inner join billinfo_feetype_item_code b
				on a.name = source_name
			where a.taskid = this_id
			and a.name is not null 
			and a.unicomdetaillist_id is not null
			group by 
				substr(a.month,1,7),
				a.taskid
			UNION 
			select 
				a.taskid as task_id,
				sum(a.fee) as sum_fee,
				sum(case when b.item_name = '基本套餐费' then a.fee else 0 end) as basic_fee,
				sum(case when b.item_name = '短信彩信费' then a.fee else 0 end) as message_fee,
				sum(case when b.item_name = '国内通话费' then a.fee else 0 end) as normal_call_fee,
				sum(case when b.item_name = '异地通话费' then a.fee else 0 end) as roam_call_fee,
				sum(case when b.item_name = '互联网流量费' then a.fee else 0 end) as flow_fee,
				sum(case when b.item_name = '业务功能费' then a.fee else 0 end) as function_fee,
				sum(case when b.item_name = '其他费用' then a.fee else 0 end) as other_fee,
#				substr(a.month,1,4) ||'-'|| substr(a.month,5,2) as bill_month
#				a.yearmonth as bill_month
#				replace(replace(querymonth,'年','-'),'月','') as bill_month
				substr(a.month,1,7) as bill_month
			from unicom_historyresult a inner join billinfo_feetype_item_code b
				on a.integrateitem = source_name
			where a.taskid = this_id
			and a.name is null 
			and a.parentitemcode <> '-1'
			group by 
				substr(a.month,1,7),
				a.taskid;

		RETURN p_etl_status;

END
;;