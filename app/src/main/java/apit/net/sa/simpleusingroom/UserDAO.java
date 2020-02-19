package apit.net.sa.simpleusingroom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;


@Dao
public interface UserDAO {
    @Query("SELECT * FROM userentity")
    public List<UserEntity> getUserS();

    @Query("SELECT * FROM userentity WHERE userName LIKE:name")
    public UserEntity getSpecificUser(String name);

    @Insert
     public void InsertUser(UserEntity userEntity);

    @Update
    public void updateUser(UserEntity userEntity);

    @Delete
    public void deleteUser(UserEntity userEntity);

    @Query("SELECT * FROM userentity")
    Maybe<List<UserEntity>> getUsersSingle();

}
