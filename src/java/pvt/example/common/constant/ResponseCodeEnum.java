package pvt.example.common.constant;

/**
 * 信息：src/java/pvt/example/common/constant/ResponseCodeEnum.java
 * <p>日期：2025/7/26
 * <p>描述：所有响应状态码 枚举类 ResponseCodeEnum
 */
public enum ResponseCodeEnum {
    CODE_200(200, "请求成功"),
    CODE_404(404, "请求地址不存在"),
    CODE_500(500, "服务器返回错误，请联系管理员"),
    CODE_600(600, "请求参数错误"),
    CODE_601(601, "信息已经存在"),
    CODE_900(900, "http请求超时"),
    CODE_901(901, "登录超时，请重新登录");
    private final Integer code;
    private final String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() { return code;}

    public String getMsg() {return msg;}
}
