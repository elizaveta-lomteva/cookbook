package com.example.cookbook.model.product;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.cookbook.model.recipe.Recipe;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "product_table",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE
        ),
        indices = {@Index("recipe_id")}
)
public class Product implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    private String title;

    private String metrics;

    private double count;

    public Product(int recipeId, String title, String metrics, double count) {
        this.recipeId = recipeId;
        this.title = title;
        this.metrics = metrics;
        this.count = count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMetrics() {
        return metrics;
    }

    public double getCount() {
        return count;
    }

    public int getRecipeId() {
        return recipeId;
    }
}
