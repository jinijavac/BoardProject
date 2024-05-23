package org.example.boardproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.boardproject.domain.Board;
import org.example.boardproject.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

//예외 페이지 만들기
//

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public String boards(Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page -1 , size);

        Page<Board> boards =  boardService.findAllBoard(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage",page);
        return "board/list";
    }

    @GetMapping("/view")
    public String view(@RequestParam("id") Long id, Model model){
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "board/view";
    }

    @GetMapping("/writeform")
    public String addform(Model model){
        model.addAttribute("board", new Board());
        return "board/writeform";
    }

    @PostMapping("/writeform")
    public String add(@ModelAttribute Board board, RedirectAttributes redirectAttributes){
        board.setCreatedAt(LocalDateTime.now()); // 현재 시간 설정
        boardService.saveBoard(board);
        redirectAttributes.addFlashAttribute("message", "게시글 추가");
        return "redirect:/board";
    }

    @GetMapping("/updateform")
    public String editform(@RequestParam("id") Long id, Model model){
        model.addAttribute("board", boardService.findBoardById(id));
        return "board/edit";
    }


    @PostMapping("/updateform")
    public String edit(@ModelAttribute Board board){
        board.setCreatedAt(LocalDateTime.now());
        boardService.saveBoard(board);
        return "redirect:/board";
    }

    @GetMapping("/deleteform")
    public String deleteForm(@RequestParam("id") Long id, Model model){
        model.addAttribute("id", id);
        return "board/deleteform";
    }

    @PostMapping("/deleteform")
    public String delete(@RequestParam("id") Long id, @RequestParam("password") String password, RedirectAttributes redirectAttributes){
        try {
            boardService.deleteBoard(id, password);
            redirectAttributes.addFlashAttribute("message", "게시글 삭제 완료");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/board";
    }
}

