package com.sh.config;

import com.alibaba.fastjson.JSON;
import com.sh.constants.CommonConst;
import com.sh.entity.WebInfo;
import com.sh.enums.CodeMsg;
import com.sh.utils.cache.PoetryCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;



public class WebInfoHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebInfo webInfo = (WebInfo) PoetryCache.get(CommonConst.WEB_INFO);
        if (webInfo == null || !webInfo.getStatus()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(PoetryResult.fail(CodeMsg.SYSTEM_REPAIR.getCode(), CodeMsg.SYSTEM_REPAIR.getMsg())));
            return false;
        } else {
            return true;
        }
    }
}
