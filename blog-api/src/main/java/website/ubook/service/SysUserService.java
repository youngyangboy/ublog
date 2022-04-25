package website.ubook.service;

import website.ubook.dao.pojo.SysUser;
import website.ubook.vo.Result;

public interface SysUserService {

    SysUser findUserById(Long id);


    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);
}
