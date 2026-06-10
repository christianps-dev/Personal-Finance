package org.alunosufg.personalfinancespring.services;
import org.alunosufg.personalfinancespring.dto.transactions.BudgetDTO;
import org.alunosufg.personalfinancespring.dto.transactions.UserTransactionDTO;
import org.alunosufg.personalfinancespring.entities.BudgetEntity;
import org.alunosufg.personalfinancespring.entities.UserEntity;
import org.alunosufg.personalfinancespring.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetServices {

    private final BudgetRepository budgetRepository;
    private final UserAuthService userAuthService;
    private final CategoriesService categoriesService;

    public BudgetServices(BudgetRepository budgetRepository, UserAuthService userAuthService,
                          CategoriesService categoriesService) {
        this.budgetRepository = budgetRepository;
        this.userAuthService = userAuthService;
        this.categoriesService = categoriesService;
    }

    public BudgetDTO addNewBudget(BudgetDTO dto, String email){


            if (!existBudget(dto, email)){
                System.out.println("Adding new budget to category:" + dto.getCategory());

                BudgetEntity newBudget = new BudgetEntity();
                newBudget.setCategory(categoriesService.getCategory(new UserTransactionDTO("", 0, dto.getCategory().toUpperCase(), "")));
                newBudget.setBudgetLimit(dto.getBudgetLimit());
                newBudget.setMonth(dto.getMonth());
                newBudget.setUser(userAuthService.getUser(email));

                budgetRepository.save(newBudget);
            }
            else {
                System.out.println("Updating budget to category: " + dto.getCategory());

                budgetRepository.updateBudget(
                        userAuthService.getUserId(email),
                        categoriesService.getCategoryId(dto.getCategory()),
                        dto.getMonth(),
                        dto.getBudgetLimit());

            }
        return dto;
    }

    public List<BudgetDTO> getBudgets(String email, Integer month) {
        UserEntity user = userAuthService.getUser(email);

        return budgetRepository.getBudgets(user, month);
    }

    public boolean existBudget(BudgetDTO dto, String email){
        System.out.println("Exist budget function");
        Optional<BudgetEntity> budget = budgetRepository.existsBudget(
                userAuthService.getUserId(email),
                categoriesService.getCategoryId(dto.getCategory()),
                dto.getMonth()
        );

        if (budget.isPresent()){
            System.out.println("--- Budget exists " + budget.get().getId());
            return true;

        }
        else {

            System.out.println("--- Budget null");

            return false;
        }
    }
}
