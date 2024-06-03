package com.prj2spring20240521.controller;

import com.prj2spring20240521.domain.Board;
import com.prj2spring20240521.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {
        if (service.validate(board)) {
            service.add(board);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("list")
    public List<Board> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public ResponseEntity board(@PathVariable Integer id) {
        if (service.getBoard(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(service.getBoard(id));
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody Board board) {
        if (service.validate(board)) {
            service.update(board);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
