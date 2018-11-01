package app.service;


import app.dao.developer.AppProductListRepository;
import app.entity.developer.AppProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zmy
 *
 */
@Component
@Service
public class AppProductListService {
    @Autowired
    AppProductListRepository appProductListRepository;

    /**
     * 查询产品的回调配置信息
     * @param prodClientId 应用名称
     * @param flag 产品名称
     * @param appmode 运行环境
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public AppProductList queryProduct(String prodClientId, String flag, String appmode) {
        AppProductList appProductList = appProductListRepository.findByProdClientIdAndFlagAndAppmode(prodClientId,flag,appmode);
        return appProductList;
    }
}
