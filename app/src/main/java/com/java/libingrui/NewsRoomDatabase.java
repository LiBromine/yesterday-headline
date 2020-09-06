package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {News.class, NewsList.class}, version = 1, exportSchema = false)
abstract class NewsRoomDatabase extends RoomDatabase {

    abstract NewsDao newsDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile NewsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static NewsRoomDatabase getDatabase(final Context context) {
        if( INSTANCE == null) {
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
                    NewsDao dao = INSTANCE.newsDao();
                    dao.deleteAllNews();
                    dao.deleteAllNewsList();
                }
            });
        }
    };
}
