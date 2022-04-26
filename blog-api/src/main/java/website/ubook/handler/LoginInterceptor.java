package website.ubook.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import website.ubook.dao.pojo.SysUser;
import website.ubook.service.LoginService;
import website.ubook.vo.ErrorCode;
import website.ubook.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 在执行controller方法（Handler）之前执行
         * 1. 需要判断请求的接口路径是否为HandlerMethod（controller方法）
         * 2. 判断token是否为空，如果为空则表示未登录
         * 3. 如果token不为空，登陆验证loginService checkToken
         * 4. 如果认证成功就放行
         */

        if (!(handler instanceof HandlerMethod)) {
            // handler 可能是RequestResourceHandler springboot程序，访问静态资源
            // 默认去classpath下的static目录去查询
            return true;
        }

        String token = request.getHeader("Authorization");

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");
        // 如果token为空，则返回未登录
        if (StringUtils.isBlank(token)) {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //如果用户不存在，返回未登录
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null) {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_PERMISSION.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        //登录验证成功，放行

        //希望在controller中直接获取用户信息 怎么获取？
        return true;
    }
}
