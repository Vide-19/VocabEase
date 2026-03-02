package com.javastudy.vocabease_common.entity.constants;

public class Constants {
    public static final String CHECK_CODE_KEY = "check_code_key";
    public static final String SESSION_KEY = "session_key";
    public static final String REDIS_KEY_CHECK_CODE = "vocabease:check:";
    public static final String JWT_KEY_LOGIN_TOKEN = "jwt_key_login_token";
    public static final Integer JWT_TOKEN_EXPIRE = 60 * 60 * 24 * 7;
    public static final String APP_UPDATE_FOLDER = "/app/";
    public static final String READ_IMG_PATH = "/api/file/getImage/";
    public static final String AVATAR_FOLDER = "avatar/";
    public static final Integer LENGTH_8 = 8;
    public static final Integer LENGTH_10 = 10;
    public static final Integer LENGTH_20 = 20;
    public static final Integer LENGTH_30 = 30;
    public static final Integer LENGTH_50 = 50;
    public static final Integer LENGTH_100 = 100;
    public static final Integer LENGTH_150 = 150;
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TRUE = "正确";
    public static final String FALSE = "错误";
    public static final String[] LETTER = new String[] {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    public static final String[] EXCEL_TITLE_ARTICLE = new String[] {"标题", "正文", "翻译", "难度", "分类"};
    public static final String[] EXCEL_TITLE_WORD = new String[] {"单词", "音标", "词性", "释义", "例句", "难度", "分类"};
    public static final String[] EXCEL_TITLE_QUESTION = new String[] {"标题", "问题描述", "问题选项", "答案解析", "问题类型", "难度", "分类"};
    public static final int ORDER_CORS = -102;

}
