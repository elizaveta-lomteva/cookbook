package com.example.cookbook.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookbook.R;
import com.example.cookbook.model.product.Product;
import com.example.cookbook.model.product.ProductAdapter;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {
    private TextView editIngredientTitle;
    private TextView editIngredientCount;
    private TextView editIngredientMetric;
    private int recipeId;

    private RecipeViewModel recipeViewModel;
    private ArrayList<Product> products = new ArrayList<>();

    public IngredientsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        FloatingActionButton addButton = addButton = view.findViewById(R.id.add_ingredient_button);

        addButton.setOnClickListener((View v) -> {
            saveProduct();
            hideKeyboard(v);
        });

        editIngredientTitle = view.findViewById(R.id.edit_text_ingredient);
        editIngredientCount = view.findViewById(R.id.edit_text_count);
        editIngredientMetric = view.findViewById(R.id.edit_text_metric);

        editIngredientTitle.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(view);
            }
        });

        editIngredientCount.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(view);
            }
        });

        editIngredientMetric.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(view);
            }
        });

        RecyclerView recyclerViewProducts = view.findViewById(R.id.recycler_view_ingredients);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewProducts.setHasFixedSize(true);

        final ProductAdapter adapter = new ProductAdapter();
        recyclerViewProducts.setAdapter(adapter);

        recipeId = getActivity().getIntent().getIntExtra(AddEditRecipeActivity.EXTRA_ID, -1);

        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        if(recipeId != -1) {

            recipeViewModel.getProductsByRecipeId(recipeId)
                    .observe(this, new Observer<List<Product>>() {
                        @Override
                        public void onChanged(@Nullable List<Product> products) {
                            adapter.setProducts(products);
                        }
                    });
        }else {
            adapter.setProducts(products);
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Product product = adapter.getProductAt(viewHolder.getAdapterPosition());

                if(recipeId != -1) {
                    recipeViewModel.delete(product);

                }else {
                    products.remove(product);
                    adapter.setProducts(products);
                }
                Toast.makeText(getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewProducts);


        return view;
    }


    private void saveProduct() {
        String title = editIngredientTitle.getText().toString().trim();
        String count = editIngredientCount.getText().toString().trim();
        String metric = editIngredientMetric.getText().toString().trim();

        if (title.isEmpty() || metric.isEmpty() || count.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        Product productToSave = new Product(recipeId, title, metric, Double.parseDouble(count));

        if(recipeId == -1){
            products.add(productToSave);
            OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
            listener.onFragmentSetProducts(products);
        } else {
            recipeViewModel.insert(productToSave);
        }

        editIngredientTitle.setText("");
        editIngredientCount.setText("");
        editIngredientMetric.setText("");
    }

    public interface OnFragmentInteractionListener{
        void onFragmentSetProducts(ArrayList<Product> products);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
