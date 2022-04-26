package website.ubook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.ubook.service.TagService;
import website.ubook.vo.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("tags")
public class TagsController {

    @Resource
    private TagService tagService;

    @GetMapping("hot")
    public Result hot() {
        int limit = 6;
        return tagService.hots(limit);
    }

    @GetMapping()
    public Result findAll() {
        return tagService.findAll();
    }

}
