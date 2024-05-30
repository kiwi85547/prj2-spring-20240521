package com.prj2spring20240521.service.comment;

import com.prj2spring20240521.domain.comment.Comment;
import com.prj2spring20240521.mapper.CommentMapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommentService {

    final CommentMapper mapper;

    @PreAuthorize("isAuthenticated()")
    public String add(Comment comment, Authentication authentication) {
        comment.setMemberId(Integer.valueOf(authentication.getName()));
        mapper.insert(comment);

        return "hello";
    }

    public List<Comment> list(Integer boardId) {
        return mapper.selectAllByBoardId(boardId);
    }

    public boolean validate(Comment comment) {
        if (comment == null) {
            return false;
        }
        if (comment.getComment().isBlank()) {
            return false;
        }
        if (comment.getBoardId() == null) {
            return false;
        }
        return true;
    }

    @PreAuthorize("isAuthenticated()")
    public void remove(Comment comment) {
        mapper.deleteById(comment.getId());
    }

    public boolean hasAccess(Comment comment, Authentication authentication) {
        if (!authentication.getName().equals(comment.getMemberId().toString())) {
            return false;
        } else {
            return true;
        }
    }
}
