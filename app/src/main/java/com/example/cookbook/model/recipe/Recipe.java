package com.example.cookbook.model.recipe;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.example.cookbook.model.product.Product;
import com.example.cookbook.model.step.Step;

import java.util.List;

@Entity(tableName = "recipe_table")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    @Ignore
    private List<Product> products;

    @Ignore
    private List<Step> steps;

    public Recipe(String title) {
        this.title = title;
    }

    @Ignore
    public Recipe(int id, String title) {
        this.title = title;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
