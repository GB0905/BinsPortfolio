package com.example.BinsPortfolio.dto;

import com.example.BinsPortfolio.entity.Member;
import com.example.BinsPortfolio.entity.Project;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDefaultInfoDto {

    @NotEmpty(message = "비밀번호는 필수 항목 입니다.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상 16자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "이름 필수 항목 입니다.") // NULL체크 및 문자열의 경우 길이 0 인지 검사
    private String name;

    @NotEmpty(message = "주소는 필수 항목 입니다.") // NULL체크 및 문자열의 경우 길이 0 인지 검사
    private String address;

    @NotEmpty(message = "이메일는 필수 항목 입니다.") // NULL체크 및 문자열의 경우 길이 0 인지 검사
    @Email(message = "이메일 형식에 맞게 입력해주세요.") // 이메일 형식이 올바른지 검증한다.
    private String email;

    @NotEmpty(message = "연락처는 필수 항목 입니다.") // NULL체크 및 문자열의 경우 길이 0 인지 검사
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phone;

    @NotEmpty(message = "자기소개는 필수 항목 입니다.") // NULL체크 및 문자열의 경우 길이 0 인지 검사
    @Size(max = 198, min = 1)
    private String comment;

    @NotEmpty(message = "깃허브는 필수 항목 입니다.") // NULL체크 및 문자열의 경우 길이 0 인지 검사
    @Pattern(regexp = "^(https?:\\/\\/github.com\\/)\\S+", message = "깃허브 형식에 맞게 입력해주세요.")
    private String github;

    @NotEmpty(message = "학력은 필수 항목 입니다.") // NULL체크 및 문자열의 경우 길이 0 인지 검사
    private String education;

    // 모델 매핑 시 이름이 겹치는 내용만 매핑한다.
    private static ModelMapper modelMapper = new ModelMapper();

    public Member createMember() {
        return modelMapper.map(this, Member.class);
    }

    public static MemberDefaultInfoDto entityToDto(Member member) {
        return modelMapper.map(member, MemberDefaultInfoDto.class);
    }
}
