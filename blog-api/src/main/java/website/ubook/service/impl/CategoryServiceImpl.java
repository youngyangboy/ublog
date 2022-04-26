package website.ubook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ubook.dao.mapper.CategoryMapper;
import website.ubook.dao.pojo.Category;
import website.ubook.service.CategoryService;
import website.ubook.vo.CategoryVo;
import website.ubook.vo.Result;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {

        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);

        return categoryVo;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId, Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //转换成和页面交互的对象然后返回
        return Result.success(copyList(categories));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //转换成和页面交互的对象然后返回
        return Result.success(copyList(categories));
    }

    private List<CategoryVo> copyList(List<Category> categories) {
        List<CategoryVo> categoryList = new ArrayList<>();
        for (Category category : categories) {
            categoryList.add(copy(category));
        }
        return categoryList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
