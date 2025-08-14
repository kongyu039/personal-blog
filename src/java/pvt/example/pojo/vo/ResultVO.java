package pvt.example.pojo.vo;

import pvt.example.common.constant.ResponseCodeEnum;

/**
 * 信息：src/java/pvt/example/common/vo/ResultVO.java
 * <p>日期：2025/7/26
 * <p>类&emsp;&emsp;名：ResultVO
 * <p>描&emsp;&emsp;述：基础 ResultVO 视图对象结果封装
 */
public class ResultVO<T> {
    private String status;
    private Integer code;
    private String msg;
    private T data;

    public ResultVO() { }

    public ResultVO(T data) {
        this.data = data;
        this.code = ResponseCodeEnum.CODE_200.getCode();
        this.msg = ResponseCodeEnum.CODE_200.getMsg();
    }

    public ResultVO(ResponseCodeEnum rce, T data) {
        this.data = data;
        this.code = rce.getCode();
        this.msg = rce.getMsg();
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Integer getCode() { return code; }

    public void setCode(Integer code) { this.code = code; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }

    public T getData() { return data; }

    public void setData(T data) { this.data = data; }
}