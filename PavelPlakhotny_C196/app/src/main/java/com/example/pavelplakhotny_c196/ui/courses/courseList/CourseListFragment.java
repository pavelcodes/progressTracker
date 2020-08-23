package com.example.pavelplakhotny_c196.ui.courses.courseList;

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

import com.example.pavelplakhotny_c196.CourseBroadcast;
import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.databinding.FragmentCourseBinding;
import com.example.pavelplakhotny_c196.ui.courses.addCourse.CourseAddActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class CourseListFragment extends Fragment implements CourseListAdapter.OnCourseListener, CourseListAdapter.OnCourseLongClickListener {

    private CourseListViewModel viewModel;


    private FragmentCourseBinding binding;
    private LocalDateTime notifyDate;
    private CourseListAdapter adapter;
    private String getData = "";
    private Boolean setAlarmStartBoolean;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private long notifyDateLong;
    private static int requestCode;

    private CourseListAdapter.OnCourseLongClickListener onCourseLongClickListener;

    public static CourseListFragment newInstance() {
        return new CourseListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_course, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        initRecyclerview();


        binding.fabAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CourseAddActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CourseListViewModel.class);
        viewModel.getCourseList().observe(getViewLifecycleOwner(), courses -> {
            ((CourseListAdapter) binding.courseRecyclerView.getAdapter()).setData(courses);
        });

    }


    private void initRecyclerview() {

        binding.courseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.courseRecyclerView.setHasFixedSize(true);
        binding.courseRecyclerView.setAdapter(new CourseListAdapter(getContext(), this::onClicked, this::onLongClicked));

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(binding.courseRecyclerView);
    }

    @Override
    public void onClicked(Course course) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("course", course);
        NavController controller = Navigation.findNavController(binding.getRoot());
        controller.navigate(R.id.action_courseListFragment_to_editCourseFragment, bundle);

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter = (CourseListAdapter) binding.courseRecyclerView.getAdapter();
            int swipedPosition = viewHolder.getAbsoluteAdapterPosition();
            viewModel.delete(adapter.getCourseAt(swipedPosition));
            Toast.makeText(getContext(), "Course Deleted", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onLongClicked(Course course) {
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
                String startCourse = course.getCourse_start();
                String endCourse = course.getCourse_end();
                LocalDate start = LocalDate.parse(startCourse, f);
                LocalDate end = LocalDate.parse(endCourse, f);

                if (setAlarmStartBoolean) {
                    notifyDate = LocalDateTime.of(start, selectedTime);
                } else {
                    notifyDate = LocalDateTime.of(end, selectedTime);
                }
                ZoneId zoneId = ZoneId.systemDefault();
                notifyDateLong = notifyDate.atZone(zoneId).toEpochSecond() * 1000;
                Toast.makeText(getContext(), "Alert Set", Toast.LENGTH_SHORT).show();
                sendNotificationAlert();
                Log.i("TimePicker", "Time picker set from id ");
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

    public void sendNotificationAlert() {
        Intent intent = new Intent(getActivity().getApplicationContext(), CourseBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notifyDateLong, pendingIntent);


    }
}
