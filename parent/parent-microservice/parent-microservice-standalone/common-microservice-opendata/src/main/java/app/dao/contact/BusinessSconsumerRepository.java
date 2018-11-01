package app.dao.contact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import app.entity.contact.OpenData_Business_Consumer;

public interface BusinessSconsumerRepository extends JpaRepository<OpenData_Business_Consumer, Long>, JpaSpecificationExecutor<OpenData_Business_Consumer> {

	//分页查询
	Page<OpenData_Business_Consumer> findAll(Pageable pageable);
	
//	@Transactional
//	@Modifying
//	@Query(value = "update OpenData_Fee_Consumer set name=?1,phone=?2,email=?3,job=?4 where id=?5")
//	int updateSConsumerByTaskid(String name,String phone,String email,String job, Long id);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM product_business_consumer where business_consumer_id =?1", nativeQuery = true)
	int deleteBusinessSconsumerById(Long id);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM OpenData_Business_Consumer where id=?1")
	int deleteSconsumerById(Long id);
}
