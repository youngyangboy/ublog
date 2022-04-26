package website.ubook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.ubook.service.CategoryService;
import website.ubook.vo.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    //categorys
    @GetMapping()
    public Result categories() {
        return categoryService.findAll();
    }

    //categorys
    @GetMapping("detail")
    public Result categoriesDetail() {
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable Long id) {
        return categoryService.findAllDetailById(id);
    }


}
