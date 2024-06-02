package com.prj2spring20240521.mapper;

import com.prj2spring20240521.domain.Board;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("""
            INSERT INTO board(title,content,member_id) VALUES (#{title},#{content},#{memberId})
            """)
    int insertBoard(Board board);

    @Select("""
            SELECT id,title,member_id FROM board
            """)
    List<Board> getList();
}
