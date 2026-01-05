package com.javastudy.vocabease_common.utils;

import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.enums.VerifyRegexEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {
    public static boolean verify(String regex, String value) {
        if (StringTools.isEmpty(value))
            return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean verify(VerifyRegexEnum regex, String value) {
        return verify(regex.getRegex(), value);
    }

    public static boolean checkAccount(String account) {
        if (StringTools.isEmpty(account) || account.length() < Constants.LENGTH_8 || account.length() > Constants.LENGTH_20)
            return false;
        return verify(VerifyRegexEnum.ACCOUNT, account);
    }

    public static boolean checkPassword(String password) {
        return verify(VerifyRegexEnum.PASSWORD, password);
    }
}
