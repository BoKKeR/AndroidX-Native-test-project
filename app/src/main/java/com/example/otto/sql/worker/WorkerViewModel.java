package com.example.otto.sql.worker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkerViewModel extends AndroidViewModel {

    private WorkerRepository repository;
    private LiveData<List<Worker>> allWorkers;


    public WorkerViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkerRepository(application);
        allWorkers = repository.getAllWorkers();
    }

    public void insert(Worker worker) {
        repository.insert(worker);

    }

    public void update(Worker worker) {
        repository.update(worker);
    }

    public void delete(Worker worker){
        repository.delete(worker);
    }

    public LiveData<List<Worker>> getAllWorkers() {
        return allWorkers;
    }
}
