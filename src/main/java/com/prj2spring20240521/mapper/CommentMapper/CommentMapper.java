package com.prj2spring20240521.mapper.CommentMapper;

import com.prj2spring20240521.domain.comment.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("""
            INSERT INTO comment(board_id, member_id, comment) VALUES (#{boardId},#{memberId},#{comment})
            """)
    int insert(Comment comment);

    @Select("""
            SELECT c.id,c.comment,c.inserted,c.member_id, m.nick_name
            FROM comment c JOIN member m ON c.member_id=m.id 
            WHERE board_id=#{boardId} ORDER BY id
            """)
    List<Comment> selectAllByBoardId(Integer boardId);

    @Delete("""
            DELETE FROM comment WHERE id = #{id}
            """)
    void deleteById(Integer id);

    @Select("""
            SELECT * FROM comment WHERE id = #{id}
            """)
    Comment selectById(Integer id);

    @Delete("""
            DELETE FROM comment WHERE board_id = #{boardId}
            """)
    int deleteByBoardId(Integer boardId);

    @Delete("""
            DELETE FROM comment WHERE member_id = #{memberId}
            """)
    int deleteByMemberId(Integer memberId);

    @Update("""
            UPDATE comment SET comment=#{comment} WHERE id = #{id}
            """)
//    넘어온 파라미터가 객체여도 안써도 됨. comment의 {프로퍼티}
    int update(Comment comment);
}