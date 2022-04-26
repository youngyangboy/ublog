package website.ubook.service;

import website.ubook.vo.CategoryVo;
import website.ubook.vo.Result;

public interface CategoryService {
    /**
     * 通过categoryId找到类别
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result findAllDetailById(Long id);


}
