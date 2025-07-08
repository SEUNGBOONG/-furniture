package com.example.demo.login.member.service.auth;

import com.example.demo.login.member.controller.auth.dto.MemberAdminResponse;
import com.example.demo.login.member.infrastructure.member.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberJpaRepository memberJpaRepository;

    @Transactional(readOnly = true)
    public List<MemberAdminResponse> getAllMembers() {
        return memberJpaRepository.findAll().stream()
                .map(MemberAdminResponse::from)
                .toList();
    }
}
