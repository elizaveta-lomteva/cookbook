package com.example.cookbook.model.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;


import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<Product> products = new ArrayList<>();

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);

        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product currentProduct = products.get(position);

        holder.textViewTitle.setText(currentProduct.getTitle());
        holder.textViewCount.setText(Double.toString(currentProduct.getCount()));
        holder.textViewMetric.setText(currentProduct.getMetrics());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    public Product getProductAt(int position){
        return products.get(position);
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewCount;
        private TextView textViewMetric;

        public ProductHolder(@NonNull View itemView){
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_titleStep);
            textViewCount = itemView.findViewById(R.id.text_view_count);
            textViewMetric = itemView.findViewById(R.id.text_view_metric);
        }
    }
}
