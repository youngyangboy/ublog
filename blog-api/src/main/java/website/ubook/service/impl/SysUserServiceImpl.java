package website.ubook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ubook.dao.mapper.SysUserMapper;
import website.ubook.dao.pojo.SysUser;
import website.ubook.service.SysUserService;

import javax.annotation.Resource;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 通过id找到SysUser对象信息
     * @param id
     * @return
     */
    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("None");
        }
        return sysUser;
    }
}
