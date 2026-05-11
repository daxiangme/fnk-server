package com.fnk.starter.web.interf;

import java.util.List;

/**
 * 不进行匹配的路径
 *
 * @author Enigma
 */
public interface ApiInterceptors {
    /**
     * 不进行匹配的路径
     *
     * @return 路径列表
     */
    List<String> notMatches();
}
