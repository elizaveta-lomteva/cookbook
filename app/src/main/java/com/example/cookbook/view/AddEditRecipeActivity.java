package com.example.cookbook.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cookbook.R;
import com.example.cookbook.model.product.Product;
import com.example.cookbook.model.product.ProductAdapter;
import com.example.cookbook.model.recipe.Recipe;
import com.example.cookbook.model.recipe.RecipeAdapter;
import com.example.cookbook.model.step.Step;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AddEditRecipeActivity extends AppCompatActivity implements IngredientsFragment.OnFragmentInteractionListener, StepsFragment.OnFragmentInteractionListener {
    public static final String EXTRA_ID =
            "com.example.cookbook.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.cookbook.EXTRA_TITLE";
    public static final String EXTRA_PRODUCTS =
            "com.example.cookbook.EXTRA_PRODUCTS";
    public static final String EXTRA_STEPS =
            "com.example.cookbook.EXTRA_STEPS";


    private EditText editTextTitle;
    private BottomNavigationView bottomNavigationView;
    private Fragment ingredients;
    private Fragment steps;
    private ArrayList<Product> products;
    private ArrayList<Step> stepsList;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_ingredient:
                            selectedFragment = ingredients;
                            break;
                        case R.id.nav_step:
                            selectedFragment = steps;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();

                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        bottomNavigationView = findViewById(R.id.nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        ingredients = new IngredientsFragment();
        steps = new StepsFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ingredients)
                .commit();

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Recipe");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        } else {
            setTitle("Add Recipe");
        }

    }

    private void saveRecipe() {
        String title = editTextTitle.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent(getApplicationContext(), MainActivity.class);

        data.putExtra(EXTRA_PRODUCTS, products);
        data.putExtra(EXTRA_STEPS, stepsList);

        data.putExtra(EXTRA_TITLE, title);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_recipe:
                saveRecipe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onFragmentSetProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public void onFragmentSetSteps(ArrayList<Step> steps) {
        this.stepsList = steps;
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

