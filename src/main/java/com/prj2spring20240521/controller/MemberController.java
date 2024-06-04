package com.prj2spring20240521.controller;

import com.prj2spring20240521.domain.Member;
import com.prj2spring20240521.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService service;

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody Member member) {
        if (service.validate(member)) {
            service.signup(member);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();

    }

}
