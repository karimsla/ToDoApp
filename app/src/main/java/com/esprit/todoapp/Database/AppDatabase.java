package com.esprit.todoapp.Database;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.esprit.todoapp.Model.TodoList;
import com.esprit.todoapp.Model.TodoListItem;
import com.esprit.todoapp.Model.User;

import androidx.room.TypeConverters;

@Database(entities = {User.class, TodoList.class, TodoListItem.class}, version = 2, exportSchema = false)
@TypeConverters({com.esprit.todoapp.Database.TypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract com.esprit.todoapp.Database.DAO getDAO();

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