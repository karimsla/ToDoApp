package com.example.todoapp.Database;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.todoapp.Model.TodoList;
import com.example.todoapp.Model.TodoListItem;
import com.example.todoapp.Model.User;

import androidx.room.TypeConverters;

@Database(entities = {User.class, TodoList.class, TodoListItem.class}, version = 2, exportSchema = false)
@TypeConverters({com.example.todoapp.Database.TypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract com.example.todoapp.Database.DAO getDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDb(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, "todolists")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}