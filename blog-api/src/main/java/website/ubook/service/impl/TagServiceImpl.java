package website.ubook.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ubook.dao.mapper.TagMapper;
import website.ubook.dao.pojo.Tag;
import website.ubook.service.TagService;
import website.ubook.vo.TagVo;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    @Override
    public List<TagVo> findTagByArticleId(Long articleId) {

        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);

        return copyList(tags);
    }


}
