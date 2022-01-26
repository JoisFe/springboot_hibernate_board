package com.example.board.service;

import com.example.board.domain.Criteria;
import com.example.board.domain.board.Board;
import com.example.board.domain.board.JpaBoardRepository;
import com.example.board.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final JpaBoardRepository boardRepository;

    public Long boardListCnt() {
        return boardRepository.count();
    }

    public List<BoardDto> boardList(Criteria cri) {

        //List<Board> boards = boardRepository.findAll(pageRequest);
        List<Board> boards = boardRepository.findAllGreaterThan(cri);
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (Board board : boards) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .author(board.getAuthor())
                    .createdDate(board.getCreatedDate())
                    .modifiedDate(board.getModifiedDate())
                    .build();

            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }

    public BoardDto getBoard(Long id) {
        Optional<Board> boardWrapper = boardRepository.findById(id);
        Board board = boardWrapper.get();

        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .author(board.getAuthor())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }

    @Transactional
    public void uploadBoard(BoardDto boardDto) {
        boardDto.setCreatedDate(LocalDateTime.now());
        boardDto.setModifiedDate(LocalDateTime.now());
        boardRepository.save(boardDto.toEntity());
    }

    @Transactional
    public void updateBoard(BoardDto boardDto) {
        boardDto.setModifiedDate(LocalDateTime.now());
        boardRepository.update(boardDto.getId(), boardDto.getTitle(), boardDto.getContent());
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.delete(id);
    }

    public String validTest(BindingResult errors, MemberService memberService) {
        Map<String, String> validatorResult = memberService.validateHandling(errors);
        for (String key : validatorResult.keySet()) {
            if (!(key.equals("valid_author") || key.equals("valid_boardId"))) return validatorResult.get(key);
        }
        return "ok"; // author 혹은 boardId 경우 valid에 걸려도 무시하고 이후에 값 넣어줄 것이기 때문에 넘어감
    }

}
