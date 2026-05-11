package com.fnk.common.db.handler;

/**
 * @author Enigma
 */
public class LongListTypeHandler extends BaseListToDbJsonTypeHandler<Long>{
    public LongListTypeHandler(Class<Long> type) {
        super(type);
    }
}
