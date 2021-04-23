package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @DeleteMapping("/api/v1/user/{id}")
    public Long deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return id;
    }


}
