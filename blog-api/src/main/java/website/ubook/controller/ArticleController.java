package website.ubook.controller;

import org.springframework.web.bind.annotation.*;
import website.ubook.common.aop.LogAnnotation;
import website.ubook.common.cache.Cache;
import website.ubook.service.ArticleService;
import website.ubook.vo.Result;
import website.ubook.vo.params.ArticleParam;
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
    // 加上此注解，表示要对此接口记录日志
    @LogAnnotation(module="文章",operator="获取文章列表")
    @PostMapping()
    @Cache(expire = 5 * 60 * 1000, name = "listArticle")//分页做缓存处理，存储5分钟
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }


    /**
     * 首页 最热文章
     *
     * @return
     */

    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000, name = "hot_article")//最热文章做缓存处理，存储5分钟
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000, name = "news_article")//最新文章做缓存处理，存储5分钟
    public Result newArticle() {
        int limit = 5;
        return articleService.newArticles(limit);
    }


    /**
     * 文章归档
     * @return
     */

    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }


    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);

    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
