package com.example.otto.sql.worker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkerDao {

    @Insert
    void insert(Worker worker);

    @Update
    void update(Worker worker);

    @Delete
    void delete(Worker worker);

    @Query("SELECT * FROM worker_table ORDER BY priority DESC")
    LiveData<List<Worker>> getAllWorkers();
}
