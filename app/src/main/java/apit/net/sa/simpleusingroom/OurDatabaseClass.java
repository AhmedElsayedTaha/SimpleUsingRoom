package apit.net.sa.simpleusingroom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {UserEntity.class},version = 3,exportSchema = false)
public abstract class OurDatabaseClass extends RoomDatabase {
    public abstract UserDAO userDAO();
}
