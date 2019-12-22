package com.example.cookbook.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cookbook.model.product.Product;
import com.example.cookbook.model.product.ProductDao;
import com.example.cookbook.model.step.Step;
import com.example.cookbook.model.step.StepDao;
import com.example.cookbook.model.recipe.Recipe;
import com.example.cookbook.model.recipe.RecipeDao;

@Database(entities = {Recipe.class, Product.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase instance;

    public abstract RecipeDao recipeDao();
    public abstract ProductDao productDao();
    public abstract StepDao stepDao();

    public static synchronized RecipeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDatabase.class, "recipe_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulatedAsyncTask(instance).execute();
        }
    };

    private static class PopulatedAsyncTask extends AsyncTask<Void, Void, Void> {
        private RecipeDao recipeDao;
        private ProductDao productDao;
        private StepDao stepDao;

        private PopulatedAsyncTask(RecipeDatabase db) {
            recipeDao = db.recipeDao();
            productDao = db.productDao();
            stepDao = db.stepDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Recipe newRecipe = new Recipe("Ratatouille");
            int recipeId = (int) recipeDao.insert(newRecipe);
            Recipe second = new Recipe(2, "Pelmeshki");
            recipeDao.insert(second);


            Product squash = new Product(recipeId, "Squash", "piece", 1);
            Product tomato = new Product(recipeId, "Tomato", "piece", 3);
            productDao.insert(squash);
            productDao.insert(tomato);

            Step first = new Step(recipeId, "Slice ingredients", 1);
            stepDao.insert(first);

            return null;
        }
    }
}
