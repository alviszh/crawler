package app.dao.developer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.developer.App;


public interface AppRepository extends JpaRepository<App,Long> {
	
	List<App> findBysUserIdAndAppNameContainingOrderById(Long sUserId,String appName);
	
	App findBysUserIdAndId(Long sUserId,Long id);
	
	App findBysUserIdAndAppId(Long sUserId,String appId);
	

}