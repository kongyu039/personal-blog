package pvt.example.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pvt.example.pojo.vo.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 信息：src/java/pvt/example/common/handler/GlobalExceptionHandler.java
 * <p>日期：2025/8/1
 * <p>描述：全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理@RequestParam/@PathVariable的验证失败（ConstraintViolationException）
     * @param ex 约束违规异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVO<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        // 解析请求参数的验证错误
        ex.getConstraintViolations().forEach(violation -> {
            // 提取参数名（格式可能是"方法名.参数名"，需要截取）
            String paramName = violation.getPropertyPath().toString();
            if (paramName.contains(".")) {
                paramName = paramName.split("\\.")[1]; // 截取参数名部分
            }
            String errorMsg = violation.getMessage();
            errors.put(paramName, errorMsg);
        });
        logger.debug("ConstraintViolationException.class @RequestParam/@PathVariable异常");
        return new ResultVO<>(errors);
    }

    /**
     * 处理@RequestBody的验证失败（MethodArgumentNotValidException）
     * @param ex 方法参数不有效异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // 解析请求体的字段错误
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(fieldName, errorMsg);
        });
        logger.debug("MethodArgumentNotValidException.class @RequestBody异常");
        return new ResultVO<>(errors);
    }

    /**
     * 处理所有其他异常
     * @param ex 异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVO<String>> handleGlobalExceptions(Exception ex, HttpServletRequest request) {
        logger.debug("Exception.class 总异常", ex);
        // 这里返回一个通用的错误信息，可以记录异常，发送通知等
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ResultVO<>("发生意外错误: " + ex.getMessage()));
    }
}
