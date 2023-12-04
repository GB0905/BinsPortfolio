package com.example.BinsPortfolio.service;

import com.example.BinsPortfolio.dto.MemberDefaultInfoDto;
import com.example.BinsPortfolio.dto.MemberSkillInfoDto;
import com.example.BinsPortfolio.entity.Image;
import com.example.BinsPortfolio.entity.Member;
import com.example.BinsPortfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    @Value("${profileImageLocation}")
    private String imageLocation;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        member.setFrontend("- ");
        member.setBackend("- ");
        member.setMobile("- ");
        member.setTools("- ");
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMember(String userid, MemberDefaultInfoDto memberDefaultInfoDto) {
        Member member = getMember(userid);
        System.out.println(member);

        member.setName(memberDefaultInfoDto.getName());
        member.setEmail(memberDefaultInfoDto.getEmail()) ;
        member.setAddress(memberDefaultInfoDto.getAddress());
        member.setPhone(memberDefaultInfoDto.getPhone());
        member.setComment(memberDefaultInfoDto.getComment());
        member.setGithub(memberDefaultInfoDto.getGithub());
        member.setEducation(memberDefaultInfoDto.getEducation());
        System.out.println(member);

        return member;
    }

    @Transactional
    public Member updateMemberSkill(String userid, MemberSkillInfoDto memberSkillInfoDto) {
        Member member = getMember(userid);

        System.out.println(memberSkillInfoDto);

        member.setFrontend(memberSkillInfoDto.getFrontend());
        member.setBackend(memberSkillInfoDto.getBackend());
        member.setMobile(memberSkillInfoDto.getMobile());
        member.setTools(memberSkillInfoDto.getTools());

        System.out.println(member);

        return member;
    }

    @Transactional
    public Member updateMemberImage(MultipartFile file, String userid) throws IOException {
        Member member = getMember(userid);

        System.out.println(member);
        System.out.println(file);
        if(!file.isEmpty()) {
            if(member.getImage() != null) {
                imageService.deleteImage(member.getImage(), imageLocation);
            }
            Image image = imageService.saveImage(file, imageLocation);
            System.out.println(image);
            member.setImage(image);
        }
        return member;
    }

    public Member getMember(String userid) {
        return memberRepository.findByUserid(userid).get();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByUserid(member.getUserid());
        if(findMember.isPresent()) {

            System.out.println("member : " + findMember);
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List stringToList(String str) {
        List<String> list = new ArrayList<>();

        if(str == null) {
            return list;
        }

        String[] str1 = str.split("\\s*-\\s{1}");

        for(String s : str1) {
            if(s!=null&&!s.equals("")) {
                list.add(s);
            }
        }

        System.out.println(list.toString());

        return list;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserid(email).orElseThrow(() ->
                new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + email));

        log.info("[로그인 된 사용자] : " + member);

        // 스프링 시큐리티에서 제공하는 User 객체를 만들어 반환
        return User.builder()
                .username(member.getUserid())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
