package com.example.pavelplakhotny_c196.ui.courses.editCourse;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.databinding.FragmentEditCourseBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class EditCourseFragment extends Fragment implements CourseEditAdapter.OnAssessmentInCourseListener {

    private CourseEditViewModel viewModel;
    private FragmentEditCourseBinding binding;
    private CourseEditAdapter adapter;
    private String startCourse;
    private String endCourse;
    private String courseName;
    private String courseNotes;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;
    private String spinnerLabel;
    private Course course;
    private Integer termIdFK;
    private LocalDate start;
    private LocalDate end;
    private Integer courseId;
    private int assessment_Id;
    private List<Assessment> unassignedAssessments = new ArrayList<>();
    private Assessment assessmentToUnlink;
    private String getData = "";
    private Integer courseIdNull;
    private List<Assessment> assessmentT = new ArrayList<>();

    private static final String TAG = "EditCourseFragment";

    public EditCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getArguments() != null;
        course = (Course) getArguments().getSerializable("course");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_course, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setCourse(course);
        spinnerLabel = course.getStatus();
        startCourse = course.getCourse_start();
        endCourse = course.getCourse_end();
        binding.courseDurationEdit.setText(startCourse + " " + endCourse);
        courseName = binding.courseNameEdit.getText().toString().trim();
        termIdFK = course.getTerm_id_FK();
        courseId = course.getCourse_id();
        initRecyclerview();
        showDatePicker();
        setSpinner();
        showDatePicker();


        binding.fabAddAssessmentToCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAssessmentDialog();
            }
        });

        binding.saveEditedCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mentorName = binding.mentorNameEdit.getText().toString().trim();
                mentorPhone = binding.mentorPhoneEdit.getText().toString().trim();
                mentorEmail = binding.mentorEmailEdit.getText().toString().trim();
                courseName = binding.courseNameEdit.getText().toString().trim();
                courseNotes = binding.courseNotesEdit.getText().toString().trim();
                spinnerLabel = binding.statusDropDown.getText().toString();
                submit_form();
            }
        });

        binding.courseNotes.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseNotes = binding.courseNotesEdit.getText().toString().trim();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, courseNotes);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CourseEditViewModel.class);
        viewModel.getAssessmentListForCurrentCourse(courseId).observe(getViewLifecycleOwner(), assessmentList -> {
            ((CourseEditAdapter) binding.assessmentRecyclerInEditCourse.getAdapter()).setData(assessmentList);
            assessmentT.addAll(assessmentList);
        });


        viewModel.getUnassignedAssessments().observe(getViewLifecycleOwner(), unassCor -> {
            unassignedAssessments.addAll(unassCor);
        });

    }

    private void initRecyclerview() {

        binding.assessmentRecyclerInEditCourse.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.assessmentRecyclerInEditCourse.setHasFixedSize(true);
        binding.assessmentRecyclerInEditCourse.setAdapter(new CourseEditAdapter(getContext(), this));

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(binding.assessmentRecyclerInEditCourse);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            courseIdNull = null;
            adapter = (CourseEditAdapter) binding.assessmentRecyclerInEditCourse.getAdapter();
            int swipedPosition = viewHolder.getAbsoluteAdapterPosition();
            assessmentToUnlink = adapter.getAssessmentInCourse(swipedPosition);
            assessment_Id = assessmentToUnlink.getAssessment_id();
            Integer id = assessmentToUnlink.getCourse_id_FK();
            String assessment_title = assessmentToUnlink.getAssessment_title();
            viewModel.unlinkAssessmentFromCourse(courseIdNull, assessment_Id);
            Toast.makeText(getContext(), assessment_title + " Removed From Course", Toast.LENGTH_SHORT).show();
            unassignedAssessments.clear();
        }
    };

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
                startCourse = startLD.format(f);
                LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second), ZoneOffset.UTC);
                LocalDate endLD = end.toLocalDate();
                endCourse = endLD.format(f);
                binding.courseDurationEdit.setText(startCourse + "  -  " + endCourse);
                Toast toast = Toast.makeText(getContext(), getString(R.string.courseDateStartLabel) + startCourse + "\n" + getString(R.string.courseDateEndLabel) + endCourse, Toast.LENGTH_SHORT);
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
        if (binding.courseNameEdit.getText().toString().trim().isEmpty()) {
            binding.courseName.setError("Enter Name");
            requestFocus(binding.courseNameEdit);
            return false;
        }
        return true;
    }

    private void submit_form() {
        if (!validate_name()) {
            return;
        } else {
            course.setCourse_title(courseName);
            course.setCourse_start(startCourse);
            course.setCourse_end(endCourse);
            course.setCourse_notes(courseNotes);
            course.setStatus(spinnerLabel);
            course.setMentor_name(mentorName);
            course.setMentor_email(mentorEmail);
            course.setMentor_phone(mentorPhone);
            course.setTerm_id_FK(termIdFK);
            viewModel.update(course);
            Toast.makeText(getContext(), "Course " + courseName + " Updated", Toast.LENGTH_SHORT).show();
            NavController controller = Navigation.findNavController(binding.getRoot());
            controller.popBackStack(R.id.fragment_term, false);
            controller.navigate(R.id.action_editCourse_to_courseList);

        }
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_status,
                        getResources().getStringArray(R.array.spinner_array));

        binding.statusDropDown.setAdapter(adapter);
        binding.statusDropDown.setText(spinnerLabel, false);
    }


    @Override
    public void onClicked(Assessment assessment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("assessment", assessment);
        NavController controller = Navigation.findNavController(binding.getRoot());
        controller.navigate(R.id.action_courseEdit_to_assessmentEdit, bundle);
    }

    private void addAssessmentDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog);
        builder.setTitle("Select Assessment");
        String[] assessment = new String[unassignedAssessments.size()];
        if (unassignedAssessments != null) {

            for (int i = 0; i < unassignedAssessments.size(); i++) {
                assessment[i] = unassignedAssessments.get(i).getAssessment_title();
            }
            builder.setSingleChoiceItems(assessment, -1, (dialog, which) -> {
                getData = assessment[which];

            });
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    viewModel.addAssessmentToCourse(courseId, getData);
                    Toast.makeText(getContext(), getData + " Added To Course", Toast.LENGTH_SHORT).show();
                    unassignedAssessments.clear();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create();
            builder.show();
        }
    }


}