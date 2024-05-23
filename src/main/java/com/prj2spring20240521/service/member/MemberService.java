package com.prj2spring20240521.service.member;

import com.prj2spring20240521.domain.member.Member;
import com.prj2spring20240521.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper mapper;
    final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;


    public void add(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        // 앞뒤 공백 제거
        member.setEmail(member.getEmail().trim());
        member.setNickName(member.getNickName().trim());
        mapper.insert(member);
    }

    public Member getByEmail(String email) {
        return mapper.selectByEmail(email.trim());
    }

    public Member getByNickName(String nickName) {
        return mapper.selectByNickName(nickName.trim());
    }

    public boolean validate(Member member) {
        if (member.getEmail() == null || member.getEmail().isBlank()) {
            return false;
        }
        if (member.getNickName() == null || member.getNickName().isBlank()) {
            return false;
        }
        if (member.getPassword() == null || member.getPassword().isBlank()) {
            return false;
        }
        String emailPattern = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*";
        if (!member.getEmail().trim().matches(emailPattern)) {
            return false;
        }
        return true;

    }

    public List<Member> getList() {
        return mapper.selectAll();
    }

    public Member getById(Integer id) {
        return mapper.selectById(id);
    }

    public void remove(Integer id) {
        mapper.deleteById(id);
    }

    public boolean hasAccess(Member member) {
        Member dbmember = mapper.selectById(member.getId());

        if (dbmember == null) {
            return false;
        }
        // .matches(평문,암호화된 키)
        return passwordEncoder.matches(member.getPassword(), dbmember.getPassword());
    }

    // 여기서부터 수업 못들었음 #################################################
    public void modify(Member member) {
        if (member.getPassword() != null && member.getPassword().length() > 0) {
            // 패스워드가 입력되었으니 바꾸기
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        } else {
            // 입력 안됐으니 기존 값으로 유지
            Member dbMember = mapper.selectById(member.getId());
            member.setPassword(dbMember.getPassword());
        }
        mapper.update(member);
    }

    public boolean hasAccessModify(Member member) {
        Member dbMember = mapper.selectById(member.getId());
        if (dbMember == null) {
            return false;
        }

        if (!passwordEncoder.matches(member.getOldPassword(), dbMember.getPassword())) {
            return false;
        }

        return true;
    }

    // 여기까지 수업 못들었음 #################################################

    public Map<String, Object> getToken(Member member) {

        Map<String, Object> result = null;

        //로그인 성공하면 만들기
        Member db = mapper.selectByEmail(member.getEmail());

        // controller에서 if문 통과해서 hashmap이 있음
        if (db != null) {
            if (passwordEncoder.matches(member.getPassword(), db.getPassword())) {
                result = new HashMap<>();
                String token = "";
                // https://github.com/spring-projects/
                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer("self")
                        // now가 안되서 Instant.now()로 바꿈
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(60 * 60 * 24 * 7)) // 만료기한 여기선 일주일
                        .subject(member.getEmail()) // 사용자의 id, email 등
                        .claim("scope", "") // 권한
                        .claim("nickName", db.getNickName())
                        .build();

                token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

                // 토큰이 담김
                result.put("token", token);
            }
        }
        return result;
    }


}
