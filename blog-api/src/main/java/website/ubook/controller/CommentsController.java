package website.ubook.controller;


import org.springframework.web.bind.annotation.*;
import website.ubook.service.CommentsService;
import website.ubook.vo.Result;
import website.ubook.vo.params.CommentParam;

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

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam) {
        return commentsService.comment(commentParam);
    }
}
