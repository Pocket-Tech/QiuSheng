package org.pockettech.qiusheng.constant;

/**
 * 返回状态码
 */
public class ResultCode {

    /**
     * 操作成功
     */
    public static final int SUCCESS = 0;

    /**
     * 参数错误
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 访问受限或授权过期
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源，服务未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 405;

    /**
     * 系统内部错误
     */
    public static final int ERROR = 500;


    /**
     * 文件处理失败
     */
    public static final int FILE_PROCESSING_FAILED = 10002;

    /**
     * 签名校验失败
     */
    public static final int SIGN_VERIFY_FAILED = 10003;

    /**
     * 文件丢失
     */
    public static final int FILE_MISSING = 10004;

    /**
     * 登录失败
     */
    public static final int LOGIN_FAILED = 20000;

    /**
     * 用户名或密码错误
     */
    public static final int PASSWORD_ERROR = 20001;

    /**
     * Token解析失败，Token非法
     */
    public static final int ILLEGAL_TOKEN = 20002;

    /**
     * 登录过期
     */
    public static final int LOGIN_EXPIRED = 20003;

    /**
     * 账户被锁定
     */
    public static final int ACCOUNT_LOCKED = 20004;

    /**
     * 未知错误
     */
    public static final int UnknownError = 10001;

}
