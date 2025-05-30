package com.app.springtest.api.member;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMemberById(Long id) {

        Optional<Member> optionalMember = memberRepository.findById(id);
        Member member = optionalMember
                .orElseThrow(() -> new EntityNotFoundException("존제하지 않는 아이디입니다."));

        return MemberResponse.builder().
                id(member.getId()).
                email(member.getEmail()).
                nickname(member.getNickname()).
                name(member.getName()).
                build();

    }

}
