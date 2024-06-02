package com.prj2spring20240521.service;

import com.prj2spring20240521.domain.Board;
import com.prj2spring20240521.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper mapper;

    public void add(Board board) {
        mapper.insertBoard(board);
    }

    public List<Board> list() {
        return mapper.getList();
    }
}
