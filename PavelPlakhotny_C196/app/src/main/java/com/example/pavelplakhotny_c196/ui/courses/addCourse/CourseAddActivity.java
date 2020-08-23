package com.example.pavelplakhotny_c196.ui.courses.addCourse;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.databinding.ActivityAddCourseBinding;
import com.example.pavelplakhotny_c196.databinding.DropdownStatusAddCourseBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseAddActivity extends AppCompatActivity {

    private ActivityAddCourseBinding binding;
    private String startCourseString;
    private String endCourseString;
    private String courseName;
    private String courseNotes;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;
    private String spinnerLabel;
    private Course course;
    private Integer termIdFK;
    private CourseAddViewModel courseAddViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_course);
        setAppBar();
        courseAddViewModel = new ViewModelProvider(this).get(CourseAddViewModel.class);
        termIdFK = null;
        setSpinner();
        showDatePicker();

        binding.saveNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mentorName = binding.mentorNameEdit.getText().toString().trim();
                mentorPhone = binding.mentorPhoneEdit.getText().toString().trim();
                mentorEmail = binding.mentorEmailEdit.getText().toString().trim();
                courseName = binding.courseNameEdit.getText().toString().trim();
                courseNotes = binding.courseNotesEdit.getText().toString().trim();
                spinnerLabel = binding.statusDropDownAdd.getText().toString();
                submit_form();

            }
        });
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Course Duration Dates").setTheme(R.style.MaterialCalendarTheme_RangeFill);
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();


        binding.courseDurationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (materialDatePicker != null && materialDatePicker.isVisible()) {
                    //do nothing
                } else {
                    materialDatePicker.show(getSupportFragmentManager(), "date picker");
                }

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {

                DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.first), ZoneOffset.UTC);
                LocalDate startLD = start.toLocalDate();
                startCourseString = startLD.format(f);
                LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second), ZoneOffset.UTC);
                LocalDate endLD = end.toLocalDate();
                endCourseString = endLD.format(f);
                binding.courseDurationEdit.setText(startCourseString + "  -  " + endCourseString);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.courseDateStartLabel) + startCourseString + "\n" + getString(R.string.courseDateEndLabel) + endCourseString, Toast.LENGTH_SHORT);
                toast.show();
            }

        });
    }

    private boolean validate_name() {
        if (binding.courseNameEdit.getText().toString().trim().isEmpty()) {
            binding.courseName.setError("Enter Name");
            requestFocus(binding.courseNameEdit);
            return false;
        }
        return true;
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void submit_form() {
        if (validate_name()) {
            course = new Course(courseName, startCourseString, endCourseString, termIdFK, spinnerLabel, courseNotes, mentorName, mentorEmail, mentorPhone);
            courseAddViewModel.saveCourse(course);
            finish();

        }
    }


    private void setAppBar() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Course");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_app_logo);
        getWindow().setStatusBarColor(Color.BLACK);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getApplication(),
                        R.layout.dropdown_status_add_course,
                        getResources().getStringArray(R.array.spinner_array));

        binding.statusDropDownAdd.setAdapter(adapter);
    }
}
