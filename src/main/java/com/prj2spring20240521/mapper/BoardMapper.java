package com.prj2spring20240521.mapper;

import com.prj2spring20240521.domain.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("""
            INSERT INTO board(title,content,member_id) VALUES (#{title},#{content},#{memberId})
            """)
    int insertBoard(Board board);

    @Select("""
            SELECT id,title,member_id FROM board ORDER BY id DESC
            """)
    List<Board> getList();

    @Select("""
            SELECT * FROM board WHERE id=#{id}
            """)
    Board getBoardById(Integer id);

    @Delete("""
            DELETE FROM board WHERE id=#{id}
            """)
    void deleteBoardById(Integer id);

    @Update("""       
            UPDATE board SET title=#{title}, content=#{content} WHERE id=#{id}
            """)
    int updateById(Board board);
}
