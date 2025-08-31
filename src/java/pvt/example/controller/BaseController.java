package pvt.example.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pvt.example.common.constant.ResponseCodeEnum;
import pvt.example.pojo.vo.ResultVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 信息：src/java/pvt/example/controller/BaseController.java
 * <p>日期：2025/7/26
 * <p>描述：BaseController | 所有 Controller 的基础父类
 */
public class BaseController {
    protected static final String STATUS_SUCCESS = "success";
    protected static final String STATUS_ERROR = "error";
    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;
    @Resource
    protected HttpSession session;

    /** 封装返回成功对象 数据为 data */
    protected <T> ResultVO<T> successResultVO(T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setStatus(STATUS_SUCCESS);
        resultVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        resultVO.setMsg(ResponseCodeEnum.CODE_200.getMsg());
        resultVO.setData(data);
        return resultVO;
    }

    /** 封装返回成功对象 数据为 null */
    protected <T> ResultVO<T> successResultVO() { return successResultVO(null); }

    /** 独立获取 ServletRequest */
    protected ServletRequestAttributes getServletRequest() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    /** 独立获取 HttpSession */
    protected HttpSession getHttpSession() { return this.getServletRequest().getRequest().getSession(); }
}
