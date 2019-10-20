package com.example.otto.sql.job;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JobDao {

    @Insert
    void insert(Job job);

    @Update
    void update(Job job);

    @Delete
    void delete(Job job);

    @Query("SELECT * FROM job_table ORDER BY priority DESC")
    LiveData<List<Job>> getAllJobs();
}
