package website.ubook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import website.ubook.dao.mapper.TagMapper;
import website.ubook.dao.pojo.Tag;
import website.ubook.service.TagService;
import website.ubook.vo.Result;
import website.ubook.vo.TagVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;


    /**
     * 把Tag对象转换成前端需要的json对象格式
     * @param tag
     * @return
     */
    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    /**
     * 通过articleId获取该文章的标签
     * @param articleId
     * @return
     */
    @Override
    public List<TagVo> findTagByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }


    /**
     * 1. 标签所拥有的文章数量最多
     * 2. 查询根据tag_id分组计数，从大到小排列，取前limit个
     * select count(*) as count,tag_id from 'ms_article_tag' group by tag_id order by count desc 2
     * select tag_id form 'ms_article_tag' group by tag_id order by count(*) desc limit
     * @param limit
     * @return
     */
    @Override
    public Result hots(int limit) {
        List<Long> tagIds = tagMapper.findHotsTagId(limit);

        if (CollectionUtils.isEmpty(tagIds)) {
            return Result.success(Collections.emptyList());
        }

        // select * from tag where id in (1,2,3,4)
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    /**
     * 查询所有的文章标签
     * @return
     */
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }



    @Override
    public Result findDetailById(Long id) {

        Tag tag = tagMapper.selectById(id);

        return Result.success(copy(tag));
    }


}
