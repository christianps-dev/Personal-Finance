package org.alunosufg.personalfinancespring.services;

import org.alunosufg.personalfinancespring.entities.CategoryEntity;
import org.alunosufg.personalfinancespring.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class CategoriesService {

    private final CategoryRepository categoryRepository;

    public CategoriesService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addNewCategory(String categoryAdd, Integer value ){
        CategoryEntity category = new CategoryEntity();
        category.setCategory(categoryAdd.toUpperCase(Locale.ROOT));
        System.out.println("Adding new category: " + category);

        if (value > 0)
            category.setType("Income");

        else if (value < 0)
            category.setType("Expense");

        else
            category.setType("Null");
        if(!categoryRepository.existsByCategory(category.getCategory())){
            System.out.println("Category added: " + category.getCategory());
            categoryRepository.save(category);
        }
    }

    public CategoryEntity getCategory(String category){
        System.out.println("Getting category: " + category);
        CategoryEntity categoryEntity = categoryRepository.getCategoryEntitiesByCategory(category.toUpperCase(Locale.ROOT));

        if(categoryEntity != null) {
            System.out.println("Category found: " + categoryEntity.getCategory());
            return categoryEntity;
        }
        else {
            System.out.println("Category not found: " + category);

            addNewCategory(category, 0);
            return categoryRepository.getCategoryEntitiesByCategory(category.toUpperCase(Locale.ROOT));
        }
    }

    public Long getCategoryId(String ctg){
        return categoryRepository.getCategoryId(ctg.toUpperCase()).orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
