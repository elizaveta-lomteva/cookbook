package com.example.cookbook.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookbook.R;
import com.example.cookbook.model.product.Product;
import com.example.cookbook.model.recipe.Recipe;
import com.example.cookbook.model.recipe.RecipeAdapter;
import com.example.cookbook.model.step.Step;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private RecipeViewModel recipeViewModel;
    private List<Product> products;
    private List<Step> steps;
    private final RecipeAdapter adapter = new RecipeAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddRecipe = findViewById(R.id.button_add_recipe);
        buttonAddRecipe.setOnClickListener((View v) -> {
                Intent intent = new Intent(MainActivity.this, AddEditRecipeActivity.class);
                startActivityForResult(intent, 1);
            });

        RecyclerView recyclerViewRecipes = findViewById(R.id.recycler_view);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecipes.setHasFixedSize(true);


        recyclerViewRecipes.setAdapter(adapter);

        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        recipeViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

                adapter.setRecipes(recipes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                recipeViewModel.delete(adapter.getRecipeAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Recipe deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewRecipes);


        adapter.setOnItemClickListener((Recipe recipe) -> {
                Intent intent = new Intent(MainActivity.this, AddEditRecipeActivity.class);
                intent.putExtra(AddEditRecipeActivity.EXTRA_ID, recipe.getId());
                intent.putExtra(AddEditRecipeActivity.EXTRA_TITLE, recipe.getTitle());

                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        products = new ArrayList<>();
        steps = new ArrayList<>();

        String toastMessage = "Recipe not saved";

        if (resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditRecipeActivity.EXTRA_TITLE);

            products = (List<Product>) data.getSerializableExtra(AddEditRecipeActivity.EXTRA_PRODUCTS);
            steps = (List<Step>) data.getSerializableExtra(AddEditRecipeActivity.EXTRA_STEPS);

            Recipe recipe = new Recipe(title);
            recipe.setProducts(products);
            recipe.setSteps(steps);

            switch (requestCode) {
                case ADD_NOTE_REQUEST:
                    recipeViewModel.insert(recipe);

                    toastMessage = "Recipe saved";
                    break;

                case EDIT_NOTE_REQUEST:
                    int id = data.getIntExtra(AddEditRecipeActivity.EXTRA_ID, -1);

                    if (id == -1) {
                        toastMessage = "Recipe can't be updated";
                        break;
                    }

                    recipe.setId(id);
                    recipeViewModel.update(recipe);

                    toastMessage = "Recipe updated";
                    break;

                default:
                    break;
            }
        }

        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }
}
