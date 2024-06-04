package com.prj2spring20240521.service;

import com.prj2spring20240521.domain.Member;
import com.prj2spring20240521.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;

    public void signup(Member member) {
        mapper.insert(member);
    }
}
