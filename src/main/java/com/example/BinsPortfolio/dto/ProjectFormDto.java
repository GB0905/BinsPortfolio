package com.example.BinsPortfolio.dto;

import com.example.BinsPortfolio.entity.Image;
import com.example.BinsPortfolio.entity.Project;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.*;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 27, min = 1, message = "27자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "소개는 필수 입력 값입니다.")
    @Size(max = 516, min = 1, message = "516자 이하로 입력해주세요.")
    private String introduction;

    @NotBlank(message = "스킬은 필수 입력 값입니다.")
    @Size(max = 516, min = 1, message = "516자 이하로 입력해주세요.")
    private String skills;

    @NotBlank(message = "링크는 필수 입력 값입니다.")
    @Size(max = 50, min = 1, message = "50자 이하로 입력해주세요.")
    private String link;

    @NotNull(message = "시작 날짜는 필수 입력 값입니다.")
    @Pattern(regexp = "(\\d{4})-(\\d{2})$", message = "날짜는 yyyy-MM 형식을 따릅니다")
    private String startDate;

    @NotNull(message = "종료 날짜는 필수 입력 값입니다.")
    @Pattern(regexp = "(\\d{4})-(\\d{2})$", message = "날짜는 yyyy-MM 형식을 따릅니다")
    private String endDate;

    @OneToOne
    private Image image;

    // 모델 매핑 시 이름이 겹치는 내용만 매핑한다.
    private static ModelMapper modelMapper = new ModelMapper();

    public Project createProject() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Project.builder()
                .id(id)
                .title(title)
                .introduction(introduction)
                .skills(skills)
                .link(link)
                .startDate(format.parse(startDate+"-01 00:00:00")) //ex) 2023-01-01 00:00:00
                .endDate(endDate)
                .build();
    }

    public static ProjectFormDto entityToDto(Project project) {
        return modelMapper.map(project, ProjectFormDto.class);
    }
}












