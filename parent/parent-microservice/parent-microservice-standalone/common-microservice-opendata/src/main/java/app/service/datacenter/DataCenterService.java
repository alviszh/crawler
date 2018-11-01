package app.service.datacenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import app.dao.developer.AppRepository;
import app.dao.developer.ProductRepository;
import app.entity.developer.App;
import app.entity.developer.Product;

@Component
@Service
public class DataCenterService {

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private ProductRepository productRepository;

	public List<App> findAllApp() {
		List<App> findAll = appRepository.findAll();
		return findAll;
	}
	
	public App findProductByAppEnvironment(String appId) {
		Long valueOf = Long.valueOf(appId);
		App app = appRepository.findById(valueOf).get();
		return app;
	}

	public List<Product> findAllProduct() {
		List<Product> listProduct = productRepository.findAll();
		return listProduct;
	}

}
