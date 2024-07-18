package com.sh.aop;

import com.sh.config.PoetryResult;
import com.sh.constants.CommonConst;
import com.sh.entity.User;
import com.sh.enums.CodeMsg;
import com.sh.enums.PoetryEnum;
import com.sh.handle.PoetryLoginException;
import com.sh.handle.PoetryRuntimeException;
import com.sh.utils.PoetryUtil;
import com.sh.utils.cache.PoetryCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Aspect
@Component
@Order(0)
@Slf4j
public class LoginCheckAspect {

    @Around("@annotation(loginCheck)")
    public Object around(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {

        String token = PoetryUtil.getToken();
        if (!StringUtils.hasText(token)) {
            throw new PoetryLoginException(CodeMsg.NOT_LOGIN.getMsg());
        }

        User user = (User) PoetryCache.get(token);
        log.info(String.valueOf(user));
        log.info(String.valueOf(loginCheck.value()));

        if (user == null) {
            throw new PoetryLoginException(CodeMsg.LOGIN_EXPIRED.getMsg());
        }

        if (token.contains(CommonConst.USER_ACCESS_TOKEN)) {
            if (loginCheck.value() == PoetryEnum.USER_TYPE_ADMIN.getCode() || loginCheck.value() == PoetryEnum.USER_TYPE_DEV.getCode()) {
                return PoetryResult.fail("请输入管理员账号！");
            }
        } else if (token.contains(CommonConst.ADMIN_ACCESS_TOKEN)) {
            log.info("请求IP：" + PoetryUtil.getIpAddr(PoetryUtil.getRequest()));
            //超级管理员才有权限
            if (loginCheck.value() == PoetryEnum.USER_TYPE_ADMIN.getCode() && user.getUserType() != CommonConst.ADMIN_USER_ID) {
                return PoetryResult.fail("请输入管理员账号！");
            }
        } else {
            throw new PoetryLoginException(CodeMsg.NOT_LOGIN.getMsg());
        }

        if (loginCheck.value() < user.getUserType()) {
            throw new PoetryRuntimeException("权限不足！");
        }

        //重置过期时间
        String userId = user.getId().toString();
        boolean flag1 = false;
        if (token.contains(CommonConst.USER_ACCESS_TOKEN)) {
            flag1 = PoetryCache.get(CommonConst.USER_TOKEN_INTERVAL + userId) == null;
        } else if (token.contains(CommonConst.ADMIN_ACCESS_TOKEN)) {
            flag1 = PoetryCache.get(CommonConst.ADMIN_TOKEN_INTERVAL + userId) == null;
        }

        if (flag1) {
            synchronized (userId.intern()) {
                boolean flag2 = false;
                if (token.contains(CommonConst.USER_ACCESS_TOKEN)) {
                    flag2 = PoetryCache.get(CommonConst.USER_TOKEN_INTERVAL + userId) == null;
                } else if (token.contains(CommonConst.ADMIN_ACCESS_TOKEN)) {
                    flag2 = PoetryCache.get(CommonConst.ADMIN_TOKEN_INTERVAL + userId) == null;
                }

                if (flag2) {
                    PoetryCache.put(token, user, CommonConst.TOKEN_EXPIRE);
                    if (token.contains(CommonConst.USER_ACCESS_TOKEN)) {
                        PoetryCache.put(CommonConst.USER_TOKEN + userId, token, CommonConst.TOKEN_EXPIRE);
                        PoetryCache.put(CommonConst.USER_TOKEN_INTERVAL + userId, token, CommonConst.TOKEN_INTERVAL);
                    } else if (token.contains(CommonConst.ADMIN_ACCESS_TOKEN)) {
                        PoetryCache.put(CommonConst.ADMIN_TOKEN + userId, token, CommonConst.TOKEN_EXPIRE);
                        PoetryCache.put(CommonConst.ADMIN_TOKEN_INTERVAL + userId, token, CommonConst.TOKEN_INTERVAL);
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
