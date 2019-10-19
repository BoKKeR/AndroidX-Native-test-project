package com.example.otto;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    //private AppBarConfiguration mAppBarConfiguration;
    private JobViewModel jobViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //mAppBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.nav_calendar, R.id.nav_jobs, R.id.nav_billing,
        //        R.id.nav_employees)
        //        .setDrawerLayout(drawer)
        //        .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);

        FloatingActionButton buttonAddJob = findViewById(R.id.button_add_job);
        buttonAddJob.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddJobActivity.class);
                        startActivityForResult(intent, 1);
                    }
                }

        );

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final JobAdaptor adapter = new JobAdaptor();
        recyclerView.setAdapter(adapter);


        jobViewModel = ViewModelProviders.of(this).get(JobViewModel.class);
        jobViewModel.getAllJobs().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> jobs) {
                adapter.setJobs(jobs);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            String title = data.getStringExtra(AddJobActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddJobActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddJobActivity.EXTRA_PRIORITY, 1);

            Job job = new Job(title, description, priority);
            jobViewModel.insert(job);

            Toast.makeText(this,"Job saved", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Job not saved", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //@Override
    //public boolean onSupportNavigateUp() {
    //    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    //    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
    //            || super.onSupportNavigateUp();
    //}
}
