package com.prj2spring20240521.domain.comment;

import java.time.LocalDateTime;

public class Comment {
    private Integer id;
    private Integer boardId;
    private Integer memberId;
    private String comment;
    private LocalDateTime inserted;
}
