package com.kuang.vip.controller.Inside;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.service.MembersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inside/member")
@Slf4j
public class InsideMemberController {

    @Resource
    private MembersService membersService;

    //查询用户的viplogo
    @GetMapping("findUserVipLevelByUserIdList")
    public R findUserVipLevelByUserIdList(@RequestParam("userIdList") List<String> userIdList){
        if(userIdList == null || userIdList.size() == 0){
            throw new XiaoXiaException(ResultCode.ERROR , "请传递正确参数");
        }
        Object userVipLevel = membersService.findUserVipLevel(userIdList);
        return R.ok().data((Map<String, Object>) userVipLevel);
    }
}
