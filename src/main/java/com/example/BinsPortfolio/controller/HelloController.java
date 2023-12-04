package com.example.BinsPortfolio.controller;

import com.example.BinsPortfolio.dto.MemberDefaultInfoDto;
import com.example.BinsPortfolio.dto.MemberSkillInfoDto;
import com.example.BinsPortfolio.entity.Member;
import com.example.BinsPortfolio.service.MemberService;
import com.example.BinsPortfolio.service.PdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HelloController {

    private final MemberService memberService;

    @GetMapping("/")
    public String index(Model model) {
        log.info("===============> 메인");
        model.addAttribute("pagename", "index");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication);
        System.out.println(authentication.getName());

        if(authentication.getName().equals("anonymousUser")) {
            return "redirect:/member/login";
        }

        Member member = memberService.getMember(authentication.getName());
        String nlString = System.getProperty("line.separator").toString();

        model.addAttribute("nlString", nlString);
        model.addAttribute("data", member);
        model.addAttribute("frontend", memberService.stringToList(member.getFrontend()));
        model.addAttribute("backend", memberService.stringToList(member.getBackend()));
        model.addAttribute("mobile", memberService.stringToList(member.getMobile()));
        model.addAttribute("tools", memberService.stringToList(member.getTools()));

        return "index";
    }

    @GetMapping("/mypage")
    public String mypage(Model model) {
        model.addAttribute("pagename", "mypage");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getName().equals("anonymousUser")) {
            return "redirect:/member/login";
        }

        try {
            Member member = memberService.getMember(authentication.getName());
            MemberDefaultInfoDto memberDefaultInfoDto = MemberDefaultInfoDto.builder()
                    .name(member.getName())
                    .address(member.getAddress())
                    .email(member.getEmail())
                    .phone(member.getPhone())
                    .comment(member.getComment())
                    .github(member.getGithub())
                    .education(member.getEducation())
                    .build();

            MemberSkillInfoDto memberSkillInfoDto = MemberSkillInfoDto.builder()
                    .frontend(member.getFrontend())
                    .backend(member.getBackend())
                    .mobile(member.getMobile())
                    .tools(member.getTools())
                    .build();

            model.addAttribute("image", member.getImage());
            model.addAttribute("memberDefaultInfoDto", memberDefaultInfoDto);
            model.addAttribute("memberSkillInfoDto", memberSkillInfoDto);
        } catch (Exception e) {
            model.addAttribute("message", "마이페이지 수정중 오류가 발생했습니다.");
            return "mypage/mypage";
        }

        return "mypage/mypage";
    }

    @PostMapping("/member/modify")
    public String mypageModify(@Valid MemberDefaultInfoDto memberDefaultInfoDto,
                             BindingResult bindingResult, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(bindingResult.hasErrors()) {
            Member member = memberService.getMember(authentication.getName());

            MemberSkillInfoDto memberSkillInfoDto = MemberSkillInfoDto.builder()
                    .frontend(member.getFrontend())
                    .backend(member.getBackend())
                    .mobile(member.getMobile())
                    .tools(member.getTools())
                    .build();

            model.addAttribute("image", member.getImage());
            model.addAttribute("memberDefaultInfoDto", memberDefaultInfoDto);
            model.addAttribute("memberSkillInfoDto", memberSkillInfoDto);

            return "mypage/mypage";
        }

        try {
            memberService.updateMember(authentication.getName(), memberDefaultInfoDto);
        } catch (Exception e) {
            model.addAttribute("message", "마이페이지 수정중 오류가 발생했습니다.");
            return mypage(model);
        }
        model.addAttribute("message", "수정 완료");

        return mypage(model);
    }

    @PostMapping("/member/modify/skill")
    public String mypageModifySkill(MemberSkillInfoDto memberSkillInfoDto, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            memberService.updateMemberSkill(authentication.getName(), memberSkillInfoDto);
        } catch (Exception e) {
            model.addAttribute("message", "마이페이지 수정중 오류가 발생했습니다.");

            return mypage(model);
        }
        model.addAttribute("message", "저장 완료");

        return mypage(model);
    }

    @PostMapping("/member/modify/image")
    public String mypageModifyImage(@RequestParam(value = "imageFile", required = false) MultipartFile file, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            memberService.updateMemberImage(file, authentication.getName());
        } catch (Exception e) {
            model.addAttribute("message", "이미지 업로드중 오류가 발생했습니다.");

            return mypage(model);
        }
        model.addAttribute("message", "업로드 완료");

        return mypage(model);
    }
}