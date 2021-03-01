package com.bwee.webit.server.controller;

import com.bwee.webit.server.model.security.LoginRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public ResponseEntity login(@RequestHeader final String username,
                                @RequestHeader final String password,
                                final HttpServletResponse response) {
        final String token;

        if (StringUtils.equals(username, "DeezNuts")) {
            token = "CQIUC9lAKq2H";
            final Cookie cookie = new Cookie("au", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(cookie);
        } else {
            token = "";
        }
        return ResponseEntity.ok(new LoginRes().setAuthToken(token));
    }
}
