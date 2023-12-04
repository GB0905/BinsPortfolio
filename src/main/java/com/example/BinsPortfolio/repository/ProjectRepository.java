package com.example.BinsPortfolio.repository;

import com.example.BinsPortfolio.entity.Member;
import com.example.BinsPortfolio.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> getProjectByIdAndMember(Long id, Member member);
    Page<Project> getAllByMemberOrderByStartDateAsc(Member member, Pageable pageable);

    void deleteProjectByIdAndMember(Long id, Member member);
}