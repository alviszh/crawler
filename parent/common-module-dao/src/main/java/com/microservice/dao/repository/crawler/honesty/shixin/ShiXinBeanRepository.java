package com.microservice.dao.repository.crawler.honesty.shixin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.honesty.shixin.ShiXinBean;

public interface ShiXinBeanRepository extends JpaRepository<ShiXinBean, Long> {

	List<ShiXinBean> findByShixinid(long shixinid);

	ShiXinBean findTop1ByOrderById();

	
	Page<ShiXinBean> findByTaskid(String taskid,Pageable pageable);
	
	Page<ShiXinBean> findByIname(String inname,Pageable pageable);

	Page<ShiXinBean> findByInameAndTaskid(String inname, String taskid,Pageable pageable);

	Page<ShiXinBean> findByCardNum(String cardnum,Pageable pageable);

	Page<ShiXinBean>  findByTaskidAndCardNum(String taskid, String cardnum,Pageable pageable);
	
	Page<ShiXinBean>  findByInameAndCardNum(String inname, String cardnum,Pageable pageable);

	Page<ShiXinBean> findByTaskidAndInameAndCardNum(String taskid, String inname, String cardnum,Pageable pageable);

	
	List<ShiXinBean> findByTaskid(String taskid);

	List<ShiXinBean> findByIname(String inname);

	List<ShiXinBean> findByInameAndTaskid(String inname, String taskid);

	List<ShiXinBean> findByCardNum(String cardnum);

	List<ShiXinBean> findByTaskidAndCardNum(String taskid, String cardnum);

	List<ShiXinBean> findByInameAndCardNum(String inname, String cardnum);

	List<ShiXinBean> findByTaskidAndInameAndCardNum(String taskid, String inname, String cardnum);
}
