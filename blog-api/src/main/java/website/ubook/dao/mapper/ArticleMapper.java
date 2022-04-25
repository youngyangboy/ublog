package website.ubook.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import website.ubook.dao.dos.Archives;
import website.ubook.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();
}
