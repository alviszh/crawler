package com.microservice.dao.repository.crawler.bank.bocom.creditcard;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardUser;


/**   
*    
* 项目名称：common-module-dao   
* 类名称：BocomCreditcardUserRepository   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月22日 下午5:02:25   
* @version        
*/
public interface BocomCreditcardUserRepository extends JpaRepository<BocomCreditcardUser, Long>{

}
