package com.fnk.starter.web.serializer;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fnk.starter.web.interf.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;

/**
 * 请求枚举JSON反序列化
 * @author Enigma
 */
@NoArgsConstructor
@AllArgsConstructor
public class RequestEnumJsonDeserialize  extends JsonDeserializer<IBaseEnum<?>> implements ContextualDeserializer {
    private JavaType type;

    @Override
    public IBaseEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this.type == null || !this.type.isEnumType()) {
            return null;
        }
        ObjectCodec oc = p.getCodec();
        JsonNode node = oc.readTree(p);

        if (node == null) {
            return null;
        }
        Serializable nodeVal;
        if (node instanceof IntNode) {
            nodeVal = node.asInt();
        } else if (node instanceof TextNode) {
            nodeVal = node.asText();
        } else {
            return null;
        }
        String nodeValString = ObjectUtil.toString(nodeVal);
        Object[] enumConstants = this.type.getRawClass().getEnumConstants();
        for (Object e : enumConstants) {
            if (e instanceof IBaseEnum<?> ie) {
                if (ObjectUtil.toString(ie.getValue()).equals(nodeValString)) {
                    return ie;
                }
            }
        }
        return null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        JavaType type = ctxt.getContextualType() != null
                ? ctxt.getContextualType()
                : property.getMember().getType();
        return new RequestEnumJsonDeserialize(type);
    }

}
