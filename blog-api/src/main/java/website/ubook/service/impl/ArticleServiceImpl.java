package website.ubook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ubook.dao.mapper.ArticleMapper;
import website.ubook.dao.pojo.Article;
import website.ubook.service.ArticleService;
import website.ubook.vo.ArticleVo;
import website.ubook.vo.Result;
import website.ubook.vo.params.PageParams;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 分页查询article数据库
     * @param pageParams page size
     * @return Result
     */
    @Override
    public Result listArticle(PageParams pageParams) {


        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());


        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //按照是否置顶进行排序
        //按照时间倒序进行排序
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        List<Article> records = articleMapper.selectPage(page, queryWrapper).getRecords();

        List<ArticleVo> articleVoList = copyList(records);

        return Result.success(articleVoList);

    }


    private List<ArticleVo> copyList(List<Article> records) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        //并不是所有的接口都需要【标签】、【作者】
        if (isTag == true) {
            Long id = article.getId();

            articleVo.setTags();
        }

        if (isAuthor == true) {
            articleVo.setAuthor();
        }
        return articleVo;
    }
}
