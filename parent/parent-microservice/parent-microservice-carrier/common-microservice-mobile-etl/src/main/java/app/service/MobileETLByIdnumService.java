package app.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.BasicUser;
import com.microservice.dao.repository.crawler.mobile.BasicUserRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.bean.MobileEtlEnum;
import app.bean.WebDataByID;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile" })

public class MobileETLByIdnumService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private BasicUserRepository basicUserRepository;
	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Value("${spring.profiles.active}")
	String profile;

	public WebDataByID getAllData(String idnum) {

		WebDataByID webDataByID = new WebDataByID();

		if (StringUtils.isBlank(idnum)) {

			webDataByID.setIdnum(idnum);
			webDataByID.setMessage(MobileEtlEnum.MOBILE_ETL_IDNUM_NULL.getMessage());
			webDataByID.setErrorCode(MobileEtlEnum.MOBILE_ETL_IDNUM_NULL.getErrorCode());
			webDataByID.setProfile(profile);
			return webDataByID;

		} else {
			// 先通过idnum查出唯一的basicUserId；
			List<BasicUser> basicUsers = basicUserRepository.findByIdnum(idnum.trim());
			if (null != basicUsers && basicUsers.size() > 0) {
				List<Long> ids = new ArrayList<Long>();
				for (BasicUser basicUser2 : basicUsers) {
					ids.add(basicUser2.getId());
				}
				// long basicUserId = basicUser.getId();
				// 再通过basicUserId在taskmobile表中查询数据
				List<String> phonenumbers = taskMobileRepository
						.findPhonenumByBasicUserIdAndFinishedAndDescription(ids);
				if (null != phonenumbers && phonenumbers.size() > 0) {
					webDataByID.setIdnum(idnum);
					webDataByID.setPhoneNumbers(phonenumbers);
					webDataByID.setMessage(MobileEtlEnum.MOBILE_ETL_SUCCESS.getMessage());
					webDataByID.setErrorCode(MobileEtlEnum.MOBILE_ETL_SUCCESS.getErrorCode());
					webDataByID.setProfile(profile);
					return webDataByID;

				} else {
					webDataByID.setIdnum(idnum);
					webDataByID.setMessage(MobileEtlEnum.MOBILE_ETL_IDNUM_RESULT_NULL.getMessage());
					webDataByID.setErrorCode(MobileEtlEnum.MOBILE_ETL_IDNUM_RESULT_NULL.getErrorCode());
					webDataByID.setProfile(profile);
					return webDataByID;
				}
			} else {
				webDataByID.setIdnum(idnum);
				webDataByID.setMessage(MobileEtlEnum.MOBILE_ETL_NO_SUCH_IDNUM.getMessage());
				webDataByID.setErrorCode(MobileEtlEnum.MOBILE_ETL_NO_SUCH_IDNUM.getErrorCode());
				webDataByID.setProfile(profile);
				return webDataByID;
			}
		}
	}

}
