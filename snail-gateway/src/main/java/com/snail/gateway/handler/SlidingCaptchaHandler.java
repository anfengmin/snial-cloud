package com.snail.gateway.handler;

import com.snail.common.core.utils.R;
import com.snail.gateway.service.ValidateCodeService;
import com.snail.gateway.vo.SlidingCaptchaVo;
import com.snail.common.core.exception.CaptchaException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 滑动验证码获取
 *
 * @author ruoyi
 */
@Component
public class SlidingCaptchaHandler implements HandlerFunction<ServerResponse> {

    @Resource
    private ValidateCodeService validateCodeService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        R<SlidingCaptchaVo> ajax;
        try {
            ajax = validateCodeService.createSlidingCaptcha();
        } catch (CaptchaException | IOException e) {
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(ajax));
    }
}


