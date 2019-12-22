package com.example.cookbook.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.cookbook.model.product.Product;
import com.example.cookbook.model.recipe.Recipe;
import com.example.cookbook.model.recipe.RecipeRepository;
import com.example.cookbook.model.step.Step;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository repository;

    private LiveData<List<Recipe>> allRecipes;

    private LiveData<List<Product>> currentProducts;

    private LiveData<List<Step>> currentStepList;


    public RecipeViewModel(@NonNull Application application) {
        super(application);
        repository = new RecipeRepository(application);
        allRecipes = repository.getAllRecipes();
    }


    public void insert(Recipe recipe) {
        repository.insert(recipe);
    }

    public void insert(Product product) {
        repository.insert(product);
    }

    public void insert(Step step) {
        repository.insert(step);
    }


    public void update(Recipe recipe) {
        repository.update(recipe);
    }


    public void delete(Recipe recipe) {
        repository.delete(recipe);
    }

    public void delete(Product product) {
        repository.delete(product);
    }

    public void delete(Step step) {
        repository.delete(step);
    }

    //
    public void filter(String title) {
        repository.filter(title);
    }
//
    public LiveData<List<Product>> getProductsByRecipeId(int recipeId) {
        currentProducts = repository.getProducts(recipeId);
        return currentProducts;
    }


    public LiveData<List<Step>> getStepsByRecipeId(int recipeId) {
        currentStepList = repository.getSteps(recipeId);
        return currentStepList;
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }
}
