package website.ubook.service;


import website.ubook.vo.Result;
import website.ubook.vo.params.PageParams;


public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);
}
