package website.ubook.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.ubook.service.CommentsService;
import website.ubook.vo.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Resource
    private CommentsService commentsService;

    //  /comments/article/{id}
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id) {

        return commentsService.commentsByArticleId(id);
    }

}
