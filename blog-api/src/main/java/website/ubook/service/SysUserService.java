package website.ubook.service;

import website.ubook.dao.pojo.SysUser;

public interface SysUserService {

    SysUser findUserById(Long id);


    SysUser findUser(String account, String password);
}
