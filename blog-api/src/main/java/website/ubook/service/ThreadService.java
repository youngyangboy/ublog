package website.ubook.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import website.ubook.dao.mapper.ArticleMapper;
import website.ubook.dao.pojo.Article;

@Service
public class ThreadService {

    //希望此操作在线程池中执行，不会影响原有的主线程
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        //为了在多线程情况下的线程安全 （乐观锁）
        updateWrapper.eq(Article::getViewCounts, viewCounts);

        //update article set view_count=100 where view_count=99 and id=11
        articleMapper.update(articleUpdate, updateWrapper);

        try {
            Thread.sleep(5000);
            System.out.println("更新完成了。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
