package com.example.pavelplakhotny_c196.ui.courses.courseList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.databinding.CourseContentBinding;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    private static final String TAG = "CourseListAdapter";
    private List<Course> tCourse;
    private Context tContext;
    private CourseListAdapter.OnCourseListener listener;
    private CourseListAdapter.OnCourseLongClickListener longClickListener;

    public Context getContext() {
        return tContext;
    }

    public CourseListAdapter(Context context, CourseListAdapter.OnCourseListener listener, CourseListAdapter.OnCourseLongClickListener longClickListener) {
        this.tContext = context;
        this.listener = listener;
        this.longClickListener = longClickListener;
        tCourse = new ArrayList<>();
    }

    public void setData(List<Course> courses) {
        tCourse.clear();
        tCourse.addAll(courses);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseListAdapter.CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        CourseContentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(tContext), R.layout.course_content, parent, false);
        return new CourseListAdapter.CourseListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListAdapter.CourseListViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        holder.bind(tCourse.get(position));
    }


    @Override
    public int getItemCount() {
        return tCourse.size();
    }

    public Course getCourseAt(int position) {
        return tCourse.get(position);
    }


    public class CourseListViewHolder extends RecyclerView.ViewHolder {
        public CourseContentBinding binding;


        public CourseListViewHolder(@NonNull CourseContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(Course course) {
            binding.getRoot().setOnClickListener(v -> listener.onClicked(binding.getCourse()));
            binding.getRoot().setOnLongClickListener(this::onLongClick);
            binding.setCourse(course);
            binding.executePendingBindings();
        }

        private boolean onLongClick(View v) {
            longClickListener.onLongClicked(binding.getCourse());
            return true;
        }
    }

    public interface OnCourseListener {
        void onClicked(Course course);
    }

    public interface OnCourseLongClickListener {
        boolean onLongClicked(Course course);
    }
}
