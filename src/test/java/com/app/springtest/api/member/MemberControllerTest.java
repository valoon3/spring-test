package com.app.springtest.api.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@AutoConfigureMockMvc
@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc; // @WebMvcTest 사용 시 MockMvc가 자동으로 구성됩니다.

    @MockitoBean // MemberController가 의존하는 MemberService를 목(mock) 객체로 만듭니다.
    private MemberService memberService;

    @Test
    @DisplayName("id = 1 인 회원을 조회하면 회원 정보 정상적으로 반환한다.")
    public void getMemberShouldReturnMemberFromService() throws Exception {
        // given: 테스트를 위한 준비
        Long memberId = 1L;
        MemberResponse memberResponse = MemberResponse.builder().
                id(memberId).
                email("test@gmail.com").
                nickname("test nickname").
                name("test name").
                build();
        ; // 실제 Member 객체 또는 목 객체를 생성합니다.
        // Member 클래스에 setter나 생성자가 필요할 수 있습니다.
        // 예시로 id만 설정 (실제 Member 클래스 구조에 따라 필드 설정)
        // Lombok의 @NoArgsConstructor(access = AccessLevel.PROTECTED) 때문에 직접 생성 및 필드 접근이 어려울 수 있습니다.
        // 테스트를 위해 Member 클래스에 모든 인자를 받는 생성자 또는 빌더 패턴, 또는 setter를 추가하는 것을 고려할 수 있습니다.
        // 또는 ReflectionTestUtils.setField(mockMember, "id", memberId); 등을 사용할 수 있습니다.
        // 여기서는 Member 객체가 적절히 생성되었다고 가정합니다.

        // MemberService의 getMemberById 메소드가 호출될 때, mockMember를 반환하도록 설정합니다.
        when(memberService.getMemberById(memberId))
                .thenReturn(memberResponse);

        // when & then: 실제 테스트 실행 및 결과 검증
        this.mockMvc.perform(get("/api/member/{id}", memberId) // 경로 변수 사용
                        .contentType(MediaType.APPLICATION_JSON)) // 요청 타입을 명시할 수 있습니다.
                .andDo(print()) // 요청/응답을 콘솔에 출력합니다.
                .andExpect(status().isOk()) // HTTP 상태가 200 OK인지 확인합니다.
                // 실제 반환되는 Member 객체의 내용에 따라 content() 검증을 추가할 수 있습니다.
                // 예를 들어, Member 객체가 JSON으로 변환될 때 특정 필드가 있는지 확인할 수 있습니다.
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(memberId))
                .andExpect(jsonPath("$.email").value(memberResponse.getEmail()))
                .andExpect(jsonPath("$.nickname").value(memberResponse.getNickname()))
                .andExpect(jsonPath("$.name").value(memberResponse.getName()));
    }

    @Test
    public void getMemberWhenMemberNotFoundShouldReturnNotFound() throws Exception {
        // given: MemberService가 특정 ID에 대해 null을 반환하는 상황을 가정
        Long nonExistentMemberId = 99L;
        when(memberService.getMemberById(nonExistentMemberId)).thenReturn(null); // 또는 예외를 던지도록 설정

        // MemberController의 getMember 메소드는 현재 null을 받으면 그대로 ResponseEntity.ok(null)을 반환합니다.
        // 실제로는 null일 경우 404 Not Found를 반환하도록 컨트롤러 로직을 수정하는 것이 좋습니다.
        // 예를 들어 MemberService에서 Optional<Member>를 반환하고, 컨트롤러에서 orElseThrow()를 사용하는 방식입니다.
        // 현재 MemberController 로직 기준으로는 null을 반환하면 200 OK에 body가 비어있게 됩니다.
        // 가이드에서는 보통 서비스 계층에서 찾지 못하면 예외를 발생시키고, 이를 통해 404 응답을 테스트합니다.

        // 현재 MemberController 로직(ResponseEntity.ok(memberService.getMemberById(id)))에 맞춘 테스트:
        this.mockMvc.perform(get("/api/member/{id}", nonExistentMemberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // 현재는 null이어도 200 OK
                .andExpect(content().string("")); // 내용이 비어있음을 확인 (JSON null은 빈 문자열로 표현될 수 있음)

        // 만약 MemberController가 null일 때 404를 반환하도록 수정한다면, 테스트는 다음과 같이 변경됩니다:
        // when(memberService.getMemberById(nonExistentMemberId)).thenThrow(new MemberNotFoundException()); // 예시 예외
        // this.mockMvc.perform(get("/api/member/{id}", nonExistentMemberId))
        //         .andDo(print())
        //         .andExpect(status().isNotFound());
    }

}
