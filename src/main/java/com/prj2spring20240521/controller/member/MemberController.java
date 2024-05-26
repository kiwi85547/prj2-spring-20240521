package com.prj2spring20240521.controller.member;

import com.prj2spring20240521.domain.member.Member;
import com.prj2spring20240521.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@ResponseBody + @Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService service;

    @PostMapping("signup")
    public ResponseEntity<Object> signup(@RequestBody Member member) {
        System.out.println("member = " + member);
        if (service.validate(member)) {
            service.add(member);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "check", params = "email")
    public ResponseEntity checkEmail(@RequestParam("email") String email) {
        System.out.println("email = " + email);
        Member member = service.getByEmail(email);
        // 없을 때 notFound
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(email);
    }

    @GetMapping(value = "check", params = "nickName")
    public ResponseEntity checkNickName(@RequestParam("nickName") String nickName) {
        System.out.println("nickName = " + nickName);
        Member member = service.getByNickName(nickName);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nickName);
    }

    @GetMapping("list")
    public List<Member> list() {
        return service.getList();
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Integer id) {
        Member member = service.getById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(member);
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity delete(@RequestBody Member member, Authentication authentication) {
        if (service.hasAccess(member, authentication)) {
            service.remove(member.getId());
            return ResponseEntity.ok().build();
        }

        // 남의 것을 지우려고 할 때 403 응답받기
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    // 여기서부터 수업 못들었음 #################################################

    @PutMapping("modify")
    public ResponseEntity modify(@RequestBody Member member) {
        if (service.hasAccessModify(member)) {
            service.modify(member);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    // 여기까지 수업 못들었음 #################################################

    @PostMapping("token")
    public ResponseEntity token(@RequestBody Member member) { // 회원가입 시 받은 이메일,패스워드
        Map<String, Object> map = service.getToken(member);
        if (map == null) {
            // 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 200 ok
        return ResponseEntity.ok(map);
    }

}
