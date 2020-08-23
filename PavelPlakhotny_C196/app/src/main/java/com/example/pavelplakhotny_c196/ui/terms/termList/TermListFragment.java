package com.example.pavelplakhotny_c196.ui.terms.termList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Term;
import com.example.pavelplakhotny_c196.databinding.FragmentTermBinding;
import com.example.pavelplakhotny_c196.ui.terms.addTerm.TermAddActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TermListFragment extends Fragment implements TermListAdapter.OnTermListener {

    private TermListViewModel tTermListViewModel;

    private FragmentTermBinding termFragmentBinding;

    private TermListAdapter adapter;



    public static TermListFragment newInstance() {
        return new TermListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        termFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_term, container, false);
        termFragmentBinding.setLifecycleOwner(getViewLifecycleOwner());
        initRecyclerview();

        termFragmentBinding.fabAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TermAddActivity.class);
                startActivity(intent);
            }
        });
        return termFragmentBinding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tTermListViewModel = new ViewModelProvider(requireActivity()).get(TermListViewModel.class);
        tTermListViewModel.getTermList().observe(getViewLifecycleOwner(), terms -> {
            ((TermListAdapter) termFragmentBinding.termRecyclerView.getAdapter()).setData(terms);
        });


    }


    private void initRecyclerview() {

        termFragmentBinding.termRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        termFragmentBinding.termRecyclerView.setHasFixedSize(true);
        termFragmentBinding.termRecyclerView.setAdapter(new TermListAdapter(getContext(),this));

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(termFragmentBinding.termRecyclerView);
    }


    @Override
    public void onClicked(Term term) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("term",term);
        NavController controller = Navigation.findNavController(termFragmentBinding.getRoot());
        controller.popBackStack(R.id.fragment_term, false);
        controller.navigate(R.id.action_termListFragment_to_editTermFragment,bundle);


    }


    final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter = (TermListAdapter) termFragmentBinding.termRecyclerView.getAdapter();
            int swipedPosition = viewHolder.getAbsoluteAdapterPosition();
            Term term = adapter.getTermAt(swipedPosition);
            Integer termid = term.getTerm_id();
            AtomicInteger courseCount = new AtomicInteger();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> courseCount.set(tTermListViewModel.checkIfCoursesAssignedToTerm(termid)));
            executor.shutdown();
            try {
                 executor.awaitTermination(4, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\n\n");

            if (courseCount.get() > 0) {
                adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                Toast.makeText(getContext(), "Unassigned Courses Before Deleting", Toast.LENGTH_LONG).show();
                return;
            } else {
                String termTitle = term.getTerm_name();
                tTermListViewModel.delete(adapter.getTermAt(swipedPosition));
                Toast.makeText(getContext(), termTitle +" Deleted", Toast.LENGTH_SHORT).show();
            }

        }
    };
}





