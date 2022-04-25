package website.ubook.service;

import website.ubook.vo.Result;
import website.ubook.vo.params.LoginParam;

public interface LoginService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);
}
