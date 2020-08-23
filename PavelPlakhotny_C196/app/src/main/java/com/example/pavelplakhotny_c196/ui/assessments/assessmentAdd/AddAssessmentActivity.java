package com.example.pavelplakhotny_c196.ui.assessments.assessmentAdd;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.databinding.ActivityAddAssessmentBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AddAssessmentActivity extends AppCompatActivity {
    String assessment_name;
    String assessment_due_date;
    ActivityAddAssessmentBinding binding;
    AssessmentAddViewModel assessmentAddViewModel;
    String assessment_type = "Performance";
    Assessment assessment;
    Integer courseId_FK;
    String assessment_start_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_assessment);
        assessmentAddViewModel = new ViewModelProvider(this).get(AssessmentAddViewModel.class);
        setAppBar();
        showDatePicker();
        courseId_FK = null;


        binding.saveNewAssessmentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assessment_name = binding.assessmentNameEdit.getText().toString().trim();
                submit_form();
            }
        });

        binding.typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.performance:
                        assessment_type = "Performance";
                        break;
                    case R.id.objective:
                        assessment_type = "Objective";
                        break;
                }
            }
        });

    }

    private void submit_form() {
        if (validate_name()) {
            assessment = new Assessment(assessment_name, assessment_type, assessment_due_date, courseId_FK, assessment_start_date);
            assessmentAddViewModel.saveAssessment(assessment);
            finish();
        }
    }

    private boolean validate_name() {
        if (binding.assessmentNameEdit.getText().toString().trim().isEmpty()) {
            binding.assessmentNameEdit.setError("Enter Name");
            requestFocus(binding.assessmentNameEdit);
            return false;
        }
        if (binding.typeRadioGroup.getCheckedRadioButtonId() == -1) {
            requestFocus(binding.typeRadioGroup);

            return false;
        }
        return true;
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() { //override back button
        finish();
        return true;
    }


    private void setAppBar() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Assessment");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_app_logo);
        getWindow().setStatusBarColor(Color.BLACK);
    }

    private void showDatePicker() {

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Assessment Duration Dates").setTheme(R.style.MaterialCalendarTheme_RangeFill);
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();


        binding.assessmentDueEdit.setOnClickListener(new View.OnClickListener() {
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
                assessment_start_date = startLD.format(f);
                LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second), ZoneOffset.UTC);
                LocalDate endLD = end.toLocalDate();
                assessment_due_date = endLD.format(f);
                binding.assessmentDueEdit.setText(assessment_start_date + "  -  " + assessment_due_date);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.assessmentDateStartLabel) + assessment_start_date + "\n" + getString(R.string.assessmentDateEndLabel) + assessment_due_date, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}

