package com.example.demo.admin.notice.domain.repository;

import com.example.demo.admin.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
