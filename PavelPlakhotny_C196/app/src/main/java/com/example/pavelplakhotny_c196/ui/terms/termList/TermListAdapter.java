package com.example.pavelplakhotny_c196.ui.terms.termList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.database.entity.Term;
import com.example.pavelplakhotny_c196.databinding.TermContentBinding;

import java.util.ArrayList;
import java.util.List;


public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.TermListViewHolder> {

    private static final String TAG = "TermListAdapter";
    private List<Term> mTerms;
    private Context tContext;
    private OnTermListener listener;

    public Context getContext() {
        return tContext;
    }

    public TermListAdapter(Context context, OnTermListener listener) {
       this.tContext=context;
       this.listener = listener;
       mTerms = new ArrayList<>();
    }
    public void setData(List<Term> terms){
        mTerms.clear();
        mTerms.addAll(terms);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TermListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        TermContentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(tContext), R.layout.term_content, parent, false);
      return new TermListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TermListViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        holder.bind(mTerms.get(position));
    }


    @Override
    public int getItemCount() {
        return mTerms.size();
    }

    public Term getTermAt(int position) {
        return mTerms.get(position);
    }


    public class TermListViewHolder extends RecyclerView.ViewHolder  {
        public TermContentBinding binding;


        public TermListViewHolder(@NonNull TermContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(Term term) {
            binding.getRoot().setOnClickListener(v -> listener.onClicked(binding.getTerm()));
            binding.setTerm(term);
            binding.executePendingBindings();
        }


    }

    public interface OnTermListener {
        void onClicked(Term term);
    }


    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(Term term);
    }
}

