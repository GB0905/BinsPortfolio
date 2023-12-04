package com.example.BinsPortfolio.controller;

import com.example.BinsPortfolio.dto.MemberDefaultInfoDto;
import com.example.BinsPortfolio.dto.MemberFormDto;
import com.example.BinsPortfolio.dto.ProjectFormDto;
import com.example.BinsPortfolio.entity.Member;
import com.example.BinsPortfolio.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/member/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping ("/member/new")
    public String insertMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        log.info("===============> memberFormDto : " + memberFormDto);

        if(bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping("/member/login")
    public String loginForm() {
        log.info("===============> 로그인");
//        model.addAttribute("pagename", "login");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!authentication.getName().equals("anonymousUser")) {
            return "redirect:/";
        }

        return "member/memberLoginForm";
    }

    @GetMapping("/member/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 패스워드를 확인해주세요");

        return "member/memberLoginForm";
    }

    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("===============> 로그아웃");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null) {
            log.info("authentication != null");
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/member/login";
    }

}