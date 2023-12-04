package com.example.BinsPortfolio.entity;

import com.example.BinsPortfolio.constant.Role;
import com.example.BinsPortfolio.dto.MemberFormDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String userid;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(length=198)
    private String comment;

    private String name;

    private String address;

    private String email;

    private String phone;

    private String github;

    private String education;

    private String frontend;

    private String backend;

    private String tools;

    private String mobile;

    @OneToOne
    private Image image;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Project> projects;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .userid(memberFormDto.getUserid())
                .password(memberFormDto.getPassword())
                .role(Role.USER)
//                .comment("")
//                .name("")
//                .address("")
//                .email("")
//                .phone("")
//                .github("")
//                .education("")
//                .frontend(null)
//                .backend(null)
//                .tools(null)
//                .mobile(null)
                .build();

        member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));

        return member;
    }
}