package com.example.BinsPortfolio.service;

import com.example.BinsPortfolio.dto.ProjectFormDto;
import com.example.BinsPortfolio.entity.Image;
import com.example.BinsPortfolio.entity.Member;
import com.example.BinsPortfolio.entity.Project;
import com.example.BinsPortfolio.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectsRepository;
    private final ImageService imageService;
    private final MemberService memberService;
    private final PdfService pdfService;

    @Value("${portfolioImgLocation}")
    private String imageLocation;

    @Transactional
    public void deleteProject(Long id, String userid) {
        Member member = memberService.getMember(userid);
        projectsRepository.deleteProjectByIdAndMember(id, member);
    }

    public Long saveProject(ProjectFormDto projectFormDto, MultipartFile itemImgFileList, String userid) throws IOException, ParseException {
        System.out.println(projectFormDto);

        Image image = imageService.saveImage(itemImgFileList, imageLocation);

        Project project = projectFormDto.createProject();
        project.setImage(image);
        project.setMember(memberService.getMember(userid));
        project.setPath("/pdfs/"+pdfService.createPdf(project));
        projectsRepository.save(project);

        System.out.println(project);
        return project.getId();
    }

    @Transactional
    public Long updateProject(ProjectFormDto projectFormDto, MultipartFile file, String userid) throws IOException, ParseException {
        Project originalProject = getProject(userid, projectFormDto.getId());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        originalProject.setStartDate(format.parse(projectFormDto.getStartDate()+"-01 00:00:00")); //ex) 2023-01-01 00:00:00
        originalProject.setEndDate(projectFormDto.getEndDate());
        originalProject.setTitle(projectFormDto.getTitle());
        originalProject.setIntroduction(projectFormDto.getIntroduction());
        originalProject.setSkills(projectFormDto.getSkills());
        originalProject.setLink(projectFormDto.getLink());

        System.out.println("====================>updateProject");
        System.out.println(file);
        if(!file.isEmpty() && projectFormDto.getId() != null) {
            System.out.println("====================>file is not empty");
            imageService.deleteImage(originalProject.getImage(), imageLocation);
            originalProject.setImage(imageService.saveImage(file, imageLocation));
        }

        originalProject.setPath("/pdfs/"+pdfService.createPdf(originalProject));

        System.out.println(projectFormDto);
        System.out.println(originalProject);
        return originalProject.getId();
    }

    public Page getProjects(String userid, Pageable pageable) {
        Member member = memberService.getMember(userid);
        Page<Project> projects = projectsRepository.getAllByMemberOrderByStartDateAsc(member, PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "startDate")));
        System.out.println(projects.toString());
        System.out.println("getProjects");
        for(Project project:projects) {
            System.out.println(project.toString());
        }
        return projects;
    }
    public Project getProject(String userid, Long projectid) {
        return projectsRepository.getProjectByIdAndMember(projectid, memberService.getMember(userid)).get();
    }
}
