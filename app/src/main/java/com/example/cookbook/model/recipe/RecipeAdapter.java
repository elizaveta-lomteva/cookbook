package com.example.cookbook.model.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> implements Filterable {
    private List<Recipe> recipes = new ArrayList<>();
    private List<Recipe> recipesFull;

    private OnItemClickListener listener;

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new RecipeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        Recipe currentRecipe = recipes.get(position);
        holder.textViewTitle.setText(currentRecipe.getTitle());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        recipesFull = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }

    public Recipe getRecipeAt(int position) {
        return recipes.get(position);
    }

    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    private Filter recipeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Recipe> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(recipesFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Recipe recipe : recipesFull) {
                    if(recipe.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(recipe);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            recipes.clear();
            recipes.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        RecipeHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_titleStep);

            itemView.setOnClickListener((View v) -> {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(recipes.get(position));
                    }
                });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
