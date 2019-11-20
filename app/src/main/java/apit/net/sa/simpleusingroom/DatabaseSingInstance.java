package apit.net.sa.simpleusingroom;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

public class DatabaseSingInstance {


    private OurDatabaseClass ourDatabaseClass;
    private static  DatabaseSingInstance ourInstance ;

    private static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
                 database.execSQL("CREATE TABLE user_new (userID INTEGER,userName TEXT,userAge TEXT," +
                         "PRIMARY KEY (user_new))");

                 database.execSQL("INSERT INTO user_new(userID,userName,userAge) SELECT userID,userName,userAge FROM UserEntity");

                 database.execSQL("DROP TABLE UserEntity");

                 database.execSQL("ALTER TABLE user_new RENAME TO UserEntity");
        }
    };

    public static DatabaseSingInstance getInstance(Context context) {
        if(ourInstance==null){
            ourInstance = new DatabaseSingInstance(context);
        }
        return ourInstance;
    }

    private DatabaseSingInstance(Context context) {
        ourDatabaseClass = Room.databaseBuilder(context,OurDatabaseClass.class,"userDB")
                .addMigrations(MIGRATION_1_2).build();

    }

    public  OurDatabaseClass getOurDatabase(){
        return ourDatabaseClass;
    }

}
