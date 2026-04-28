package com.bloghub.service;

import com.bloghub.dto.CategoryRequestDto;
import com.bloghub.dto.CategoryResponseDto;
import com.bloghub.dto.CategoryUpdateDto;
import com.bloghub.entity.Category;
import com.bloghub.exception.ResourceAlreadyExitsException;
import com.bloghub.exception.ResourceNotFoundException;
import com.bloghub.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {


    private CategoryRepository cRepo;

    @Autowired//Constructor Injection
    public CategoryService(CategoryRepository cRepo) {
        this.cRepo = cRepo;
    }

    public CategoryResponseDto createCategory(CategoryRequestDto catReq) {
        if (cRepo.existsByCatName(catReq.getCatName())) {//checking if category with same name already exists
            throw new ResourceAlreadyExitsException("Category with name " + catReq.getCatName() + " already exists.");//if exists, throw exception
        }
        Category newCategory = new Category();//creating new category entity
        newCategory.setCatName(catReq.getCatName());//setting category name
        newCategory.setDescr(catReq.getDescr());//setting category description
        Category savedCategory = cRepo.save(newCategory);//saving category to repository
        return new CategoryResponseDto(savedCategory.getId(), savedCategory.getCatName(), savedCategory.getDescr());//returning response dto

    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = cRepo.findAll();//fetching all categories from repository
        List<CategoryResponseDto> categoryDtos = categories.stream()//streaming the list of category entities
                .map(cat -> new CategoryResponseDto(cat.getId(), cat.getCatName(), cat.getDescr()))//mapping each category entity to response dto
                .toList();//collecting the mapped dtos into a list
        return categoryDtos;
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = cRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found."));
        return new CategoryResponseDto(category.getId(), category.getCatName(), category.getDescr());//returning response dto
    }

    public CategoryResponseDto updateCategory(Long id, CategoryUpdateDto catUpd) {
        Category category = cRepo.findById(id)

                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found."));
        if (catUpd == null || (catUpd.getCatName() == null && catUpd.getDescr() == null)) {//validating that at least one field is provided for update
            throw new IllegalArgumentException("At least one field (catName or descr) must be provided for update.");//if not, throw exception
        }
        if (catUpd.getCatName() != null) {//updating category name if provided
            category.setCatName(catUpd.getCatName());
        }
        if (catUpd.getDescr() != null) {//updating category description if provided
            category.setDescr(catUpd.getDescr());
        }
        Category updatedCategory = cRepo.save(category);//saving updated category to repository
        return new CategoryResponseDto(updatedCategory.getId(), updatedCategory.getCatName(), updatedCategory.getDescr());//returning response dto

    }

    public void deleteCategory(Long id) {
        Category category = cRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found."));//
        cRepo.delete(category);//deleting category from repository
    }

}
