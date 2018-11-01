package app.dao.developer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.developer.AppProductList;
import org.springframework.data.jpa.repository.Query;


public interface AppProductListRepository extends JpaRepository<AppProductList, Long> {
	
	List<AppProductList> findByAppIdAndAppmode(String appId,String Mode);

	@Query(value = "select * from opendata_app_productlist oap, opendata_app oa, opendata_product op where oap.opendata_app_id = oa.id and oap.product_id = op.id and oa.prod_client_id=?1 and op.flag = ?2 and oap.appmode = ?3", nativeQuery = true)
	AppProductList findByProdClientIdAndFlagAndAppmode(String prod_client_id, String flag, String appmode);
}
