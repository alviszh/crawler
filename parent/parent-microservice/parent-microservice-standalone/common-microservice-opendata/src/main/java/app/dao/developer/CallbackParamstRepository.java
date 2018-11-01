package app.dao.developer;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.developer.CallbackParams;


public interface CallbackParamstRepository extends JpaRepository<CallbackParams, Long> {
	

}
