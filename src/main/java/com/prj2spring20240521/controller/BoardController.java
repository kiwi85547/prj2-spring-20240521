package com.prj2spring20240521.controller;

import com.prj2spring20240521.domain.Board;
import com.prj2spring20240521.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    @PostMapping("add")
    public void add(@RequestBody Board board) {
        service.add(board);
        System.out.println("board = " + board);
    }
}
