package com.kuang.ucenter.controller.inside;

import com.kuang.springcloud.utils.R;
import com.kuang.ucenter.service.UserAttentionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/inside/attention")
@Slf4j
public class InsideAttentionController {

    @Resource
    private UserAttentionService attentionService;

    //查询出用户的所有粉丝id
    @GetMapping("findUserFansId")
    public R findUserFansId(String userId){
        List<String> userIdList = attentionService.findUserFansId(userId);
        return R.ok().data("userIdList" , userIdList);
    }
}
