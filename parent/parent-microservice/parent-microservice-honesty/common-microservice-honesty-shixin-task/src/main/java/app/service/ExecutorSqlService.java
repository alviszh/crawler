package app.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.honesty.shixin.ShiXinBean;
import com.microservice.dao.repository.crawler.honesty.shixin.ShiXinBeanRepository;

import app.bean.HonestyShiXinBean;
import app.bean.Honestybean;
import app.commontracerlog.TracerLog;

/**
 * 
 * 项目名称：common-microservice-honesty-shixin-task 类名称：ExecutorSqlService 类描述：
 * 创建人：hyx 创建时间：2018年8月9日 下午1:48:54
 * 
 * @version
 */
@Component
public class ExecutorSqlService {

	@Autowired
	private ShiXinBeanRepository shiXinBeanRepository;
	
	@Autowired
	private TracerLog tracerLog;


	@Async("ExecutorForSearch") // 配置类中的方法名
	public Future<HonestyShiXinBean> findShiXinBeanForPage(Honestybean honestybean) {

		HonestyShiXinBean honestyShiXinBean = null;

		Integer pageNum = 0;

		Integer pageSize = 100;

		if (honestybean.getPagenum() != null) {
			pageNum = honestybean.getPagenum();
		}

		if (honestybean.getPagesize() != null) {
			pageSize = honestybean.getPagesize();
		}

		if (!(honestybean.getpCardNum() == null || honestybean.getpCardNum().isEmpty())) {

			if (honestybean.getpCardNum().length() == 18) {
				StringBuilder sb = new StringBuilder(honestybean.getpCardNum());
				sb.replace(10, 14, "****");

				honestybean.setpCardNum(sb.toString());
			}

			if (honestybean.getpCardNum().length() == 15) {
				StringBuilder sb = new StringBuilder(honestybean.getpCardNum());
				sb.replace(7, 11, "****");

				honestybean.setpCardNum(sb.toString());
			}

		}

		Sort sort = new Sort(Sort.Direction.DESC, "id");

		Pageable pageable = new PageRequest(pageNum, pageSize, sort);

		Boolean bool_isname = true;

		Boolean bool_taskid = true;

		Boolean bool_cardnum = true;

		if (honestybean.getTaskid() == null || honestybean.getTaskid().isEmpty()) {
			bool_taskid = false;
		}

		if (honestybean.getpName() == null || honestybean.getpName().isEmpty()) {
			bool_isname = false;
		}

		if (honestybean.getpCardNum() == null || honestybean.getpCardNum().isEmpty()) {
			bool_cardnum = false;
		}
		
		

		if (!bool_taskid) {

			if ((!bool_isname) && (!bool_cardnum)) {
				
				tracerLog.System("未传入有效信息,请检查参数信息", "404");

				honestyShiXinBean = new HonestyShiXinBean();
				honestyShiXinBean.setTaskid(honestybean.getTaskid());
				honestyShiXinBean.setpName(honestybean.getpName());
				honestyShiXinBean.setpCardNum(honestybean.getpCardNum());
				
				
				honestyShiXinBean.setMessage("未传入有效信息,请检查参数信息");
				honestyShiXinBean.setStatus("404");
				return new AsyncResult<HonestyShiXinBean>(honestyShiXinBean);
			}

			if ((!bool_isname) && bool_cardnum) {
				tracerLog.System("根据cardnum查询", honestybean.getpCardNum());
				honestyShiXinBean = new HonestyShiXinBean();
				honestyShiXinBean.setTaskid(honestybean.getTaskid());
				honestyShiXinBean.setpName(honestybean.getpName());
				honestyShiXinBean.setpCardNum(honestybean.getpCardNum());
				Page<ShiXinBean> page = shiXinBeanRepository.findByCardNum(honestybean.getpCardNum(), pageable);
				List<ShiXinBean> list = new ArrayList<>();
				Iterator<ShiXinBean> it = page.iterator();
				while (it.hasNext()) {

					list.add(it.next());
				}

				honestyShiXinBean.setList(list);
				honestyShiXinBean.setStatus("200");
				honestyShiXinBean.setMessage("sucess");
			}

			if (bool_isname && (!bool_cardnum)) {

				tracerLog.System("根据name查询", honestybean.getpCardNum());

				Page<ShiXinBean> page = shiXinBeanRepository.findByIname(honestybean.getpName(), pageable);
				List<ShiXinBean> list = new ArrayList<>();
				Iterator<ShiXinBean> it = page.iterator();
				while (it.hasNext()) {

					list.add(it.next());
				}
				honestyShiXinBean = new HonestyShiXinBean();
				honestyShiXinBean.setTaskid(honestybean.getTaskid());
				honestyShiXinBean.setpName(honestybean.getpName());
				honestyShiXinBean.setpCardNum(honestybean.getpCardNum());
				honestyShiXinBean.setList(list);
				honestyShiXinBean.setStatus("200");
				honestyShiXinBean.setMessage("sucess");
			}

			if ((bool_isname) && (bool_cardnum)) {
				
				tracerLog.System("根据name和cardnum查询", honestybean.getpCardNum());

				Page<ShiXinBean> page = shiXinBeanRepository.findByInameAndCardNum(honestybean.getpName(),
						honestybean.getpCardNum(), pageable);
				List<ShiXinBean> list = new ArrayList<>();
				Iterator<ShiXinBean> it = page.iterator();
				while (it.hasNext()) {

					list.add(it.next());
				}
				honestyShiXinBean = new HonestyShiXinBean();
				honestyShiXinBean.setTaskid(honestybean.getTaskid());
				honestyShiXinBean.setpName(honestybean.getpName());
				honestyShiXinBean.setpCardNum(honestybean.getpCardNum());
				honestyShiXinBean.setList(list);
				honestyShiXinBean.setStatus("200");
				honestyShiXinBean.setMessage("sucess");
			}

		} else {
			if ((!bool_isname) && (!bool_cardnum)) {
				tracerLog.System("根据taskid查询", honestybean.toString());

				Page<ShiXinBean> page = shiXinBeanRepository.findByTaskid(honestybean.getTaskid(), pageable);
				List<ShiXinBean> list = new ArrayList<>();
				Iterator<ShiXinBean> it = page.iterator();
				while (it.hasNext()) {

					list.add(it.next());
				}
				honestyShiXinBean = new HonestyShiXinBean();
				honestyShiXinBean.setTaskid(honestybean.getTaskid());
				honestyShiXinBean.setpName(honestybean.getpName());
				honestyShiXinBean.setpCardNum(honestybean.getpCardNum());
				honestyShiXinBean.setList(list);
				honestyShiXinBean.setStatus("200");
				honestyShiXinBean.setMessage("sucess");
			}

			if ((!bool_isname) && bool_cardnum) {
				tracerLog.System("根据taskid和cardnum查询", honestybean.toString());

				Page<ShiXinBean> page = shiXinBeanRepository.findByTaskidAndCardNum(honestybean.getTaskid(),
						honestybean.getpCardNum(), pageable);
				List<ShiXinBean> list = new ArrayList<>();
				Iterator<ShiXinBean> it = page.iterator();
				while (it.hasNext()) {

					list.add(it.next());
				}
				honestyShiXinBean = new HonestyShiXinBean();
				honestyShiXinBean.setTaskid(honestybean.getTaskid());
				honestyShiXinBean.setpName(honestybean.getpName());
				honestyShiXinBean.setpCardNum(honestybean.getpCardNum());
				honestyShiXinBean.setList(list);
				honestyShiXinBean.setStatus("200");
				honestyShiXinBean.setMessage("sucess");
			}

			if (bool_isname && (!bool_cardnum)) {
				tracerLog.System("根据taskid,name查询", honestybean.toString());

				Page<ShiXinBean> page = shiXinBeanRepository.findByInameAndTaskid(honestybean.getpName(),
						honestybean.getTaskid(), pageable);
				List<ShiXinBean> list = new ArrayList<>();
				Iterator<ShiXinBean> it = page.iterator();
				while (it.hasNext()) {

					list.add(it.next());
				}
				honestyShiXinBean = new HonestyShiXinBean();
				honestyShiXinBean.setTaskid(honestybean.getTaskid());
				honestyShiXinBean.setpName(honestybean.getpName());
				honestyShiXinBean.setpCardNum(honestybean.getpCardNum());
				honestyShiXinBean.setList(list);
				honestyShiXinBean.setStatus("200");
				honestyShiXinBean.setMessage("sucess");
			}

			if ((bool_isname) && (bool_cardnum)) {
				tracerLog.System("根据taskid,name和cardnum查询", honestybean.toString());

				Page<ShiXinBean> page = shiXinBeanRepository.findByTaskidAndInameAndCardNum(honestybean.getTaskid(),
						honestybean.getpName(), honestybean.getpCardNum(), pageable);
				List<ShiXinBean> list = new ArrayList<>();
				Iterator<ShiXinBean> it = page.iterator();
				while (it.hasNext()) {

					list.add(it.next());
				}
				honestyShiXinBean = new HonestyShiXinBean();
				honestyShiXinBean.setTaskid(honestybean.getTaskid());
				honestyShiXinBean.setpName(honestybean.getpName());
				honestyShiXinBean.setpCardNum(honestybean.getpCardNum());
				honestyShiXinBean.setList(list);
				honestyShiXinBean.setStatus("200");
				honestyShiXinBean.setMessage("sucess");
			}

		}

		System.out.println("honestyShiXinBean :" + honestyShiXinBean.toString());

		return new AsyncResult<HonestyShiXinBean>(honestyShiXinBean);
	}
}
