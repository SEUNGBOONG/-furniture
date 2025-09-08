package com.example.demo.login.member.service.auth;

import com.example.demo.common.util.AdminValidator;
import com.example.demo.login.member.controller.member.dto.MemberCorporationDto;
import com.example.demo.login.member.controller.member.dto.MemberDto;
import com.example.demo.login.member.controller.member.dto.PagedResponse;
import com.example.demo.login.member.controller.auth.dto.MemberAdminResponse;
import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.member.infrastructure.member.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberJpaRepository memberJpaRepository;
    private static final int PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    public List<MemberAdminResponse> getAllMembers() {
        return memberJpaRepository.findAll().stream()
                .map(MemberAdminResponse::from)
                .toList();
    }

    // ✅ 일반 회원 조회
    public PagedResponse<MemberDto> getNormalMembers(int page) {
        int offset = (page - 1) * PAGE_SIZE;
        List<Member> members = memberJpaRepository.findNormalMembers(PAGE_SIZE, offset);
        long total = memberJpaRepository.countNormalMembers();

        List<MemberDto> dtos = members.stream()
                .map(m -> new MemberDto(
                        m.getMemberName(),
                        m.getRoadAddress(),
                        m.getJibunAddress(),
                        m.getZipCode(),
                        m.getPhoneNumber()
                ))
                .toList();

        return new PagedResponse<>(total, dtos);
    }

    // ✅ 사업자 회원 조회
    public PagedResponse<MemberCorporationDto> getCorporateMembers(int page) {
        int offset = (page - 1) * PAGE_SIZE;
        List<Member> members = memberJpaRepository.findCorporateMembers(PAGE_SIZE, offset);
        long total = memberJpaRepository.countCorporateMembers();

        List<MemberCorporationDto> dtos = members.stream()
                .map(m -> new MemberCorporationDto(
                        m.getMemberName(),
                        m.getRoadAddress(),
                        m.getJibunAddress(),
                        m.getZipCode(),
                        m.getPhoneNumber(),
                        m.getCorporationNumber(),
                        m.getCorporationImageURL()
                ))
                .toList();

        return new PagedResponse<>(total, dtos);
    }
}
