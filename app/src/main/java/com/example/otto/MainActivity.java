package com.example.otto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    public static final int ADD_JOB_REQUEST = 1;
    public static final int EDIT_JOB_REQUEST = 2;
    private JobViewModel jobViewModel;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new JobFragment()).commit();
                break;

            case R.id.nav_people:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WorkerFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new JobFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        FloatingActionButton buttonAddJob = findViewById(R.id.button_add_job);
        buttonAddJob.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddEditJobActivity.class);
                        startActivityForResult(intent, 1);
                    }
                }

        );

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intent = new Intent(MainActivity.this, AddEditJobActivity.class);
                intent.putExtra(AddEditJobActivity.EXTRA_ID, job.getId());
                intent.putExtra(AddEditJobActivity.EXTRA_TITLE, job.getTitle());
                intent.putExtra(AddEditJobActivity.EXTRA_DESCRIPTION, job.getDescription());
                intent.putExtra(AddEditJobActivity.EXTRA_PRIORITY, job.getPriority());

                startActivityForResult(intent, EDIT_JOB_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ADD_JOB_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditJobActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditJobActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditJobActivity.EXTRA_PRIORITY, 1);

            Job job = new Job(title, description, priority);
            jobViewModel.insert(job);

            Toast.makeText(this, "Job saved", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_JOB_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditJobActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Job cant be updated", Toast.LENGTH_LONG);
                return;
            }

            String title = data.getStringExtra(AddEditJobActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditJobActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditJobActivity.EXTRA_PRIORITY, 1);

            Job job = new Job(title, description, priority);
            job.setId(id);

            jobViewModel.update(job);
            Toast.makeText(this, "Job updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Job not saved", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
