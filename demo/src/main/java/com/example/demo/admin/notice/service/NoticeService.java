package com.example.demo.admin.notice.service;

import com.example.demo.admin.notice.controller.dto.NoticeRequest;
import com.example.demo.admin.notice.domain.entity.Notice;
import com.example.demo.admin.notice.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Notice createNotice(NoticeRequest request) {
        return noticeRepository.save(Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build());
    }

    public Notice updateNotice(Long id, NoticeRequest request) {
        Notice notice = getOrElseThrow(id);
        notice.update(request.getTitle(), request.getContent());
        return notice;
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    public Notice getNotice(Long id) {
        return getOrElseThrow(id);
    }

    private Notice getOrElseThrow(final Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));
    }
}
