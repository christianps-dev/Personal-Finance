package org.alunosufg.personalfinancespring.services;

import org.alunosufg.personalfinancespring.dto.transactions.UserTransactionDTO;
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

    public void addNewCategory(UserTransactionDTO dto){
        CategoryEntity category = new CategoryEntity();
        category.setCategory(dto.category().toUpperCase(Locale.ROOT));
        System.out.println("Adding new category: " + dto.category());

        if (dto.value() > 0)
            category.setType("Income");

        else
            category.setType("Expense");

        if(!categoryRepository.existsByCategory(category.getCategory())){
            System.out.println("Category added: " + category.getCategory());
            categoryRepository.save(category);
        }
    }

    public CategoryEntity getCategory(UserTransactionDTO dto){
        System.out.println("Getting category: " + dto.category());
        CategoryEntity categoryEntity = categoryRepository.getCategoryEntitiesByCategory(dto.category().toUpperCase(Locale.ROOT));

        if(categoryEntity != null) {
            System.out.println("Category found: " + categoryEntity.getCategory());
            return categoryEntity;
        }
        else {
            System.out.println("Category not found: " + dto.category());

            addNewCategory(dto);
            return categoryRepository.getCategoryEntitiesByCategory(dto.category().toUpperCase(Locale.ROOT));
        }
    }

}
