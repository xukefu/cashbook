package com.xkf.cashbook.common.constant;

/**
 * @author xukf01
 */
public interface Constants {

    String PROJECT_CLBM_AGENT_WEB = "cashbook";

    /** 过期时间 */
    Integer LOGIN_AREA_EXPIRE_DAY = 7;
    Integer LOGIN_EXPIRE_DAY = 1;
    Integer VALIDATE_CODE_EXPIRE_MINUTES = 2;

    Integer EXPIRE_ROOM_MINUTES = 1;

    /** session key name */
    String KEY_TOKEN = "csrf_token";
    String KEY_PROMOTER_ID = "family";
    String KEY_NAME_CARD = "nameCard";

    /** redis key name prefix */
    String KEY_PREFIX_TOKEN_BLACKLIST = "cashbook:token:blacklist:";
    String KEY_PREFIX_TOKEN_EXPIRATION = "cashbook:agent:token:expiration:";
    String KEY_PREFIX_CACHE = "cashbook:cache:";
    String KEY_PREFIX_LOGIN = "cashbook:login:";
    String KEY_PREFIX_VALIDATE_CODE = "cashbook:validateCode:";
    String KEY_PREFIX_USER_STATUS = "cashbook:user:status:";

    String DOMAIN = "www.cashbook.top";

}
