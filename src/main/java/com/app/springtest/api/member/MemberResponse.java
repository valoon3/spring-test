package com.app.springtest.api.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponse {
    private Long id;
    private String email;
    private String nickname;
    private String name;
}
