package com.kuang.bbs.client;


import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;


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
            public R findAvatarAndNicknameByUserId(String userId) {
                return R.error();
            }

            @Override
            public R findUserIsCollection(String articleId , String userId) {
                return R.error();
            }

            @Override
            public R findUserFansId(String userId) {
                return R.error();
            }

        };
    }
}
