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

import com.example.otto.R;
import com.example.otto.activity.AddEditWorkerActivity;
import com.example.otto.sql.worker.Worker;
import com.example.otto.sql.worker.WorkerAdapter;
import com.example.otto.sql.worker.WorkerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class WorkerFragment extends Fragment {

    public static final int ADD_WORKER_REQUEST = 1;
    public static final int EDIT_WORKER_REQUEST = 2;
    private WorkerViewModel workerViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_workers, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton buttonAddWorker = getView().findViewById(R.id.button_add_worker);
        buttonAddWorker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), AddEditWorkerActivity.class);
                        startActivityForResult(intent, 1);
                    }
                }

        );

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        final WorkerAdapter adapter = new WorkerAdapter();
        recyclerView.setAdapter(adapter);


        workerViewModel = ViewModelProviders.of(this).get(WorkerViewModel.class);
        workerViewModel.getAllWorkers().observe(this, new Observer<List<Worker>>() {
            @Override
            public void onChanged(List<Worker> workers) {
                adapter.setWorkers(workers);
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
                workerViewModel.delete(adapter.getWorkerAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new WorkerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Worker worker) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddEditWorkerActivity.class);
                intent.putExtra(AddEditWorkerActivity.EXTRA_ID, worker.getId());
                intent.putExtra(AddEditWorkerActivity.EXTRA_TITLE, worker.getTitle());
                intent.putExtra(AddEditWorkerActivity.EXTRA_DESCRIPTION, worker.getDescription());
                intent.putExtra(AddEditWorkerActivity.EXTRA_PRIORITY, worker.getPriority());

                startActivityForResult(intent, EDIT_WORKER_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ADD_WORKER_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditWorkerActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditWorkerActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditWorkerActivity.EXTRA_PRIORITY, 1);

            Worker worker = new Worker(title, description, priority);
            workerViewModel.insert(worker);

        } else if (requestCode == EDIT_WORKER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditWorkerActivity.EXTRA_ID, -1);
            if (id == -1) {
                return;
            }

            String title = data.getStringExtra(AddEditWorkerActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditWorkerActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditWorkerActivity.EXTRA_PRIORITY, 1);

            Worker worker = new Worker(title, description, priority);
            worker.setId(id);

            workerViewModel.update(worker);

        } else {

        }
    }
}
