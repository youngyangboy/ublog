package website.ubook.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.ubook.service.LoginService;
import website.ubook.vo.Result;
import website.ubook.vo.params.LoginParam;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping()
    public Result login(@RequestBody LoginParam loginParam) {
        //登录 需要验证用户，需要访问用户表，但是
        return loginService.login(loginParam);
    }

}
