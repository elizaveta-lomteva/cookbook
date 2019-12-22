package com.example.cookbook.model.recipe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cookbook.model.product.Product;
import com.example.cookbook.model.step.Step;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipe_table ORDER BY title ASC")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM product_table WHERE recipe_id=:recipeId ORDER BY title ASC")
    LiveData<List<Product>> getProducts(int recipeId);

    @Query("SELECT * FROM step_table WHERE recipe_id=:recipeId ORDER BY `order` ASC")
    LiveData<List<Step>> getSteps (int recipeId);

    @Query("SELECT * FROM recipe_table INNER JOIN product_table ON recipe_table.id=product_table.recipe_id WHERE product_table.title=:title GROUP BY recipe_table.id")
    LiveData<List<Recipe>> getFiltered(String title);
}
