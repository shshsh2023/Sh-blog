package com.sh.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sh.utils.PoetryUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DataAutoFill implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createBy", String.class, !StringUtils.hasText(PoetryUtil.getUsername()) ? "Sara" : PoetryUtil.getUsername());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updateBy", String.class, !StringUtils.hasText(PoetryUtil.getUsername()) ? "Sara" : PoetryUtil.getUsername());
    }
}
