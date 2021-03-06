package com.example.otto.sql.job;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Job.class, version = 1, exportSchema = false)
public abstract class JobDatabase extends RoomDatabase {

    private static JobDatabase instance;

    public abstract JobDao jobDao();

    public static synchronized JobDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), JobDatabase.class, "job_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
    return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private JobDao jobDao;

        private PopulateDbAsyncTask(JobDatabase db) {
            jobDao = db.jobDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jobDao.insert(new Job("ICA AB", "Change out the server rack with the servers. Pull new ethernet cables for each terminal and self shopping wifi kiosk AP. Job has to be done after the closing time.", 1));
            return null;
        }
    }
}
