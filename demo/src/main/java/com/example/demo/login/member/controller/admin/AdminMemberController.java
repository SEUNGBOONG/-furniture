package com.example.demo.login.member.controller.admin;

import com.example.demo.login.member.controller.auth.dto.MemberAdminResponse;
import com.example.demo.login.member.service.auth.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<MemberAdminResponse>> getAllMembers() {
        List<MemberAdminResponse> members = adminService.getAllMembers();
        return ResponseEntity.ok(members);
    }
}
