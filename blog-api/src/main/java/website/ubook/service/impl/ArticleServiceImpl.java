package website.ubook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import website.ubook.dao.mapper.ArticleMapper;
import website.ubook.dao.pojo.Article;
import website.ubook.service.ArticleService;
import website.ubook.service.SysUserService;
import website.ubook.service.TagService;
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

    @Resource
    private TagService tagService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 分页查询article数据库，返回ArticleVo列表对象，封装在Result中
     * @param pageParams {page,size}
     * @return Result
     */
    @Override
    public Result listArticle(PageParams pageParams) {

        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        //按照是否置顶进行排序，按照时间倒序进行排序
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);

        List<Article> records = articleMapper.selectPage(page, queryWrapper).getRecords();

        List<ArticleVo> articleVoList = copyList(records, true, true);

        return Result.success(articleVoList);

    }


    /**
     * 最热文章
     * @param limit
     * @return
     */
    @Override
    public Result hotArticle(int limit) {


        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getAuthorId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        // select id,title from article order by viewCount desc limit 5;
        List<Article> articles = articleMapper.selectList(queryWrapper);

        //返回articleVo列表，不需要tag和author
        return Result.success(copyList(articles,false,false));
    }

    /**
     * 最新文章
     * @param limit
     * @return
     */
    @Override
    public Result newArticles(int limit) {


        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getAuthorId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        // select id,title from article order by createDate desc limit 5;
        List<Article> articles = articleMapper.selectList(queryWrapper);

        //返回articleVo列表，不需要tag和author
        return Result.success(copyList(articles,false,false));
    }

    /**
     * 下面两个方法的功能是根据是否需要Tag和Author将Article对象转换成前端需要的ArticleVo对象
     *
     * @param records  Article文章列表
     * @param isTag    是否需要Tag标签
     * @param isAuthor 是否需要作者信息
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口都需要【标签】、【作者】
        if (isTag == true) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagByArticleId(articleId));
        }
        if (isAuthor == true) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        return articleVo;
    }


}
