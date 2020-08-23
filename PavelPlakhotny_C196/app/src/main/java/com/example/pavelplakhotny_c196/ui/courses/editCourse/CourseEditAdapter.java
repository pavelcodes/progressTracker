package com.example.pavelplakhotny_c196.ui.courses.editCourse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.databinding.AssessmentContentBinding;

import java.util.ArrayList;
import java.util.List;

public class CourseEditAdapter extends RecyclerView.Adapter<CourseEditAdapter.AssessmentListViewHolder> {
    private List<Assessment> tAssessment;
    private Context tContext;
    private CourseEditAdapter.OnAssessmentInCourseListener listener;

    public Context getContext() {
        return tContext;
    }

    public CourseEditAdapter(Context tContext, CourseEditAdapter.OnAssessmentInCourseListener listener) {
        tAssessment = new ArrayList<>();
        this.tContext = tContext;
        this.listener = listener;
    }

    public void setData(List<Assessment> assessments) {
        tAssessment.clear();
        tAssessment.addAll(assessments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseEditAdapter.AssessmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AssessmentContentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(tContext), R.layout.assessment_content, parent, false);
        return new CourseEditAdapter.AssessmentListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseEditAdapter.AssessmentListViewHolder holder, int position) {
        holder.bind(tAssessment.get(position));
    }

    @Override
    public int getItemCount() {
        return tAssessment.size();
    }

    public Assessment getAssessmentInCourse(int position) {
        return tAssessment.get(position);
    }


    public class AssessmentListViewHolder extends RecyclerView.ViewHolder {
        AssessmentContentBinding binding;

        public AssessmentListViewHolder(@NonNull AssessmentContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Assessment assessment) {
            binding.getRoot().setOnClickListener(v -> listener.onClicked(binding.getAssessment()));
            binding.setAssessment(assessment);
            binding.executePendingBindings();
        }
    }

    public interface OnAssessmentInCourseListener {
        void onClicked(Assessment assessment);
    }
}
