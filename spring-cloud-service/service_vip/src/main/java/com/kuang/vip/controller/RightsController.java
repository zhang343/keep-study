package com.kuang.vip.controller;


import com.kuang.springcloud.utils.R;
import com.kuang.vip.entity.vo.RightsVo;
import com.kuang.vip.service.RightsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-07
 * 权益处理类
 */
@RestController
@RequestMapping("/vm/trai")
@Slf4j
public class RightsController {

    @Resource
    private RightsService rightsService;

    //查询出vip权益,非普通会员权益
    @GetMapping("findVipRight")
    public R findVipRight(){
        log.info("查询出vip权益,非普通会员权益");
        List<RightsVo> rightsList = rightsService.findVipRight();
        return R.ok().data("vipList" , rightsList);
    }
}

