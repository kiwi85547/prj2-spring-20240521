package com.prj2spring20240521.service.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.domain.board.BoardFile;
import com.prj2spring20240521.mapper.board.BoardMapper;
import com.prj2spring20240521.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;
    private final MemberMapper memberMapper;

    // Authentication에 subject로 넘겨준 email이 들어있음
    // email에서 id로 바꾸면서 Member member = memberMapper.selectByEmail(authentication.getName()); 코드 삭제
    public void add(Board board, MultipartFile[] files, Authentication authentication) throws IOException {
        board.setMemberId(Integer.valueOf(authentication.getName()));
        // 게시물 저장

        mapper.insert(board);

        if (files != null) {
            for (MultipartFile file : files) {
                // db에 해당 게시물의 파일 목록 저장
                mapper.insertFileName(board.getId(), file.getOriginalFilename());
                // 실제 파일 저장
                // 부모 디렉토리 만들기
                String dir = STR."C:/Temp/prj2/\{board.getId()}";
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }


                // 파일 경로
                String path = STR."C:/Temp/prj2/\{board.getId()}/\{file.getOriginalFilename()}";
                File destination = new File(path);
                file.transferTo(destination);
            }
        }
    }

    public boolean validate(Board board) {
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }
        return true;

    }

    public Map<String, Object> list(Integer page, String searchType, String keyword) {
        Map pageInfo = new HashMap();
        Integer countAll = mapper.countAllWithPaging(searchType, keyword);

        Integer offset = (page - 1) * 10;
        Integer lastPageNumber = (countAll - 1) / 10 + 1;
        Integer leftPageNumber = (page - 1) / 10 * 10 + 1;
        Integer rightPageNumber = leftPageNumber + 9;
        rightPageNumber = Math.min(rightPageNumber, lastPageNumber);
        leftPageNumber = rightPageNumber - 9;
        leftPageNumber = Math.max(leftPageNumber, 1);
        Integer prevPageNumber = leftPageNumber - 1;
        Integer nextPageNumber = rightPageNumber + 1;

        // rightPageNumber는 lastPageNumber 보다 크지 않도록

        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("lastPageNumber", lastPageNumber);
        pageInfo.put("leftPageNumber", leftPageNumber);
        pageInfo.put("rightPageNumber", rightPageNumber);

        return Map.of("pageInfo", pageInfo,
                "boardList", mapper.selectAllPaging(offset, searchType, keyword));
    }

    public Board get(Integer id) {
        Board board = mapper.selectById(id);
        List<String> filesNames = mapper.selectFileNameByBoardId(id);
        List<BoardFile> files = filesNames.stream()
                .map(name -> new BoardFile(name, STR."http://192.168.224.1:8888/\{id}/\{name}"))
                .toList();

        board.setFileList(files);

        // http://192.168.224.1:8888/{id}/{name}
        return board;
    }

    public void remove(Integer id) {
        // file 명 조회
        List<String> fileNames = mapper.selectFileNameByBoardId(id);

        // disk 에 있는 file
        String dir = STR."C:/Temp/prj2/\{id}/";
        for (String fileName : fileNames) {
            File file = new File(dir + fileName);
            file.delete();
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.delete();
        }

        // board_file
        mapper.deleteFileByBoardId(id);

        // board

        mapper.deleteById(id);
    }

    public void edit(Board board, List<String> removeFileList) {
        // disk의 파일 삭제
        if (removeFileList != null && removeFileList.size() > 0) {

            for (String fileName : removeFileList) {

                String path = STR."C:/Temp/prj2/\{board.getId()}/\{fileName}";
                File file = new File(path);
                file.delete();
                // db records 삭제

                mapper.deleteFileByBoardIdAndName(board.getId(), fileName);
            }
        }
        mapper.update(board);
    }


    public boolean hasAccess(Integer id, Authentication authentication) {
        Board board = mapper.selectById(id);

        return board.getMemberId()
                .equals(Integer.valueOf(authentication.getName()));
    }
}
