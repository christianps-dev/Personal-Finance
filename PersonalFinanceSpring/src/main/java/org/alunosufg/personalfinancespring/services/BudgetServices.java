package org.alunosufg.personalfinancespring.services;
import org.alunosufg.personalfinancespring.dto.budgets.BudgetDTO;
import org.alunosufg.personalfinancespring.dto.budgets.BudgetLimitDTO;
import org.alunosufg.personalfinancespring.entities.BudgetEntity;
import org.alunosufg.personalfinancespring.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetServices {

    private final BudgetRepository budgetRepository;
    private final CategoriesService categoriesService;
    private final AccountsServices accountsServices;

    public BudgetServices(BudgetRepository budgetRepository, CategoriesService categoriesService,
                         AccountsServices accountsServices) {
        this.budgetRepository = budgetRepository;
        this.categoriesService = categoriesService;
        this.accountsServices = accountsServices;
    }

    public BudgetDTO addNewBudget(BudgetDTO dto, String email){


            if (!existBudget(dto, email)){
                System.out.println("Adding new budget to category:" + dto.getCategory());

                BudgetEntity newBudget = new BudgetEntity();
                newBudget.setCategory(categoriesService.getCategory(dto.getCategory()));
                newBudget.setBudgetLimit(dto.getBudgetLimit());
                newBudget.setMonth(dto.getMonth());
                newBudget.setAccount(accountsServices.getAccount(email));

                budgetRepository.save(newBudget);
            }
            else {
                System.out.println("Updating budget to category: " + dto.getCategory());

                budgetRepository.updateBudget(
                        accountsServices.getAccount(email),
                        categoriesService.getCategoryId(dto.getCategory()),
                        dto.getMonth(),
                        dto.getBudgetLimit());

            }
        return dto;
    }

    public List<BudgetDTO> getBudgets(String email, Integer month) {
        System.out.println("--- Getting budgets");

        return budgetRepository.getBudgets(accountsServices.getAccount(email), month);
    }

    public boolean existBudget(BudgetDTO dto, String email){
        System.out.println("Exist budget function");
        Optional<BudgetEntity> budget = budgetRepository.existsBudget(
                accountsServices.getAccount(email),
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

    public List<BudgetLimitDTO> getBudgetsLimits(String email, Integer month){

        System.out.printf("--- Getting budgets spends in the month %d \n", month);


        String monthQuery = "2026-" + month + "-";
        Date lastDay = java.sql.Date.valueOf(monthQuery + "31");
        Date firstDay = java.sql.Date.valueOf(monthQuery + "1");

        return budgetRepository.getBudgetsLimitsAndSpends(accountsServices.getAccount(email), firstDay, lastDay);

    }
}
