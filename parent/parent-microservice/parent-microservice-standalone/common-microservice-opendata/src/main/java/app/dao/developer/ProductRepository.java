package app.dao.developer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.developer.Product;
import app.entity.developer.enums.ProductStatus;


public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByProductStatusOrderById(ProductStatus productStatus);
	
	List<Product> findByProductStatusNotOrderById(ProductStatus productStatus);

}
