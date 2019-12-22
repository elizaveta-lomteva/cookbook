package com.example.cookbook.view;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.cookbook.model.step.Step;
import com.example.cookbook.model.step.StepAdapter;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    private TextView editStepText;
    private int recipeId;

    private RecipeViewModel recipeViewModel;
    private ArrayList<Step> steps = new ArrayList<>();
    final StepAdapter adapter = new StepAdapter();

    public StepsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        FloatingActionButton addButton = view.findViewById(R.id.add_step_button);
        addButton.setOnClickListener((View v) -> {
            saveStep();
            hideKeyboard(v);
        });

        editStepText = view.findViewById(R.id.edit_step_text);

        editStepText.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(view);
            }
        });

        RecyclerView recyclerViewSteps = view.findViewById(R.id.recycler_view_steps);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSteps.setHasFixedSize(true);

        recyclerViewSteps.setAdapter(adapter);

        recipeId = getActivity().getIntent().getIntExtra(AddEditRecipeActivity.EXTRA_ID, -1);


        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        if (recipeId != -1) {
            recipeViewModel.getStepsByRecipeId(
                    getActivity().getIntent().getIntExtra(AddEditRecipeActivity.EXTRA_ID, 0))
                    .observe(this, new Observer<List<Step>>() {
                        @Override
                        public void onChanged(@Nullable List<Step> steps) {
                            adapter.setSteps(steps);
                        }
                    });
        } else {
            adapter.setSteps(steps);
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                recipeViewModel.delete(adapter.getStepAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "Step deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewSteps);


        return view;
    }

    private void saveStep() {
        String text = editStepText.getText().toString().trim();
        int order = adapter.getItemCount() + 1;

        if (text.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Step stepToSave = new Step(recipeId, text, order);
        System.out.println(stepToSave.getId() + " " + stepToSave.getOrder() + " " + stepToSave.getText());

        if (recipeId == -1) {
            steps.add(stepToSave);
            OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
            listener.onFragmentSetSteps(steps);
        } else {
            recipeViewModel.insert(stepToSave);
        }

        editStepText.setText("");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentSetSteps(ArrayList<Step> steps);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
