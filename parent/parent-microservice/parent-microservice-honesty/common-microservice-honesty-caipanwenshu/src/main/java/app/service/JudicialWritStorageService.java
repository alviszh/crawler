package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritList;
import com.microservice.dao.repository.crawler.honesty.judicialwrit.JudicialWritListRepository;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.honesty.judicialwrit" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.honesty.judicialwrit" })
public class JudicialWritStorageService {
	@Autowired
	private JudicialWritListRepository judicialWritListRepository;

	/***
	 *  去重 （缓存）
	 * @param writid
	 * @return
	 */
	@Cacheable(value = "cpws", key = "'findByMainPostUniq' + #writid ", unless = "#result==null")
	public String findByWritid(String writid) {
		 JudicialWritList findByWritid = judicialWritListRepository.findByWritid(writid);
		 return findByWritid.getWritid();
	}
}
