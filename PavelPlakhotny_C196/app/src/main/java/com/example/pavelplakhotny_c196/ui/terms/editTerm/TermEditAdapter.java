package com.example.pavelplakhotny_c196.ui.terms.editTerm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.databinding.CourseContentInTermBinding;

import java.util.ArrayList;
import java.util.List;

public class TermEditAdapter extends RecyclerView.Adapter<TermEditAdapter.CourseListInTermEditViewHolder> {
private List<Course> tCourse;
private Context tContext;
private TermEditAdapter.OnCourseInTermListener listener;
public Context getContext() {
        return tContext;
        }

    public TermEditAdapter(Context tContext, TermEditAdapter.OnCourseInTermListener listener) {
        tCourse = new ArrayList<>();
        this.tContext = tContext;
        this.listener = listener;
    }

    public void setData(List<Course> courses){
        tCourse.clear();
        tCourse.addAll(courses);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TermEditAdapter.CourseListInTermEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CourseContentInTermBinding binding = DataBindingUtil.inflate(LayoutInflater.from(tContext), R.layout.course_content_in_term, parent, false);
        return new TermEditAdapter.CourseListInTermEditViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TermEditAdapter.CourseListInTermEditViewHolder holder, int position) {
    holder.bind(tCourse.get(position));
    }

    @Override
    public int getItemCount() {
      return tCourse.size();
    }

    public Course getCourseAtInTerm(int position) {
        return tCourse.get(position);
    }

    public class CourseListInTermEditViewHolder extends RecyclerView.ViewHolder{
    public CourseContentInTermBinding binding;

        public CourseListInTermEditViewHolder(@NonNull CourseContentInTermBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Course course) {
            binding.getRoot().setOnClickListener(v -> listener.onClicked(binding.getCourse()));
            binding.setCourse(course);
            binding.executePendingBindings();
        }
    }
    public interface OnCourseInTermListener {
        void onClicked(Course course);
    }
}
