package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.budget.BudgetLimit;
import pl.xsware.domain.model.category.Category;
import pl.xsware.domain.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Category>> getAll(@PathVariable Long userId) {
        List<Category> list = categoryService.getAllCategory(userId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addCategory(@RequestBody @Valid Category data) {
        categoryService.addCategory(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/remove")
    public ResponseEntity<Response> removeCategory(@RequestBody @Valid Category data) {
        categoryService.removeCategory(data);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/edit")
    public ResponseEntity<Response> editCategory(@RequestBody @Valid Category data) {
        categoryService.editCategory(data);
        return ResponseEntity.ok(Response.create("OK"));
    }
}
