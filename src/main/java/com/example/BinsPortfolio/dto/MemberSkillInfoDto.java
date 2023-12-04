package com.example.BinsPortfolio.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSkillInfoDto {
    private String frontend;
    private String backend;
    private String mobile;
    private String tools;
}
