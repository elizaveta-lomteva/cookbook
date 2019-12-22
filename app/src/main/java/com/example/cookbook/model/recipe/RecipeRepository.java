package com.example.cookbook.model.recipe;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cookbook.model.RecipeDatabase;
import com.example.cookbook.model.product.Product;
import com.example.cookbook.model.product.ProductDao;
import com.example.cookbook.model.step.Step;
import com.example.cookbook.model.step.StepDao;

import java.util.List;
import java.util.Objects;

public class RecipeRepository {

    private RecipeDao recipeDao;
    private ProductDao productDao;
    private StepDao stepDao;

    private MutableLiveData<List<Recipe>> mutableLiveData = new MutableLiveData<>();

    private LiveData<List<Recipe>> allRecipes;

    public RecipeRepository(Application application) {
        RecipeDatabase database = RecipeDatabase.getInstance(application);
        recipeDao = database.recipeDao();
        productDao = database.productDao();
        stepDao = database.stepDao();

        allRecipes = recipeDao.getAllRecipes();
    }


    public void insert(Recipe recipe) {
        new InsertRecipeAsyncTask(recipeDao, productDao, stepDao).execute(recipe);
    }
    public void insert(Product product) {
        new InsertProductAsyncTask(productDao).execute(product);
    }
    public void insert(Step step) {
        new InsertStepAsyncTask(stepDao).execute(step);
    }


    public void update(Recipe recipe) {
        new UpdateRecipeAsyncTask(recipeDao).execute(recipe);
    }


    public void delete(Recipe recipe) {
        new DeleteRecipeAsyncTask(recipeDao).execute(recipe);
    }

    public void delete(Product product) {
        new DeleteProductAsyncTask(productDao).execute(product);
    }

    public void delete(Step step) {
        new DeleteStepAsyncTask(stepDao).execute(step);
    }



    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public LiveData<List<Product>> getProducts(int recipeId) {
        return recipeDao.getProducts(recipeId);
    }

    public LiveData<List<Step>> getSteps(int recipeId) {
        return recipeDao.getSteps(recipeId);
    }

    //
    public LiveData<List<Recipe>> filter(String title) {
        return recipeDao.getFiltered(title);
    }
//

    private static class InsertRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {
        private RecipeDao recipeDao;
        private ProductDao productDao;
        private StepDao stepDao;

        private InsertRecipeAsyncTask(RecipeDao recipeDao, ProductDao productDao, StepDao stepDao) {
            this.productDao = productDao;
            this.stepDao = stepDao;
            this.recipeDao = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            Recipe recipe = recipes[0];
            int recipeId = (int) recipeDao.insert(recipe);

            List<Product> products = recipe.getProducts();
            if (!Objects.isNull(products)) {
                for (Product product : products) {
                    product.setRecipeId(recipeId);
                    productDao.insert(product);
                }
            }

            List<Step> steps = recipe.getSteps();

            if (!Objects.isNull(steps)) {
                for (Step step : steps) {
                    step.setRecipeId(recipeId);
                    stepDao.insert(step);
                }
            }

            return null;
        }
    }

    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private InsertProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.insert(products[0]);

            return null;
        }
    }

    private static class InsertStepAsyncTask extends AsyncTask<Step, Void, Void> {
        private StepDao stepDao;

        private InsertStepAsyncTask(StepDao stepDao) {
            this.stepDao = stepDao;
        }

        @Override
        protected Void doInBackground(Step... steps) {

            stepDao.insert(steps[0]);
            return null;
        }
    }



    private static class UpdateRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {
        private RecipeDao recipeDao;

        private UpdateRecipeAsyncTask(RecipeDao recipeDao) {
            this.recipeDao = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            Recipe recipe = recipes[0];
            recipeDao.update(recipe);

            return null;
        }
    }



    private static class DeleteRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {
        private RecipeDao recipeDao;

        private DeleteRecipeAsyncTask(RecipeDao recipeDao) {
            this.recipeDao = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            recipeDao.delete(recipes[0]);
            return null;
        }
    }

    private static class DeleteProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private DeleteProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.delete(products[0]);
            return null;
        }
    }

    private static class DeleteStepAsyncTask extends AsyncTask<Step, Void, Void> {
        private StepDao stepDao;

        private DeleteStepAsyncTask(StepDao stepDao) {
            this.stepDao = stepDao;
        }

        @Override
        protected Void doInBackground(Step... steps) {
            stepDao.delete(steps[0]);
            return null;
        }
    }
}
