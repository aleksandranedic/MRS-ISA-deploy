package com.project.team9.service;

import com.project.team9.exceptions.CategoryExistsException;
import com.project.team9.model.buissness.UserCategory;
import com.project.team9.repo.UserCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserCategoryService {
    private final ClientService clientService;
    private final UserCategoryRepository repository;

    @Autowired
    public UserCategoryService(ClientService clientService, UserCategoryRepository repository) {
        this.clientService = clientService;
        this.repository = repository;
    }

    public List<UserCategory> getAllClientCategories() {
        return repository.getAllClientCategories();
    }

    public List<UserCategory> getAllVendorCategories() {
        return repository.getAllVendorCategories();
    }

    public Long addUserCategory(UserCategory userCategory) throws CategoryExistsException {




        if (Objects.equals(userCategory.getType(), "CLIENT")) {

            for (UserCategory uc: getAllClientCategories()) {

                if (uc.getMinimumPoints() <= userCategory.getMinimumPoints() && userCategory.getMinimumPoints() <= uc.getMaximumPoints()) {
                    throw new CategoryExistsException();
                }

                if (uc.getMinimumPoints() <= userCategory.getMinimumPoints() && userCategory.getMaximumPoints() <= uc.getMaximumPoints()) {
                    throw new CategoryExistsException();
                }

            }

            userCategory.setClientCategory(true);
            userCategory.setVendorCategory(false);
        }
        else if (Objects.equals(userCategory.getType(), "VENDOR")) {

            for (UserCategory uc: getAllVendorCategories()) {

                if (uc.getMinimumPoints() <= userCategory.getMinimumPoints() && userCategory.getMinimumPoints() <= uc.getMaximumPoints()) {
                    throw new CategoryExistsException();
                }

                if (uc.getMinimumPoints() <= userCategory.getMinimumPoints() && userCategory.getMaximumPoints() <= uc.getMaximumPoints()) {
                    throw new CategoryExistsException();
                }

            }


            userCategory.setClientCategory(false);
            userCategory.setVendorCategory(true);
        }

        return repository.save(userCategory).getId();
    }

    public Long deleteUserCategory(Long id) {
        UserCategory userCategory = repository.getById(id);
        userCategory.setDeleted(true);
        return repository.save(userCategory).getId();
    }

    public UserCategory getVendorCategoryBasedOnPoints(int points) {
        for (UserCategory userCategory: repository.getAllVendorCategories()) {
            if (points >= userCategory.getMinimumPoints() && points <= userCategory.getMaximumPoints()){
                return userCategory;
            }
        }
        return null;
    }

    public UserCategory getClientCategoryBasedOnPoints(int points) {
        for (UserCategory userCategory: repository.getAllClientCategories()) {
            if (points >= userCategory.getMinimumPoints() && points <= userCategory.getMaximumPoints()){
                return userCategory;
            }
        }
        return null;
    }
}
