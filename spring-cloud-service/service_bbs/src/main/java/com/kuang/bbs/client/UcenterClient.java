package com.kuang.bbs.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XiaoZhang
 * @date 2022/2/6 15:32
 * 远程调用service-ucenter服务类
 */
@FeignClient(
        name = "service-ucenter" ,
        fallbackFactory = UcenterClientFactory.class
)
public interface UcenterClient {

    @GetMapping("/user/account/findUserNumber")
    R findUserNumber();

    @GetMapping("/user/account/findAvatarAndNicknameByUserId")
    R findAvatarAndNicknameByUserId();

    @GetMapping("/user/collection/findUserIsCollection")
    R findUserIsCollection(@RequestParam("articleId") String articleId);

    @GetMapping("/user/attention/findUserFansId")
    R findUserFansId(@RequestHeader("token") String token);

    //查询我的收藏文章数量
    @GetMapping("/user/collection/findMyCollectionArticleNumber")
    R findMyCollectionArticleNumber(@RequestHeader("token") String token);

}
