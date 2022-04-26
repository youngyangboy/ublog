package website.ubook.service;

import website.ubook.vo.Result;
import website.ubook.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagByArticleId(Long articleId);

    Result hots(int limit);

    /**
     * 查询所有的文章标签
     * @return
     */
    Result findAll();

}
