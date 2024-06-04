package com.cafe.controller;

import com.cafe.constants.CafeConstants;
import com.cafe.entity.Category;
import com.cafe.service.CategoryService;
import com.cafe.utils.CafeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Operation(
            summary = "Add a new category",
            description = "Endpoint to add a new category."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody(required = true) Map<String, String> requestMap){
        try{
            return categoryService.addCategory(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "get all category",
            description = "Endpoint to get all category."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get")
    public ResponseEntity<List<Category>> getAllCategory(@RequestBody(required = false) String filterValue){
        try{
            return categoryService.getAllCategory(filterValue);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "update category",
            description = "Endpoint to update category."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap){
        try{
            return categoryService.updateCategory(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
