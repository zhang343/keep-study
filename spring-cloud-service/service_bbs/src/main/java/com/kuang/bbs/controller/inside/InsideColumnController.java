package com.kuang.bbs.controller.inside;

import com.kuang.bbs.service.ColumnService;
import com.kuang.springcloud.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/column")
public class InsideColumnController {

    @Resource
    private ColumnService columnService;

    //查看用户专栏数量
    @GetMapping("findUserColumnNumber")
    public R findUserColumnNumber(String userId){
        Integer columnNumber = columnService.findUserColumnNumber(userId);
        return R.ok().data("columnNumber" , columnNumber);
    }

    //查看其他用户专栏数量
    @GetMapping("findOtherUserColumnNumber")
    public R findOtherUserColumnNumber(String userId){
        Integer columnNumber = columnService.findOtherUserColumnNumber(userId);
        return R.ok().data("columnNumber" , columnNumber);
    }
}
