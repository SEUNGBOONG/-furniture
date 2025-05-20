package com.example.demo.login.member.service.auth;

import com.example.demo.config.s3.S3Uploader;
import com.example.demo.login.global.exception.PleaseAttachImage;
import com.example.demo.login.global.exception.exceptions.InvalidRegistrationNumber;
import com.example.demo.login.member.controller.auth.dto.LoginRequest;
import com.example.demo.login.member.controller.auth.dto.LoginResponse;
import com.example.demo.login.member.controller.auth.dto.SignUpRequest;
import com.example.demo.login.member.domain.member.Member;
import com.example.demo.login.member.exception.exceptions.auth.DuplicateEmailException;
import com.example.demo.login.member.exception.exceptions.auth.DuplicateNickNameException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidEmailFormatException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidLoginRequestException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidPasswordFormatException;
import com.example.demo.login.member.exception.exceptions.auth.InvalidSignUpRequestException;
import com.example.demo.login.member.exception.exceptions.auth.NotFoundMemberByEmailException;
import com.example.demo.login.member.infrastructure.auth.JwtTokenProvider;
import com.example.demo.login.member.infrastructure.member.MemberJpaRepository;

import com.example.demo.login.member.mapper.auth.AuthMapper;
import com.example.demo.login.util.CorporationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberJpaRepository memberJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CorporationValidator corporationValidator;
    private final S3Uploader s3Uploader;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Member signUp(SignUpRequest signUpRequest, MultipartFile corporationImage) {
        validateSignupRequestFormat(signUpRequest);
        validateEmailFormat(signUpRequest.memberEmail());
        checkPasswordLength(signUpRequest.memberPassword());
        checkDuplicateMemberNickName(signUpRequest.memberNickName());
        checkDuplicateMemberEmail(signUpRequest.memberEmail());

        if (signUpRequest.checkCorporation()) {
            checkBusinessNumber(signUpRequest, corporationImage);
            return checkBusinessMember(signUpRequest, corporationImage);
        }

        Member member = AuthMapper.toNormalMember(signUpRequest);
        return memberJpaRepository.save(member);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        validateLoginRequestFormat(loginRequest);
        Member member = findMemberByEmail(loginRequest.memberEmail());
        member.checkPassword(loginRequest.memberPassword());

        String token = jwtTokenProvider.createToken(member.getId());
        return AuthMapper.toLoginResponse(token, member);
    }

    private void validateSignupRequestFormat(SignUpRequest signUpRequest) {
        if (signUpRequest == null ||
                isEmpty(signUpRequest.memberEmail()) ||
                isEmpty(signUpRequest.memberName()) ||
                isEmpty(signUpRequest.memberPassword()) ||
                isEmpty(signUpRequest.memberNickName())) {
            throw new InvalidSignUpRequestException();
        }
    }

    private void validateEmailFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailFormatException();
        }
    }

    private void checkDuplicateMemberNickName(String nickName) {
        if (memberJpaRepository.existsByMemberNickName(nickName)) {
            throw new DuplicateNickNameException();
        }
    }

    private Member checkBusinessMember(final SignUpRequest signUpRequest, final MultipartFile corporationImage) {
        String imageUrl = s3Uploader.uploadFile(corporationImage);
        Member member = AuthMapper.toMember(signUpRequest, imageUrl);
        return memberJpaRepository.save(member);
    }

    private void checkBusinessNumber(final SignUpRequest signUpRequest, final MultipartFile corporationImage) {
        if (!corporationValidator.isValidCorporationNumber(signUpRequest.corporationNumber())) {
            throw new InvalidRegistrationNumber();
        }

        if (corporationImage == null || corporationImage.isEmpty()) {
            throw new PleaseAttachImage();
        }
    }

    private void checkDuplicateMemberEmail(String email) {
        if (memberJpaRepository.existsByMemberEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    private void checkPasswordLength(String password) {
        if (password.length() <= 7) {
            throw new InvalidPasswordFormatException();
        }
    }


    private void validateLoginRequestFormat(LoginRequest loginRequest) {
        if (loginRequest == null ||
                isEmpty(loginRequest.memberEmail()) ||
                isEmpty(loginRequest.memberPassword())) {
            throw new InvalidLoginRequestException();
        }
    }

    private Member findMemberByEmail(String email) {
        return memberJpaRepository.findMemberByMemberEmail(email)
                .orElseThrow(NotFoundMemberByEmailException::new);
    }
}
