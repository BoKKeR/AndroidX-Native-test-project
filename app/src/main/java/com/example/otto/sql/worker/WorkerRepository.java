package com.example.otto.sql.worker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkerRepository {
    private WorkerDao workerDao;
    private LiveData<List<Worker>> allJobs;

    public WorkerRepository(Application application) {
        WorkerDatabase database = WorkerDatabase.getInstance(application);
        workerDao = database.jobDao();
        allJobs = workerDao.getAllJobs();

    }

    public void insert(Worker worker) {
        new InsertJobAsyncTask(workerDao).execute(worker);
    }

    public void update(Worker worker){
        new UpdateJobAsyncTask(workerDao).execute(worker);
    }

    public void delete(Worker worker) {
        new DeleteJobAsyncTask(workerDao).execute(worker);
    }

    public LiveData<List<Worker>> getAllJobs(){
        return allJobs;
    }

    private static class InsertJobAsyncTask extends AsyncTask<Worker, Void, Void> {
        private WorkerDao workerDao;

        private InsertJobAsyncTask(WorkerDao workerDao){
            this.workerDao = workerDao;
        }

        @Override
        protected Void doInBackground(Worker... workers) {
            workerDao.insert(workers[0]);
            return null;
        }
    }

    private static class UpdateJobAsyncTask extends AsyncTask<Worker, Void, Void> {
        private WorkerDao workerDao;

        private UpdateJobAsyncTask(WorkerDao workerDao){
            this.workerDao = workerDao;
        }

        @Override
        protected Void doInBackground(Worker... workers) {
            workerDao.update(workers[0]);
            return null;
        }
    }

    private static class DeleteJobAsyncTask extends AsyncTask<Worker, Void, Void> {
        private WorkerDao workerDao;

        private DeleteJobAsyncTask(WorkerDao workerDao){
            this.workerDao = workerDao;
        }

        @Override
        protected Void doInBackground(Worker... workers) {
            workerDao.delete(workers[0]);
            return null;
        }
    }
}
