package com.prj2spring20240521.mapper;

import com.prj2spring20240521.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    @Insert("""
            INSERT INTO member(email, password, nick_name) VALUES(#{email},#{password},#{nickName})
            """)
    int insert(Member member);
}
