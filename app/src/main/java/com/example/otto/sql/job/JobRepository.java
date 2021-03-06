package com.example.otto.sql.job;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class JobRepository {
    private JobDao jobDao;
    private LiveData<List<Job>> allJobs;

    public JobRepository(Application application) {
        JobDatabase database = JobDatabase.getInstance(application);
        jobDao = database.jobDao();
        allJobs = jobDao.getAllJobs();

    }

    public void insert(Job job) {
        new InsertJobAsyncTask(jobDao).execute(job);
    }

    public void update(Job job){
        new UpdateJobAsyncTask(jobDao).execute(job);
    }

    public void delete(Job job) {
        new DeleteJobAsyncTask(jobDao).execute(job);
    }

    public LiveData<List<Job>> getAllJobs(){
        return allJobs;
    }

    private static class InsertJobAsyncTask extends AsyncTask<Job, Void, Void> {
        private JobDao jobDao;

        private InsertJobAsyncTask(JobDao jobDao){
            this.jobDao = jobDao;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            jobDao.insert(jobs[0]);
            return null;
        }
    }

    private static class UpdateJobAsyncTask extends AsyncTask<Job, Void, Void> {
        private JobDao jobDao;

        private UpdateJobAsyncTask(JobDao jobDao){
            this.jobDao = jobDao;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            jobDao.update(jobs[0]);
            return null;
        }
    }

    private static class DeleteJobAsyncTask extends AsyncTask<Job, Void, Void> {
        private JobDao jobDao;

        private DeleteJobAsyncTask(JobDao jobDao){
            this.jobDao = jobDao;
        }

        @Override
        protected Void doInBackground(Job... jobs) {
            jobDao.delete(jobs[0]);
            return null;
        }
    }
}
