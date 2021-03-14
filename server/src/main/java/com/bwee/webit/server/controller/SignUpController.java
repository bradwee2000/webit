package com.bwee.webit.server.controller;

import com.bwee.webit.auth.AuthTokenGenerator;
import com.bwee.webit.model.WebitUser;
import com.bwee.webit.service.WebitUserService;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Slf4j
@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private WebitUserService webitUserService;

    @Autowired
    private AuthTokenGenerator tokenGenerator;

    @PostMapping
    public ResponseEntity signup(@RequestBody final SignupReq req) {
        final WebitUser user = webitUserService.createNewUser(new WebitUserService.CreateNewUser()
                .setName(req.getName())
                .setUsername(req.getUsername())
                .setPassword(req.getPassword()));

        final String token = tokenGenerator.generateToken(user);

        return ResponseEntity.ok(new TokenRes(token));
    }

    @Data
    @Accessors(chain = true)
    public static class SignupReq {
        @NotEmpty
        @Size(min = 4, max = 50)
        private String name;

        @NotEmpty
        @Size(min = 4, max = 50)
        private String username;

        @NotEmpty
        @Size(min = 4, max = 50)
        private String password;
    }

    @Data
    @Accessors(chain = true)
    public static class TokenRes {
        private String accessToken;

        private TokenRes(final String accessToken) {
            this.accessToken = accessToken;
        }
    }
}