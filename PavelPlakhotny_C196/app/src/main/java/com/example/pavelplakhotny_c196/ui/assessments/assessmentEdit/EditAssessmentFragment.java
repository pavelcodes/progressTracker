package com.example.pavelplakhotny_c196.ui.assessments.assessmentEdit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.databinding.FragmentEditAssessmentBinding;
import com.example.pavelplakhotny_c196.ui.assessments.assessmentList.AssessmentsListViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;


public class EditAssessmentFragment extends Fragment {

    String assessment_name;
    String assessment_due_date;
    FragmentEditAssessmentBinding binding;
    AssessmentsListViewModel viewModel;
    String assessment_type;
    Assessment assessment;
    Integer courseId_FK;
    String assessment_start_date;
    private ExecutorService executor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_assessment, container, false);
        viewModel = new ViewModelProvider(this).get(AssessmentsListViewModel.class);
        assert getArguments() != null;
        assessment = (Assessment) getArguments().getSerializable("assessment");
        binding.setAssessment(assessment);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        assessment_name = assessment.getAssessment_title();
        assessment_due_date = assessment.getDue_date();
        assessment_start_date = assessment.getStart_date();
        courseId_FK = assessment.getCourse_id_FK();
        loadType();
        showDatePicker();


        binding.typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.performance_radio:
                        assessment_type = "Performance";
                        break;
                    case R.id.objective_radio:
                        assessment_type = "Objective";
                        break;
                }
            }
        });


        binding.saveEditAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assessment_name = binding.assessmentNameEdit.getText().toString().trim();
                /*assessment_type = binding.objectiveRadio.getText().toString();*/
                submit_form();
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AssessmentsListViewModel.class);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.ic_app_logo);
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
                    materialDatePicker.show(getChildFragmentManager(), "date picker");
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
                Toast toast = Toast.makeText(getContext(), getString(R.string.assessmentDateStartLabel) + assessment_start_date + "\n" + getString(R.string.assessmentDateEndLabel) + assessment_due_date, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validate_name() {
        if (binding.assessmentNameEdit.getText().toString().trim().isEmpty()) {
            binding.assessmentName.setError("Enter Name");
            requestFocus(binding.assessmentNameEdit);
            return false;
        }
        return true;
    }

    private void submit_form() {
        if (!validate_name()) {
            return;
        } else {
            assessment.setAssessment_title(assessment_name);
            assessment.setDue_date(assessment_due_date);
            assessment.setAssessment_type(assessment_type);
            assessment.setCourse_id_FK(courseId_FK);
            assessment.setStart_date(assessment_start_date);
            viewModel.update(assessment);
            Toast.makeText(getContext(), "Assessment " + assessment_name + " Updated", Toast.LENGTH_SHORT).show();
            NavController controller = Navigation.findNavController(binding.getRoot());
            controller.popBackStack(R.id.fragment_assessment, false);
            controller.navigate(R.id.action_editAssessment_to_assessmentList);

        }
    }

    private void loadType() {
        assessment_type = assessment.getAssessment_type();
        if (assessment_type.equals("Objective")) {
            binding.typeRadioGroup.check(R.id.objective_radio);
        } else {
            binding.typeRadioGroup.check(R.id.performance_radio);
        }

    }
}