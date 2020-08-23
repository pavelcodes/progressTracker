package com.example.pavelplakhotny_c196.ui.assessments.assessmentList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.databinding.AssessmentContentBinding;

import java.util.ArrayList;
import java.util.List;


public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.AssessmentListViewHolder> {

    private static final String TAG = "AssessmentListAdapter";
    private List<Assessment> mAssessment;
    private Context tContext;
    private AssessmentListAdapter.onAssessmentListener listener;
    private AssessmentListAdapter.OnAssessmentLongClickListener longClickListener;

    public Context getContext() {
        return tContext;
    }

    public AssessmentListAdapter(Context context,
                                 AssessmentListAdapter.onAssessmentListener listener, AssessmentListAdapter.OnAssessmentLongClickListener longClickListener) {
        this.tContext = context;
        this.listener = listener;
        this.longClickListener = longClickListener;
        mAssessment = new ArrayList<>();
    }

    public void setData(List<Assessment> assessments) {
        mAssessment.clear();
        mAssessment.addAll(assessments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssessmentListAdapter.AssessmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        AssessmentContentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(tContext), R.layout.assessment_content, parent, false);
        return new AssessmentListAdapter.AssessmentListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentListViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder:Assessment List");
        holder.bind(mAssessment.get(position));
    }


    @Override
    public int getItemCount() {
        return mAssessment.size();
    }

    public Assessment getAssessmentAt(int position) {
        return mAssessment.get(position);
    }


    public class AssessmentListViewHolder extends RecyclerView.ViewHolder {
        public AssessmentContentBinding binding;


        public AssessmentListViewHolder(@NonNull AssessmentContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(Assessment assessment) {
            binding.getRoot().setOnClickListener(v -> listener.onClicked(binding.getAssessment()));
            binding.getRoot().setOnLongClickListener(this::onLongClick);
            binding.setAssessment(assessment);
            binding.executePendingBindings();
        }

        private boolean onLongClick(View v) {
            longClickListener.onLongClicked(binding.getAssessment());
            return true;
        }
    }

    public interface onAssessmentListener {
        void onClicked(Assessment assessment);
    }

    public interface OnAssessmentLongClickListener {
        boolean onLongClicked(Assessment assessment);
    }

}
