package com.example.cookbook.model.step;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.cookbook.model.recipe.Recipe;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "step_table",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE
        ),
        indices = {@Index("recipe_id")}
)
public class Step implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "recipe_id")
    private int recipeId;
    private String text;
    private int order;

    public Step(int recipeId, String text, int order) {
        this.recipeId = recipeId;
        this.text = text;
        this.order = order;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getText() {
        return text;
    }

    public int getOrder() {
        return order;
    }
}
