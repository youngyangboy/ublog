package website.ubook.vo.params;

import lombok.Data;
import website.ubook.vo.CategoryVo;
import website.ubook.vo.TagVo;

import java.util.List;

@Data
public class ArticleParam {
    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

}
