package website.ubook.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.ubook.service.ArticleService;
import website.ubook.vo.Result;
import website.ubook.vo.params.PageParams;

import javax.annotation.Resource;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping()
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }
}
