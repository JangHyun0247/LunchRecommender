package com.sparta.lunchrecommender.global.aspect.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "LogAop")
@Aspect
@Component
public class LogAop {

    @Pointcut("execution(* com.sparta.lunchrecommender.domain.auth.controller.AuthController.*(..))")
    private void auth(){}

    @Pointcut("execution(* com.sparta.lunchrecommender.domain.comment.controller.CommentController.*(..))")
    private void comment(){}

    @Pointcut("execution(* com.sparta.lunchrecommender.domain.follow.controller.FollowController.*(..))")
    private void follow(){}

    @Pointcut("execution(* com.sparta.lunchrecommender.domain.like.controller.LikeController.*(..))")
    private void like(){}

    @Pointcut("execution(* com.sparta.lunchrecommender.domain.post.controller.PostController.*(..))")
    private void post(){}

    @Pointcut("execution(* com.sparta.lunchrecommender.domain.user.controller.*.*(..))")
    private void user(){}

    @Before("auth() || comment() || follow() || like() || post() || user()")
    public void beforeLogging(){
        //요청한 모든 속성 가져오기
        // HTTP 요청에 대한 메서드 사용하기 위해 RequestAttributes 객체로 캐스팅
        //ServletRequestAttributes 객체에서 HttpServletRequest 객체 얻기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("method 방식 : " +request.getMethod());
        log.info("URI 주소 : " + request.getRequestURI());
    }








}
