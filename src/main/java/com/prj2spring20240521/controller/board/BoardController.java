package com.prj2spring20240521.controller.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    @PostMapping("add")
//    로그인 했을 때만 보이게
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity add(
            Authentication authentication, Board board,
            @RequestParam(value = "files[]", required = false) MultipartFile[] files) {

        if (files != null) {
            System.out.println("files = " + files.length);
        }

        for (MultipartFile file : files) {
            System.out.println("file.name = " + file.getOriginalFilename());
        }

        if (service.validate(board)) {
            service.add(board, authentication);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // /api/board/list?1
    @GetMapping("list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(value = "type", required = false) String searchType,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return service.list(page, searchType, keyword);
    }

    // /api/board/5
    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Integer id) throws Exception {
        // react에서 Spinner 보려고 일부러 쓰레드 걸었음
//        Thread.sleep(1000);


        Board board = service.get(id);
        if (board == null) {
            // notFound() : 404 Not Found 상태 코드를 가진 ResponseEntity 객체를 생성하기 위한 정적 팩토리 메소드.
            // 메소드 호출 시, ResponseEntity.BodyBuilder 타입의 인스턴스가 반환된다.
            // .build() : 실제 ResponseEntity 객체 생성됨
            return ResponseEntity.notFound().build();
        }
        // .ok() : HTTP 200 OK 상태 코드를 가진 ResponseEntity를 생성하기 위한 정적 팩토리 메소드.
        // 메소드 호출 시, ResponseEntity.BodyBuilder 타입의 인스턴스가 반환된다.
        // .ok(board)는 안됨. ok()는 메서드
        // .body(board) : 응답 본문에 들어갈 객체를 전달. 실제 ResponseEntity 객체 생성됨

// ResponseEntity에서는 상황에 따라 build()를 호출하기도 하고, body()와 같은 메서드로 직접 ResponseEntity를 생성하기도 함. 모두 빌더 패턴의 개념을 사용한 것
        return ResponseEntity.ok().body(board);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    // @PathVariable : URL 경로 변수의 값을 메서드 파라미터로 바인딩해준다.
    public ResponseEntity delete(@PathVariable Integer id, Authentication authentication) {
        if (service.hasAccess(id, authentication)) {
            service.remove(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity edit(@RequestBody Board board, Authentication authentication) {
        if (!service.hasAccess(board.getId(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (service.validate(board)) {
            service.edit(board);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
