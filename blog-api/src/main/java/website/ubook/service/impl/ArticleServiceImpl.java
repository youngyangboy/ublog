package website.ubook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.ubook.dao.dos.Archives;
import website.ubook.dao.mapper.ArticleBodyMapper;
import website.ubook.dao.mapper.ArticleMapper;
import website.ubook.dao.mapper.ArticleTagMapper;
import website.ubook.dao.pojo.Article;
import website.ubook.dao.pojo.ArticleBody;
import website.ubook.dao.pojo.ArticleTag;
import website.ubook.dao.pojo.SysUser;
import website.ubook.service.*;
import website.ubook.utils.UserThreadLocal;
import website.ubook.vo.ArticleBodyVo;
import website.ubook.vo.ArticleVo;
import website.ubook.vo.Result;
import website.ubook.vo.TagVo;
import website.ubook.vo.params.ArticleParam;
import website.ubook.vo.params.PageParams;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagService tagService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleBodyMapper articleBodyMapper;

    @Resource
    private ThreadService threadService;

    @Resource
    private ArticleTagMapper articleTagMapper;


    /**
     * 分页查询article数据库，返回ArticleVo列表对象，封装在Result中
     *
     * @param pageParams {page,size}
     * @return Result
     */

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage =
                this.articleMapper.listArticle(page,
                        pageParams.getCategoryId(),
                        pageParams.getTagId(),
                        pageParams.getYear(),
                        pageParams.getMonth());

        return Result.success(copyList(articleIPage.getRecords(), true, true));
    }
//    @Override
//    public Result listArticle(PageParams pageParams) {
//
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//
//        //按照是否置顶进行排序，按照时间倒序进行排序
//        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
//
//        if (pageParams.getCategoryId() != null) {
//            // 相当于查询时添加了条件：and category_id = #{categoryId}
//            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
//        }
//
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null) {
//            /*  加入标签条件查询
//                Article表中并没有tag字段，并且一张文章有多个标签，是一对多的关系
//                ArticleTag表  article_id(1) : tag_id(n)
//             */
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0) {
//                //相当于添加查询条件：and id in（1，2，3）
//                queryWrapper.in(Article::getId, articleIdList);
//            }
//        }
//
//
//        List<Article> records = articleMapper.selectPage(page, queryWrapper).getRecords();
//
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//
//        return Result.success(articleVoList);
//
//    }


    /**
     * 最热文章
     *
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
        return Result.success(copyList(articles, false, false));
    }

    /**
     * 最新文章
     *
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
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();

        return Result.success(archivesList);
    }


    @Override
    public Result findArticleById(Long articleId) {

        /**
         * 1. 根据文章id查询文章信息
         * 2. 根据bodyId和categoryId去做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);

        ArticleVo articleVo = copy(article, true, true, true, true);

        /*
        查看完文章，新增阅读数量存在的问题：
        1. 查看完文章后，本应该直接返回数据，这是需要做一个更新操作，更新时加写锁，阻塞其他的读操作，性能会比较低
        2. 更新增加了此次接口的耗时，如果一旦更新出问题，不能影响查看文章的操作
        解决方法：可以使用线程池，把更新操作扔到线程池中去执行，这样就和主线程不相关了
         */
        threadService.updateArticleViewCount(articleMapper, article);

        return Result.success(articleVo);
    }


//    @Transactional
//    public Result publish(ArticleParam articleParam) {
//        SysUser sysUser = UserThreadLocal.get();
//
//        Article article = new Article();
//        article.setAuthorId(sysUser.getId());
//        article.setCategoryId(articleParam.getCategory().getId());
//        article.setCreateDate(System.currentTimeMillis());
//        article.setCommentCounts(0);
//        article.setSummary(articleParam.getSummary());
//        article.setTitle(articleParam.getTitle());
//        article.setViewCounts(0);
//        article.setWeight(Article.Article_Common);
//        article.setBodyId(-1L);
//        this.articleMapper.insert(article);
//
//        //tags
//        List<TagVo> tags = articleParam.getTags();
//        if (tags != null) {
//            for (TagVo tag : tags) {
//                ArticleTag articleTag = new ArticleTag();
//                articleTag.setArticleId(article.getId());
//                articleTag.setTagId(tag.getId());
//                this.articleTagMapper.insert(articleTag);
//            }
//        }
//        ArticleBody articleBody = new ArticleBody();
//        articleBody.setContent(articleParam.getBody().getContent());
//        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
//        articleBody.setArticleId(article.getId());
//        articleBodyMapper.insert(articleBody);
//
//        article.setBodyId(articleBody.getId());
//        articleMapper.updateById(article);
//        ArticleVo articleVo = new ArticleVo();
//        articleVo.setId(article.getId());
//        return Result.success(articleVo);
//    }

    /**
     * 文章发布
     *
     * @param articleParam
     * @return
     */
    @Transactional
    @Override
    public Result publish(ArticleParam articleParam) {
        /**
         * 1. 发布文章，目的时构建Article对象
         * 2. 作者id 当前的登录用户 使用TheadLocal
         * 3. 标签  将标签加入到关联列表中
         * 4. 内容存储
         */

        // 此接口要加入登录拦截当中
        SysUser sysUser = UserThreadLocal.get();

        Article article = new Article();
        article.setAuthorId(sysUser.getId());

        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(articleParam.getCategory().getId());
        // 插入文章之后数据库会生成一个文章id，供后面使用
        this.articleMapper.insert(article);

        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }

        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        //先插入数据库，才会产生id
        article.setBodyId(articleBody.getId());

        articleMapper.updateById(article);

        HashMap<String, String> map = new HashMap<>();

        map.put("id", article.getId().toString());

        return Result.success(map);

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
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }


    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {

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

        if (isBody == true) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));

        }

        if (isCategory == true) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }

        return articleVo;
    }


    private ArticleBodyVo findArticleBodyById(Long bodyId) {


        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());


        return articleBodyVo;
    }


}
