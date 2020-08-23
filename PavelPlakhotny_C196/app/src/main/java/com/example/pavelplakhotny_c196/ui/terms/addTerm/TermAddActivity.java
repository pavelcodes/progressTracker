package com.example.pavelplakhotny_c196.ui.terms.addTerm;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Term;
import com.example.pavelplakhotny_c196.databinding.ActivityAddTermBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;

public class TermAddActivity extends AppCompatActivity {

    private ActivityAddTermBinding binding;
    private String startTermString;
    private String endTermString;
    private String termName;
    private Term term;
    private TermAddViewModel termAddViewModel;
    private ExecutorService executor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_add_term);
        setAppBar();
        termAddViewModel = new ViewModelProvider(this).get(TermAddViewModel.class);

    showDatePicker();

        binding.submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_form();

            }
        });
    }

    private void showDatePicker() { MaterialDatePicker.Builder<Pair<Long,Long>> builder= MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Term Duration Dates").setTheme(R.style.MaterialCalendarTheme_RangeFill);
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();


        binding.termDateEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(materialDatePicker!=null && materialDatePicker.isVisible()) {
                    //do nothing
                } else {
                    materialDatePicker.show(getSupportFragmentManager(),"date picker");                }

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long , Long>>(){
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {

                DateTimeFormatter f = DateTimeFormatter.ofPattern( "MM/dd/yyyy") ;
                LocalDateTime start= LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.first), ZoneOffset.UTC);
                LocalDate startLD= start.toLocalDate();
                startTermString= startLD.format(f);
                LocalDateTime end= LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second), ZoneOffset.UTC);
                LocalDate endLD= end.toLocalDate();
                endTermString = endLD.format(f);
                termName=binding.termNameEdit.getText().toString().trim();
                binding.termDateEdit .setText(startTermString + "  -  " + endTermString);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.dateStartLabel)+startTermString + "\n"+ getString(R.string.dateEndLabel) + endTermString,  Toast.LENGTH_SHORT);
                toast.show();

            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //request focus
    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        if(!validate_name())
        {return;}
        else {
            term= new Term(termName,startTermString,endTermString);
            termAddViewModel.saveTerm(term);
            finish();

        }
    }


    private void setAppBar() {
        getSupportActionBar().setTitle("Add Term");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_app_logo);
        getWindow().setStatusBarColor(Color.BLACK);
    }
}
