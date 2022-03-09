package com.kuang.bbs.client;


import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author XiaoZhang
 * @date 2022/2/6 11:35
 * 远程调用service-ucenter服务熔断降级类
 */
@Component
@Slf4j
public class UcenterClientFactory implements FallbackFactory<UcenterClient> {

    @Override
    public UcenterClient create(Throwable throwable) {
        return new UcenterClient() {
            @Override
            public R findUserNumber() {
                log.error("远程调用service-ucenter下面的/user/account/findUserNumber方法失败");
                return R.error();
            }

            @Override
            public R findAvatarAndNicknameByUserId() {
                log.error("远程调用service-ucenter下面的/user/account/findAvatarAndNicknameByUserId方法失败");
                return R.error();
            }

            @Override
            public R findUserIsCollection(String articleId) {
                log.error("远程调用service-ucenter下面的/user/collection/findUserIsCollection方法失败");
                return R.error();
            }

            @Override
            public R findUserFansId(String token) {
                log.error("远程调用service-ucenter下面的/user/attention/findUserFansId方法失败");
                return R.error();
            }
        };
    }
}
