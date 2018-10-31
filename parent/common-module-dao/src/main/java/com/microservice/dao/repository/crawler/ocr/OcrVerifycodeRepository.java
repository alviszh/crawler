package com.microservice.dao.repository.crawler.ocr;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.ocr.OcrVerifycode;

public interface OcrVerifycodeRepository extends JpaRepository<OcrVerifycode, Long>{

	OcrVerifycode findByImgageName(String imageName);

}
