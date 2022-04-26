package website.ubook.service;

import org.springframework.transaction.annotation.Transactional;
import website.ubook.dao.pojo.SysUser;
import website.ubook.vo.Result;
import website.ubook.vo.params.LoginParam;

@Transactional
public interface LoginService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);


    /**
     * 注册
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);

}
