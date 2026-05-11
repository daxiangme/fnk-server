package com.fnk.starter.web.interf;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fnk.starter.web.serializer.RequestEnumJsonDeserialize;

import java.io.Serializable;

/**
 * @author Enigma
 */
@JsonDeserialize(using = RequestEnumJsonDeserialize.class)
public interface IBaseEnum<T extends Serializable> extends IEnum<T> {
}
