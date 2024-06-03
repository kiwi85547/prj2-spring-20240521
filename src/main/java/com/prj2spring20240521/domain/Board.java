package com.prj2spring20240521.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    Integer id;
    String title;
    String content;
    Integer memberId;
    LocalDateTime inserted;
}
