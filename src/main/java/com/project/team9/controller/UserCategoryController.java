package com.project.team9.controller;

import com.project.team9.model.buissness.UserCategory;
import com.project.team9.service.UserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "category")
public class UserCategoryController {

    private final UserCategoryService service;

    @Autowired
    public UserCategoryController(UserCategoryService service) {
        this.service = service;
    }

    @GetMapping("/client")
    public List<UserCategory> getAllClientCategories(){
        return service.getAllClientCategories();
    }
    @GetMapping("/vendor")
    public List<UserCategory> getAllVendorCategories(){
        return service.getAllVendorCategories();
    }

    @PostMapping("/add")
    public Long addUserCategory(@RequestBody UserCategory userCategory) {
        return service.addUserCategory(userCategory);
    }

    @PostMapping("/delete/{id}")
    public Long deleteCategory(@PathVariable Long id) {
        return service.deleteUserCategory(id);
    }
}
