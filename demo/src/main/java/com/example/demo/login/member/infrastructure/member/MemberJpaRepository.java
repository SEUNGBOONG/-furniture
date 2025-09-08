package com.example.demo.login.member.infrastructure.member;

import com.example.demo.login.member.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberNickName(String memberNickName);

    boolean existsByMemberEmail(String memberEmail);

    Optional<Member> findMemberByMemberEmail(String memberEmail);

    Optional<Member> findById(Long memberId);

    List<Member> findAll();

    @Query(value = "SELECT * FROM member m WHERE m.check_corporation = false LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Member> findNormalMembers(int limit, int offset);

    @Query(value = "SELECT COUNT(*) FROM member m WHERE m.check_corporation = false", nativeQuery = true)
    long countNormalMembers();

    // ✅ 사업자 회원 조회
    @Query(value = "SELECT * FROM member m WHERE m.check_corporation = true LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Member> findCorporateMembers(int limit, int offset);

    @Query(value = "SELECT COUNT(*) FROM member m WHERE m.check_corporation = true", nativeQuery = true)
    long countCorporateMembers();
}
