package com.java.libingrui;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(entities={NewsAbstract.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase{
    public abstract NewsAbstractDao newsAbstractDao();
}

