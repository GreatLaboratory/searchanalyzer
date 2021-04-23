package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.dto.UserJoinRequestDto;
import kr.co.tbase.searchad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    // 회원가입 - db에 회원정보 저장
    @PostMapping("/join")
    public String join(UserJoinRequestDto userJoinRequestDto) {
        userService.save(userJoinRequestDto);
        return "redirect:/login";
    }

    // 회원가입 페이지로 이동
    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    // 로그인 페이지로 이동
    @GetMapping(value = {"/","/login"})
    public String loginPage() {
        return "login";
    }

    // 회원목록 페이지로 이동
    @GetMapping("/admin/users")
    public String adminPage(Model model, @RequestParam(defaultValue = "") String keyword, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        model.addAttribute("users", userService.search(keyword, pageable));
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("keyword", keyword);
        model.addAttribute("checkNext", userService.checkNext(pageable));
        model.addAttribute("checkPrevious", userService.checkPrevious(pageable));

        return "userList";
    }
}
