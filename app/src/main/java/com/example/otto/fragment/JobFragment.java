package com.example.otto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otto.sql.job.Job;
import com.example.otto.sql.job.JobAdapter;
import com.example.otto.sql.job.JobViewModel;
import com.example.otto.R;
import com.example.otto.activity.AddEditJobActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class JobFragment extends Fragment {

    public static final int ADD_JOB_REQUEST = 1;
    public static final int EDIT_JOB_REQUEST = 2;
    private JobViewModel jobViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_job, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton buttonAddJob = getView().findViewById(R.id.button_add_job);
        buttonAddJob.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), AddEditJobActivity.class);
                        startActivityForResult(intent, 1);
                    }
                }

        );

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        final JobAdapter adapter = new JobAdapter();
        recyclerView.setAdapter(adapter);


        jobViewModel = ViewModelProviders.of(this).get(JobViewModel.class);
        jobViewModel.getAllJobs().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> jobs) {
                adapter.setJobs(jobs);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                jobViewModel.delete(adapter.getJobAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new JobAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Job job) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddEditJobActivity.class);
                intent.putExtra(AddEditJobActivity.EXTRA_ID, job.getId());
                intent.putExtra(AddEditJobActivity.EXTRA_TITLE, job.getTitle());
                intent.putExtra(AddEditJobActivity.EXTRA_DESCRIPTION, job.getDescription());
                intent.putExtra(AddEditJobActivity.EXTRA_PRIORITY, job.getPriority());

                startActivityForResult(intent, EDIT_JOB_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ADD_JOB_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditJobActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditJobActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditJobActivity.EXTRA_PRIORITY, 1);

            Job job = new Job(title, description, priority);
            jobViewModel.insert(job);

            //Toast.makeText(this, "Worker saved", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_JOB_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditJobActivity.EXTRA_ID, -1);
            if (id == -1) {
                //Toast.makeText(this, "Worker cant be updated", Toast.LENGTH_LONG);
                return;
            }

            String title = data.getStringExtra(AddEditJobActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditJobActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditJobActivity.EXTRA_PRIORITY, 1);

            Job job = new Job(title, description, priority);
            job.setId(id);

            jobViewModel.update(job);
           // Toast.makeText(this, "Worker updated", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Worker not saved", Toast.LENGTH_LONG).show();
        }
    }
}
