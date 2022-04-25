package website.ubook.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import website.ubook.dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> findTagsByArticleId(Long articleId);

    List<Long> findHotsTagId(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
