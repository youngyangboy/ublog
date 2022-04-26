package website.ubook.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.ubook.dao.pojo.SysUser;
import website.ubook.utils.UserThreadLocal;
import website.ubook.vo.Result;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test() {

        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);

        return Result.success(null);
    }
}
