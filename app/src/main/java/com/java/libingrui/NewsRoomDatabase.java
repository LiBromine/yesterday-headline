package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.util.Log;

@Database(entities = {News.class, NewsList.class, EpidemicInfo.class, EntityData.class, NewsEntityCrossRef.class}, version = 1, exportSchema = false)
abstract class NewsRoomDatabase extends RoomDatabase {

    abstract NewsDao newsDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile NewsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static NewsRoomDatabase getDatabase(final Context context) {
        if( INSTANCE == null) {
            Log.v("debug","new db");
            synchronized (NewsRoomDatabase.class) {
                if( INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsRoomDatabase.class,"News_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Log.v("debug","db callback");
                    NewsDao dao = INSTANCE.newsDao();
                    dao.deleteAllNews();
                    dao.deleteAllNewsList();
                    dao.deleteAllEpidemicInfo();
                    dao.deleteAllEntityData();
                    dao.deleteAllNewsEntityCrossRef();
                }
            });
        }
    };
}
