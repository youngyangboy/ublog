package website.ubook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ubook.dao.mapper.SysUserMapper;
import website.ubook.dao.pojo.SysUser;
import website.ubook.service.LoginService;
import website.ubook.service.SysUserService;
import website.ubook.vo.ErrorCode;
import website.ubook.vo.LoginUserVo;
import website.ubook.vo.Result;

import javax.annotation.Resource;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;


    /**
     * 通过id找到SysUser对象信息
     *
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

    @Override
    public SysUser findUser(String account, String password) {

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }


    @Override
    public Result findUserByToken(String token) {

        /**
         * 1. token合法性校验
         *      是否为空，解析是否成功，redis是否存在
         * 2. 如果校验失败 返回错误
         * 3. 如果成功，返回对应结果 LoginVo
         */


        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null) {
            return Result.fail(ErrorCode.TOKEN_ILLEGAL.getCode(), ErrorCode.TOKEN_ILLEGAL.getMsg());
        }

        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());

        return Result.success(loginUserVo);
    }
}
