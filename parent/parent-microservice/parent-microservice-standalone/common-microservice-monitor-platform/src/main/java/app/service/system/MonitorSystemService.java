/**
 * 
 */
package app.service.system;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.microservice.persistence.DynamicSpecifications;

import app.entity.system.MonitorEurekaServerInfo;
import app.repository.system.MonitorEurekaServerInfoRepository;


/**
 * @author sln
 * @Description: 系统层面—数据库相关服务
 */
@Component
public class MonitorSystemService {
	@Autowired
	private MonitorEurekaServerInfoRepository eurekaServerInfoRepository;
	/**
	 * 根据条件分页查询任务列表
	 * @param searchParams
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	//如下注解是后来加上的，为了避免黄线
	@SuppressWarnings({ "deprecation", "serial" })   
	public Page<MonitorEurekaServerInfo> getMobileTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize){
		Sort sort = new Sort(Sort.Direction.ASC, "appname");
		Pageable page = new PageRequest(currentPage, pageSize, sort);
		
		String appname = "";
		String developer = "";
		
		if (searchParams.get("appname") != null)
			appname = (String) searchParams.get("appname");
		if ( searchParams.get("developer") != null)
			developer = (String) searchParams.get("developer");

		final String finalAppname = appname;
		final String finalDeveloper = developer;
		return eurekaServerInfoRepository.findAll(new Specification<MonitorEurekaServerInfo>() {

			public Predicate toPredicate(Root<MonitorEurekaServerInfo> root,
										 CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> appnamePath = root.get("appname");
				Path<String> developerPath = root.get("developer");

				//path转化
				List<Predicate> orPredicates = Lists.newArrayList();

				if (!finalAppname.equals("")) {
					Predicate p1 = cb.equal(appnamePath, finalAppname);
					orPredicates.add(cb.and(p1));
				}
				if (!finalDeveloper.equals("")){
					Predicate p2 = cb.equal(developerPath, finalDeveloper);
					orPredicates.add(cb.and(p2));
				}
				//以下是springside3提供的方法
				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, MonitorEurekaServerInfo.class).toPredicate(root, query, cb);

				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p,o);

				return null;
			}
		}, page);
	}
//	删除监控项
	public int deleteItemById(Long id) {
		int i = eurekaServerInfoRepository.deleteItemById(id);
		return i;
	}
}
