package com.fnk.starter.data.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Enigma
 */
@Component
@Slf4j
public class MybatisPlusAutoField implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = metaObject.getValue("createTime");
        if(createTime == null){
            this.strictInsertFill(metaObject,"createTime", Date.class,new Date());
        }
        this.strictInsertFill(metaObject,"updateTime", Date.class,new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime", Date.class,new Date());
    }

}
