package app.service.crawler;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.microservice.persistence.DynamicSpecifications;

import app.entity.crawler.MonitorTelecomTasker;
import app.repository.crawler.MonitorTelecomTaskerRepository;
/**
 * @author sln
 * @Description: 爬虫层面—数据库相关服务
 */
@Component
public class MonitorCrawlerService {
	@Autowired
	private MonitorTelecomTaskerRepository taskerRepository;
	/**
	 * 根据条件分页查询任务列表
	 * @param searchParams
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	//如下注解是后来加上的，为了避免黄线
	@SuppressWarnings({ "deprecation", "serial" })   
	public Page<MonitorTelecomTasker> getCarrierTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize){
		Sort sort = new Sort(Sort.Direction.ASC, "developer");
		Pageable page = new PageRequest(currentPage, pageSize, sort);
		
		String province = "";
		String developer = "";
		
		if (searchParams.get("province") != null)
			province = (String) searchParams.get("province");
		if ( searchParams.get("developer") != null)
			developer = (String) searchParams.get("developer");

		final String finalProvince = province;
		final String finalDeveloper = developer;
		return taskerRepository.findAll(new Specification<MonitorTelecomTasker>() {

			public Predicate toPredicate(Root<MonitorTelecomTasker> root,
										 CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> provincePath = root.get("province");
				Path<String> developerPath = root.get("developer");

				//path转化
				List<Predicate> orPredicates = Lists.newArrayList();

				if (!finalProvince.equals("")) {
					Predicate p1 = cb.equal(provincePath, finalProvince);
					orPredicates.add(cb.and(p1));
				}
				if (!finalDeveloper.equals("")){
					Predicate p2 = cb.equal(developerPath, finalDeveloper);
					orPredicates.add(cb.and(p2));
				}
				//以下是springside3提供的方法
				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, MonitorTelecomTasker.class).toPredicate(root, query, cb);

				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p,o);

				return null;
			}
		}, page);
	}
//	删除监控项
	public int deleteItemById(Long id) {
		int i = taskerRepository.deleteItemById(id);
		return i;
	}
//	添加监控项
	public MonitorTelecomTasker saveCarrierItem(MonitorTelecomTasker monitorTelecomTasker) {
		return taskerRepository.save(monitorTelecomTasker);
	}
}
