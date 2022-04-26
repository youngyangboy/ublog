package website.ubook.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.ubook.vo.Result;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test() {
        return Result.success(null);
    }
}
