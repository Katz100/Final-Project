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
import com.example.final_project.database.entities.User;
import com.example.final_project.database.entities.UserWatchList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Movie.class, UserWatchList.class, User.class}, version = 1, exportSchema = false)
public abstract class MovieWatchlistDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "MovieWatchlistDatabase";

    public static final String USER_WATCH_LIST_TABLE = "userWatchListTable";

    public static final String USER_TABLE = "userTable";

    private static volatile MovieWatchlistDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
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
            db.execSQL("PRAGMA foreign_keys=ON"); // Enable foreign key constraints
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            //TODO: Uncomment and implement this if you add default MovieWatchlist data later
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                MovieDAO movieDAO = INSTANCE.movieDAO();
                UserWatchListDAO userWatchListDAO = INSTANCE.userWatchListDAO();

                dao.deleteAll();
                User admin = new User("admin1", "admin1", true);
                admin.setAdmin(true);
                dao.insert(admin);
                User admin2 = new User("admin2", "admin2", true);
                admin2.setAdmin(true);
                dao.insert(admin2);
                User testUser1 = new User("testuser1", "testuser1", false);
                dao.insert(testUser1);
                User testUser2 = new User("testuser2", "testuser2", false);
                dao.insert(testUser2);

                for (int i = 0; i < 10; i++) {
                    movieDAO.insert(new Movie("Movie" + i, "Comedy"));
                }

                userWatchListDAO.insert(new UserWatchList(1, 1, false, 0.0));
                userWatchListDAO.insert(new UserWatchList(1, 2, true, 0.0));
                userWatchListDAO.insert(new UserWatchList(2, 3, false, 0.0));
            });
        }
    };

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
    public abstract MovieDAO movieDAO();

    public abstract UserDAO userDAO();

    public abstract UserWatchListDAO userWatchListDAO();

}