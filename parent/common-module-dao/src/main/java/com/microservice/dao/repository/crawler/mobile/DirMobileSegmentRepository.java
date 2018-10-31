package com.microservice.dao.repository.crawler.mobile;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.DirMobileSegment;


public interface DirMobileSegmentRepository extends JpaRepository<DirMobileSegment, Long> {

	DirMobileSegment findByPrefix(String prefix);
}
