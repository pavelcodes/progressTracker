package com.example.pavelplakhotny_c196.ui.terms.editTerm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.database.entity.Term;
import com.example.pavelplakhotny_c196.databinding.FragmentEditTermBinding;
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

public class EditTermFragment extends Fragment implements TermEditAdapter.OnCourseInTermListener {
    private TermEditViewModel viewModel;
    private FragmentEditTermBinding binding;
    private Term term;
    private TermEditAdapter adapter;
    private Course courseToUnlink;
    private String startTerm;
    private String endTerm;
    private String termName;
    private Integer termIdNull;
    private Integer termId;
    private String getData = "";
    private String a;
    private List<Course> courseI = new ArrayList<>();
    private List<Course> courseT = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        term = (Term) getArguments().getSerializable("term");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_term, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setTerm(term);
        initRecyclerview();
        showDatePicker();


        startTerm = term.getTerm_start();
        endTerm = term.getTerm_end();
        termId = term.getTerm_id();
        binding.termDateEdit.setText(startTerm + " " + endTerm);
        termName=binding.termNameEdit.getText().toString().trim();



  binding.fabAddCourseToTerm.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          addCourseDialog();
      }
  });
        binding.editTermSaveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_form();
            }
        });

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TermEditViewModel.class);
        viewModel.getCourseListForTerm(termId).observe(getViewLifecycleOwner(), courses -> {
            ((TermEditAdapter) binding.courseRecyclerInEditTerm.getAdapter()).setData(courses);
            courseT.addAll(courses);
        });

        viewModel.getUnassignedCourses().observe(getViewLifecycleOwner(),unassCor ->{
            courseI.addAll(unassCor);
        });



    }
    private void initRecyclerview() {

        binding.courseRecyclerInEditTerm.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.courseRecyclerInEditTerm.setHasFixedSize(true);
        binding.courseRecyclerInEditTerm.setAdapter(new TermEditAdapter(getContext(), this));

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(binding.courseRecyclerInEditTerm);
    }

    @Override
    public void onClicked(Course course) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("course",course);
        NavController controller = Navigation.findNavController(binding.getRoot());
        controller.popBackStack(R.id.editAssessmentFragment, false);
        controller.navigate(R.id.action_termEdit_to_editCourse,bundle);

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            termIdNull = null;
            adapter= (TermEditAdapter) binding.courseRecyclerInEditTerm.getAdapter();
            int swipedPosition = viewHolder.getAbsoluteAdapterPosition();
            courseToUnlink = adapter.getCourseAtInTerm(swipedPosition);
            int course_id = courseToUnlink.getCourse_id();
            Integer id= courseToUnlink.getTerm_id_FK();
            viewModel.unlinkCourseFromTerm(termIdNull, course_id);
            String courseName= courseToUnlink.getCourse_title();
            courseI.clear();
            Toast.makeText(getContext(), courseName + " Removed From Term", Toast.LENGTH_SHORT).show();
        }
    };

    private void showDatePicker(){
        MaterialDatePicker.Builder<Pair<Long,Long>> builder= MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Term Duration Dates").setTheme(R.style.MaterialCalendarTheme_RangeFill);
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();


        binding.termDateEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(materialDatePicker!=null && materialDatePicker.isVisible()) {
                    //do nothing
                } else {
                    materialDatePicker.show(getChildFragmentManager(),"date picker");                }

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long , Long>>(){
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {

                DateTimeFormatter f = DateTimeFormatter.ofPattern( "MM/dd/yyyy") ;
                LocalDateTime start= LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.first), ZoneOffset.UTC);
                LocalDate startLD= start.toLocalDate();
                startTerm = startLD.format(f);
                LocalDateTime end= LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second), ZoneOffset.UTC);
                LocalDate endLD= end.toLocalDate();
                endTerm = endLD.format(f);
                binding.termDateEdit.setText(startTerm + "  -  " + endTerm);
                Toast toast = Toast.makeText(getContext(), getString(R.string.dateStartLabel)+startTerm + "\n"+ getString(R.string.dateEndLabel) + endTerm,  Toast.LENGTH_SHORT);
                toast.show();

            }

        });
    }


    //request focus
    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validate_name() {
        if (binding.termNameEdit.getText().toString().trim().isEmpty()) {
            binding.termName.setError("Enter Name");
            requestFocus(binding.termNameEdit);
            return false;
        }
        return true;
    }

    private void submit_form()
    {
        if (validate_name()) {
            termName=binding.termNameEdit.getText().toString().trim();
            term.setTerm_name(termName);
            term.setTerm_start(startTerm);
            term.setTerm_end(endTerm);
            viewModel.update(term);
            Toast.makeText(getContext(), "Term "+ termName + " Updated", Toast.LENGTH_SHORT).show();
            NavController controller = Navigation.findNavController(binding.getRoot());
            controller.navigate(R.id.action_edit_to_termsList);
        }
    }

    private void addCourseDialog() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog);
        builder.setTitle("Select Course");

        String[] course = new String[courseI.size()];
        if (courseI != null) {

            for (int i = 0; i < courseI.size(); i++) {
                course[i] = courseI.get(i).getCourse_title();
            }
            builder.setSingleChoiceItems(course, -1, (dialog, which) -> {
                getData = course[which];

            });
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    viewModel.addCourseToTerm(termId,getData);
                    Toast.makeText(getContext(), getData + " Added To Term", Toast.LENGTH_SHORT).show();
                    courseI.clear();

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