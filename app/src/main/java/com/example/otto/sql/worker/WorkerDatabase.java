package com.example.otto.sql.worker;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Worker.class, version = 1, exportSchema = false)
public abstract class WorkerDatabase extends RoomDatabase {

    private static WorkerDatabase instance;

    public abstract WorkerDao workerDao();

    public static synchronized WorkerDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), WorkerDatabase.class, "worker_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
    return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private WorkerDao workerDao;

        private PopulateDbAsyncTask(WorkerDatabase db) {
            workerDao = db.workerDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workerDao.insert(new Worker("Peter", "Manin", 1));
            workerDao.insert(new Worker("Debbie", "Rozanne", 1));
            workerDao.insert(new Worker("Kaycee ", "Curtis", 1));
            workerDao.insert(new Worker("May", "Bertha", 1));
            workerDao.insert(new Worker("Gertrude", "Mora", 1));

            return null;
        }
    }
}
