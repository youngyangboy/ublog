package website.ubook.service;

import website.ubook.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagByArticleId(Long articleId);
}
