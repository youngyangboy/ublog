package website.ubook.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import website.ubook.vo.Result;


/**
 * 对加了Controller注解的方法进行拦截助理 AOP的实现
 */
@ControllerAdvice
public class AllExceptionHandler {

    //进行异常处理,处理Exception.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public Result doException(Exception ex) {
        ex.printStackTrace();
        return Result.fail(-999, "系统异常");
    }

}
