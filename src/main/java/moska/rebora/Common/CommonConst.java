package moska.rebora.Common;

public class CommonConst {
    public static final String LOCALHOST = "http://localhost:8080";
    //public static final String DEV_SERVER = "http://13.125.27.234:8080";
    public static final String DEV_SERVER = "http://dev.re-bora.com";
    public static final String PRD_SERVER = "https://m.re-bora.com";
    public static final String CURRENT_SERVER = DEV_SERVER;

    public static final Integer DEFAULT_PAGE_SIZE = 10;

    public static final String LOC_NAVER_CALLBACK = "http://localhost:8080/api/user/oath/naverCallback";
    public static final String PRD_NAVER_CALLBACK = "https://m.re-bora.com/api/user/oath/naverCallback";
    public static final String CURRENT_NAVER_CALLBACK = LOC_NAVER_CALLBACK;

    public static final String NAVER_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";

    public static final String NAVER_TOKEN_ME_URL = "https://openapi.naver.com/v1/nid/me";

    public static final String KAKAO_TOKEN_ME_URL = "https://kapi.kakao.com/v2/user/me";
}
