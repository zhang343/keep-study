package com.kuang.bbs.controller.admin;

import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/bbs")
@Slf4j
public class AdminArticleController {

    @GetMapping("abc")
    public R abc(){
        return R.ok().data("abb" , 123);
    }
}
