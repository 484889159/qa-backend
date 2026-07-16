package com.qa.controller;

import com.qa.common.Result;
import com.qa.entity.User;
import com.qa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        if (username == null || password == null) {
            return Result.error("用户名和密码不能为空");
        }

        User user = userService.login(username, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        String token = userService.generateToken(user);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("nickname", user.getNickname());
        data.put("avatar", user.getAvatar());
        data.put("role", user.getRole());

        return Result.success(data);
    }

    @GetMapping("/info")
    public Result getUserInfo(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        User user = userService.getCurrentUser(token);
        if (user == null) {
            return Result.error("用户未登录");
        }

        return Result.success(user);
    }
}