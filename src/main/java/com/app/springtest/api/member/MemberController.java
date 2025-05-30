package com.app.springtest.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

     @GetMapping("/{id}")
     public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
         return ResponseEntity.ok(memberService.getMemberById(id));
     }

}
