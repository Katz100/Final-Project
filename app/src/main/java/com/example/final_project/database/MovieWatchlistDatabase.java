package com.example.final_project.database;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.final_project.MainActivity;
import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.MovieDAO;


@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class MovieWatchlistDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "MovieWatchlistDatabase";
    private static volatile MovieWatchlistDatabase INSTANCE;
    public static MovieWatchlistDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieWatchlistDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MovieWatchlistDatabase.class, DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            //TODO: Uncomment and implement this if you add default MovieWatchlist data later
//            databaseWriteExecutor.execute(() -> {
//                UserDAO dao = INSTANCE.userDAO();
//                dao.deleteAll();
//                User admin = new User("admin1", "admin1");
//                admin.setAdmin(true);
//                dao.insert(admin);
//                User testUser1 = new User("testuser1", "testuser1");
//                dao.insert(testUser1);
//            });
        }
    };
    public abstract MovieDAO movieDAO();

    //TODO: userDAO
}
