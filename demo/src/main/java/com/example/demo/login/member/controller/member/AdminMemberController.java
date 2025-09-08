package com.example.demo.login.member.controller.member;

import com.example.demo.common.util.AdminValidator;
import com.example.demo.login.global.annotation.Member;
import com.example.demo.login.member.controller.member.dto.MemberCorporationDto;
import com.example.demo.login.member.controller.member.dto.MemberDto;
import com.example.demo.login.member.controller.member.dto.PagedResponse;
import com.example.demo.login.member.controller.auth.dto.MemberAdminResponse;
import com.example.demo.login.member.service.auth.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminService adminService;

    // ✅ 일반 회원 조회 (관리자만)
    @GetMapping("/normal")
    public ResponseEntity<?> getNormalMembers(@Member Long memberId,
                                              @RequestParam(defaultValue = "1") int page) {
        ResponseEntity<String> forbidden = AdminValidator.getStringResponseEntity(memberId);
        if (forbidden != null) return forbidden;

        return ResponseEntity.ok(adminService.getNormalMembers(page));
    }

    // ✅ 사업자 회원 조회 (관리자만)
    @GetMapping("/corporate")
    public ResponseEntity<?> getCorporateMembers(@Member Long memberId,
                                                 @RequestParam(defaultValue = "1") int page) {
        ResponseEntity<String> forbidden = AdminValidator.getStringResponseEntity(memberId);
        if (forbidden != null) return forbidden;

        return ResponseEntity.ok(adminService.getCorporateMembers(page));
    }
}
