package com.example.BinsPortfolio.repository;

import com.example.BinsPortfolio.entity.Member;
import com.example.BinsPortfolio.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserid(String userid);
    Optional<Member> findByUseridAndPassword(String userid, String password);
}
