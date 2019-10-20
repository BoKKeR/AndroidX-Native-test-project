package com.example.otto.sql.worker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkerRepository {
    private WorkerDao workerDao;
    private LiveData<List<Worker>> allWorkers;

    public WorkerRepository(Application application) {
        WorkerDatabase database = WorkerDatabase.getInstance(application);
        workerDao = database.workerDao();
        allWorkers = workerDao.getAllWorkers();

    }

    public void insert(Worker worker) {
        new InsertWorkerAsyncTask(workerDao).execute(worker);
    }

    public void update(Worker worker){
        new UpdateWorkerAsyncTask(workerDao).execute(worker);
    }

    public void delete(Worker worker) {
        new DeleteWorkerAsyncTask(workerDao).execute(worker);
    }

    public LiveData<List<Worker>> getAllWorkers(){
        return allWorkers;
    }

    private static class InsertWorkerAsyncTask extends AsyncTask<Worker, Void, Void> {
        private WorkerDao workerDao;

        private InsertWorkerAsyncTask(WorkerDao workerDao){
            this.workerDao = workerDao;
        }

        @Override
        protected Void doInBackground(Worker... workers) {
            workerDao.insert(workers[0]);
            return null;
        }
    }

    private static class UpdateWorkerAsyncTask extends AsyncTask<Worker, Void, Void> {
        private WorkerDao workerDao;

        private UpdateWorkerAsyncTask(WorkerDao workerDao){
            this.workerDao = workerDao;
        }

        @Override
        protected Void doInBackground(Worker... workers) {
            workerDao.update(workers[0]);
            return null;
        }
    }

    private static class DeleteWorkerAsyncTask extends AsyncTask<Worker, Void, Void> {
        private WorkerDao workerDao;

        private DeleteWorkerAsyncTask(WorkerDao workerDao){
            this.workerDao = workerDao;
        }

        @Override
        protected Void doInBackground(Worker... workers) {
            workerDao.delete(workers[0]);
            return null;
        }
    }
}
