package website.ubook.service;

import website.ubook.vo.CategoryVo;

import java.util.List;

public interface CategoryService {
    /**
     * 通过categoryId找到类别
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);

}
