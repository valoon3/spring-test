package com.app.springtest.api.member;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @MockitoBean
    private MemberRepository memberRepository;

    @InjectMocks // @Mock으로 생성된 객체들을 MemberService에 주입
    private MemberService memberService;

    private Member member;
    private MemberResponse memberResponse;

    @BeforeEach
    void setUp() {
        // 테스트에 사용될 Member 객체 초기화 (실제 Member 클래스 구조에 따라 필드 설정)
        // Member 클래스에 public 생성자나 빌더, 또는 setter가 필요할 수 있습니다.
        // 여기서는 ReflectionTestUtils나 적절한 생성자가 있다고 가정합니다.
        // 예시로 간단히 생성 (실제로는 Member.java의 생성자/setter에 맞게 수정 필요)
        member = new Member();
        // ReflectionTestUtils.setField(member, "id", 1L);
        // ReflectionTestUtils.setField(member, "email", "test@example.com");
        // ReflectionTestUtils.setField(member, "nickname", "tester");
        // ReflectionTestUtils.setField(member, "name", "Test User");

        // MemberResponse도 유사하게 초기화 (실제 MemberResponse 구조에 맞게 수정 필요)
        memberResponse = MemberResponse.builder()
                .id(1L)
                .email("test@example.com")
                .nickname("tester")
                .name("Test User")
                .build();
    }

    @Test
    @DisplayName("회원 ID로 조회 성공 시 MemberResponse 반환")
    void getMemberById_whenMemberExists_shouldReturnMemberResponse() {
        // given
        Long memberId = 1L;
        // memberRepository.findById가 Member 객체를 포함한 Optional을 반환하도록 설정
        // 실제 Member 객체와 MemberResponse 객체의 필드를 일치시켜야 합니다.
        // Member 객체에 ID와 다른 필드들이 설정되어 있다고 가정
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        // member 객체의 getter가 memberResponse와 동일한 값을 반환한다고 가정
        // 예: when(member.getId()).thenReturn(memberResponse.getId());
        //    when(member.getEmail()).thenReturn(memberResponse.getEmail()); 등

        // when
        MemberResponse actualResponse = memberService.getMemberById(memberId);

        // then
        assertThat(actualResponse).isNotNull();
        // 실제 member 객체의 필드 값과 비교하도록 수정해야 합니다.
        // 예를 들어, member 객체에 setId, setEmail 등을 호출하여 memberResponse와 동일한 값을 가지도록 설정한 후,
        // assertThat(actualResponse.getId()).isEqualTo(member.getId()); 와 같이 검증
        assertThat(actualResponse.getId()).isEqualTo(memberResponse.getId());
        assertThat(actualResponse.getEmail()).isEqualTo(memberResponse.getEmail());
        assertThat(actualResponse.getNickname()).isEqualTo(memberResponse.getNickname());
        assertThat(actualResponse.getName()).isEqualTo(memberResponse.getName());

        verify(memberRepository).findById(memberId); // findById가 1번 호출되었는지 검증
    }

    @Test
    @DisplayName("회원 ID로 조회 실패 시 EntityNotFoundException 발생")
    void getMemberById_whenMemberNotExists_shouldThrowEntityNotFoundException() {
        // given
        Long nonExistentId = 99L;
        // memberRepository.findById가 빈 Optional을 반환하도록 설정
        when(memberRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            memberService.getMemberById(nonExistentId);
        });

        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 아이디입니다.");
        verify(memberRepository).findById(nonExistentId); // findById가 1번 호출되었는지 검증
    }

    @Test
    @DisplayName("회원 ID가 null일 때 NullPointerException 또는 유사한 예외 발생 가능성 (선택적 테스트)")
    void getMemberById_whenIdIsNull_shouldThrowException() {
        // given
        Long nullId = null;
        // memberRepository.findById(null)이 어떤 동작을 할지 Mockito로 정의할 수 있지만,
        // 보통 이런 경우는 IllegalArgumentException 등을 서비스 메소드 시작 부분에서 검증하는 것이 좋습니다.
        // 현재 코드에서는 findById에 null이 전달되면 Repository 레벨에서 NPE가 발생할 가능성이 높습니다.
        // 또는 when(memberRepository.findById(null)).thenThrow(new IllegalArgumentException("ID는 null일 수 없습니다."));

        // when & then
        // 예를 들어, Repository가 NPE를 던진다고 가정 (실제 Spring Data JPA 구현에 따라 다를 수 있음)
        when(memberRepository.findById(null)).thenThrow(new NullPointerException("ID cannot be null for findById"));

        assertThrows(NullPointerException.class, () -> {
            memberService.getMemberById(nullId);
        });

        verify(memberRepository).findById(nullId);
    }

}