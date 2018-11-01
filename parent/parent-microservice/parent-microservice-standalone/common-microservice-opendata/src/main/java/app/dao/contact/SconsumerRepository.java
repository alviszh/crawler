package app.dao.contact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import app.entity.contact.OpenData_Fee_Consumer;

public interface SconsumerRepository extends JpaRepository<OpenData_Fee_Consumer, Long>, JpaSpecificationExecutor<OpenData_Fee_Consumer> {

	//分页查询
	Page<OpenData_Fee_Consumer> findAll(Pageable pageable);
	
	@Transactional
	@Modifying
	@Query(value = "update OpenData_Fee_Consumer set name=?1,phone=?2,email=?3,job=?4 where id=?5")
	int updateSConsumerByTaskid(String name,String phone,String email,String job, Long id);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM OpenData_Fee_Consumer where id=?1")
	int deleteSconsumerById(Long id);
}
