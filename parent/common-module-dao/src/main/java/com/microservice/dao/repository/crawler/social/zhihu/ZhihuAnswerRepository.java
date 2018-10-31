package com.microservice.dao.repository.crawler.social.zhihu;

import com.microservice.dao.entity.crawler.soical.zhihu.ZhihuAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZhihuAnswerRepository extends JpaRepository<ZhihuAnswer, Long> {
    ZhihuAnswer findByAnswerCode(String questionCode);
}
