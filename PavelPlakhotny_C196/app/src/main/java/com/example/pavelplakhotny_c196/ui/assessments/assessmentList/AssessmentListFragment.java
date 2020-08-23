package com.example.pavelplakhotny_c196.ui.assessments.assessmentList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pavelplakhotny_c196.AssessmentBroadcast;
import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.databinding.FragmentAssessmentBinding;
import com.example.pavelplakhotny_c196.ui.assessments.assessmentAdd.AddAssessmentActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AssessmentListFragment extends Fragment implements AssessmentListAdapter.onAssessmentListener, AssessmentListAdapter.OnAssessmentLongClickListener {


    private AssessmentsListViewModel assessmentsListViewModel;

    private FragmentAssessmentBinding binding;

    private AssessmentListAdapter adapter;
    private String getData = "";
    private Boolean setAlarmStartBoolean;
    private LocalDateTime notifyDate;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private long notifyDateLong;
    private static int requestCode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_assessment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        initRecyclerview();
        binding.fabAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAssessmentActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assessmentsListViewModel = new ViewModelProvider(requireActivity()).get(AssessmentsListViewModel.class);
        assessmentsListViewModel.getAssessmentList().observe(getViewLifecycleOwner(), assessments -> {
            ((AssessmentListAdapter) binding.assessmentRecyclerView.getAdapter()).setData(assessments);
        });
    }

    private void initRecyclerview() {

        binding.assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.assessmentRecyclerView.setHasFixedSize(true);
        binding.assessmentRecyclerView.setAdapter(new AssessmentListAdapter(getContext(), this::onClicked, this::onLongClicked));

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(binding.assessmentRecyclerView);
    }

    @Override
    public void onClicked(Assessment assessment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("assessment", assessment);
        NavController controller = Navigation.findNavController(binding.getRoot());
        controller.popBackStack(R.id.fragment_assessment, false);
        controller.navigate(R.id.action_assessmentListFragment_to_editAssessmentFragment, bundle);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter = (AssessmentListAdapter) binding.assessmentRecyclerView.getAdapter();
            int swipedPosition = viewHolder.getAbsoluteAdapterPosition();
            assessmentsListViewModel.delete(adapter.getAssessmentAt(swipedPosition));
            Toast.makeText(getContext(), "Assessment Deleted", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onLongClicked(Assessment assessment) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog);
        builder.setTitle("Set Notification For:");
        String[] courseOptions = {"Start Date", "End Date"};

        builder.setSingleChoiceItems(courseOptions, -1, (dialog, which) -> {
            getData = courseOptions[which];

        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                setAlarmStartBoolean = getData.equals("Start Date");
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        timeSetListener,
                        hour, min, true);
                timePickerDialog.show();
            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalTime selectedTime = LocalTime.of(hourOfDay, minute, 0, 0);
                String startAssessment = assessment.getStart_date();
                String endAssessment = assessment.getDue_date();
                LocalDate start = LocalDate.parse(startAssessment, f);
                LocalDate end = LocalDate.parse(endAssessment, f);

                if (setAlarmStartBoolean) {
                    notifyDate = LocalDateTime.of(start, selectedTime);
                } else {
                    notifyDate = LocalDateTime.of(end, selectedTime);
                }
                ZoneId zoneId = ZoneId.systemDefault();
                notifyDateLong = notifyDate.atZone(zoneId).toEpochSecond() * 1000;
                Intent intent = new Intent(getContext(), AssessmentBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode++, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, notifyDateLong, pendingIntent);

                Toast.makeText(getContext(), "Alert Set", Toast.LENGTH_SHORT).show();
                Log.i("TimePicker", "Time picker set");
            }
        };


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
        return true;
    }
}
