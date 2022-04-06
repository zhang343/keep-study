package com.kuang.bbs.controller.admin;

import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bbs/admin")
@Slf4j
public class AdminArticleController {


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("one")
    public R one(){
        return R.ok().message("one方法");
    }
}
