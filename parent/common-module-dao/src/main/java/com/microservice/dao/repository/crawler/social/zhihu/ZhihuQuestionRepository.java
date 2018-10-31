package com.microservice.dao.repository.crawler.social.zhihu;

import com.microservice.dao.entity.crawler.soical.zhihu.ZhihuQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZhihuQuestionRepository extends JpaRepository<ZhihuQuestion, Long> {
    ZhihuQuestion findByQuestionCode(String questionCode);
}
