package com.fnk.common.bean.consts;

/**
 * 正则表达式常量定义
 * @author Enigma
 */
public class RegexConst {
    public final static String PHONE = "^(?:(?:\\+|00)86)?1\\d{10}$";

    public final static String PASSWORD = "[a-zA-Z\\d#@!~%^&\\.*]{6,18}";

    public final static String CARD_NUMBER = "^[1-9][0-9]{5}(18|19|20)[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{3}([0-9]|(X|x))";

}
