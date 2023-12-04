package com.example.BinsPortfolio.controller;

import com.example.BinsPortfolio.dto.ProjectFormDto;
import com.example.BinsPortfolio.entity.Image;
import com.example.BinsPortfolio.entity.Project;
import com.example.BinsPortfolio.service.PdfService;
import com.example.BinsPortfolio.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/project/delete/{id}")
    public String projectDelete(@PathVariable Long id) {
        log.info("===============> delete project");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication);
        System.out.println(authentication.getName());

        if(authentication.getName().equals("anonymousUser")) {
            return "redirect:/member/login";
        }

        projectService.deleteProject(id, authentication.getName());

        return "redirect:/projects";
    }

    @GetMapping("/projects")
    public String projectList(Model model, @PageableDefault(size = 4, page = 1) Pageable pageable) {
        log.info("===============> projects");
        model.addAttribute("pagename", "projects");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication);
        System.out.println(authentication.getName());
        System.out.println(pageable);

        if(authentication.getName().equals("anonymousUser")) {
            return "redirect:/member/login";
        }

        Page<Project> projects = projectService.getProjects(authentication.getName(), pageable);

        int blockLimit = 4;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), projects.getTotalPages());
        String nlString = System.getProperty("line.separator").toString();

        model.addAttribute("nlString", nlString);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("data", projects);

        System.out.println(projects.isEmpty());

        return "project/projectList";
    }

    @PostMapping("/project/new")
    public String projectNew(@Valid ProjectFormDto projectFormDto,
                             BindingResult bindingResult,
                             @RequestParam(value = "imageFile", required = false) MultipartFile file,
                              Model model) {

        log.info("===============> project/new post");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(bindingResult.hasErrors()) {
            return "project/projectForm";
        }

        log.info("===============> project/new post");
        if(file.isEmpty() && projectFormDto.getId() == null) {
            model.addAttribute("errorMessage", "이미지는 필수입니다.");
            return "project/projectForm";
        }

        log.info("===============> project/new post");
        try {
            projectService.saveProject(projectFormDto, file, authentication.getName());
        } catch (IOException e) {
            model.addAttribute("errorMessage", "포트폴리오 업로드중 오류가 발생했습니다.");
            return "project/projectForm";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/projects";
    }

    @GetMapping("/project/new")
    public String projectForm(Model model) {
        log.info("===============> project/new");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication);
        System.out.println(authentication.getName());

        if(authentication.getName().equals("anonymousUser")) {
            return "redirect:/member/login";
        }

        model.addAttribute("projectFormDto", new ProjectFormDto());
        return "project/projectForm";
    }

    @PostMapping("/project/update")
    public String projectUpdate(@Valid ProjectFormDto projectFormDto,
                             BindingResult bindingResult,
                             @RequestParam(value = "imageFile", required = false) MultipartFile file,
                             Model model) {

        log.info("===============> project/new post");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(projectFormDto);

        if(bindingResult.hasErrors()) {
            Image image = projectService.getProject(authentication.getName(), projectFormDto.getId()).getImage();
            projectFormDto.setImage(image);
            model.addAttribute("projectFormDto", projectFormDto);
            return "project/projectForm";
        }

        log.info("===============> project/new post");
        try {
            projectService.updateProject(projectFormDto, file, authentication.getName());
        } catch (IOException e) {
            model.addAttribute("errorMessage", "포트폴리오 수정중 오류가 발생했습니다.");
            return "project/projectForm";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/projects";
    }

    @GetMapping("/project/{id}/edit")
    public String projectForm(@PathVariable Long id, Model model) {
        log.info("===============> project/edit");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication);
        System.out.println(authentication.getName());

        if(authentication.getName().equals("anonymousUser")) {
            return "redirect:/member/login";
        }

        Project project = projectService.getProject(authentication.getName(), id);

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

            ProjectFormDto projectFormDto = ProjectFormDto.builder()
                    .id(project.getId())
                    .image(project.getImage())
                    .startDate(format.format(project.getStartDate()))
                    .endDate(project.getEndDate())
                    .title(project.getTitle())
                    .introduction(project.getIntroduction())
                    .skills(project.getSkills())
                    .link(project.getLink())
                    .build();

            model.addAttribute("projectFormDto", projectFormDto);
            return "project/projectForm";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "project/projectForm";
    }

}
