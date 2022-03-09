package com.kuang.ucenter.client;

import com.kuang.springcloud.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author XiaoZhang
 * @date 2022/2/6 11:35
 * 远程调用service-vip服务熔断降级类
 */
@Component
@Slf4j
public class VipClientFactory implements FallbackFactory<VipClient> {

    @Override
    public VipClient create(Throwable throwable) {
        return new VipClient() {
            @Override
            public R findMemberRightLogo(List<String> userIdList) {
                log.error("远程调用service-vip下面的/vm/user/findMemberRightLogo方法失败");
                return R.error();
            }

            @Override
            public R findMemberRightVipLevel(String userId) {
                log.error("远程调用service-vip下面的/vm/user/findMemberRightVipLevel方法失败");
                return R.error();
            }

            @Override
            public R addArticle() {
                log.error("远程调用service-vip下面的/usertodayright/addArticle方法失败");
                return R.error();
            }

            @Override
            public R findMemberRightVipLevelAndIsSign(String token) {
                log.error("远程调用service-vip下面的/vm/user/findMemberRightVipLevelAndIsSign方法失败");
                return R.error();
            }

            @Override
            public R findSignExperience() {
                log.error("远程调用service-vip下面的/vm/user/findSignExperience方法失败");
                return R.error();
            }

            @Override
            public R toSign() {
                log.error("远程调用service-bbs下面的/usertodayright/toSign接口失败");
                return R.error();
            }
        };
    }
}
