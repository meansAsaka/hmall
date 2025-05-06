package com.hmall.api.config;


import com.hmall.api.clients.UserClient;
import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {

    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.NONE;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 获取登录用户
                Long userId = UserContext.getUser();
                if (userId == null) {
                    // 如果为空则直接跳过
                    return;
                }
                // 如果不为空，传递给下游微服务
                requestTemplate.header("user-info", userId.toString());
            }
        };
    }
}
