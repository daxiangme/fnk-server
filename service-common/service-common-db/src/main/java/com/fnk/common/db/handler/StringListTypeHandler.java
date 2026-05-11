package com.fnk.common.db.handler;

/**
 * @author Enigma
 */
public class StringListTypeHandler extends BaseListToDbJsonTypeHandler<String>{
    public StringListTypeHandler(Class<String> type) {
        super(type);
    }
}
