package com.example.cookbook.model.step;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StepDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Step step);

    @Update
    void update(Step step);

    @Delete
    void delete(Step step);

    @Query("SELECT * FROM step_table WHERE recipe_id=:recipeId ORDER BY text ASC")
    LiveData<List<Step>> getRecipeSteps(int recipeId);
}
