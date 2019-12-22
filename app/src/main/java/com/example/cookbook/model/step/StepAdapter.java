package com.example.cookbook.model.step;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {
    private List<Step> steps = new ArrayList<>();

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);

        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder holder, int position) {
        Step currentStep = steps.get(position);

        holder.textViewText.setText(currentStep.getText());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public Step getStepAt(int position){
        return steps.get(position);
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    class StepHolder extends RecyclerView.ViewHolder{
        private TextView textViewText;

        public StepHolder(@NonNull View itemView) {
            super(itemView);

            textViewText = itemView.findViewById(R.id.text_view_titleStep);
        }
    }

}
