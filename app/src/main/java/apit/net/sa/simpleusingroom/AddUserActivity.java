package apit.net.sa.simpleusingroom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUserActivity extends AppCompatActivity {

    @BindView(R.id.userName)
    EditText userET;
    @BindView(R.id.userAge)
    EditText userAge;
    String name;
    String age;
    Boolean isEdited;
    UserEntity userEntities = new UserEntity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);



        isEdited = getIntent().getBooleanExtra("edit",false);
        userEntities = (UserEntity) getIntent().getSerializableExtra("user");
        if(isEdited){
            if(userEntities!=null){
                userET.setText(userEntities.getUserName());
                userAge.setText(userEntities.getAge());
            }
        }

    }

    @OnClick(R.id.addBtn)

    public void AddBtn(){
        if(isEdited){
            name = userET.getText().toString();
            age = userAge.getText().toString();
            new updateAsync().execute();
        }
        else {
            name = userET.getText().toString();
            age = userAge.getText().toString();
            new MyAsync().execute();
        }
    }

        class MyAsync extends AsyncTask<Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUserName(name);
                userEntity.setAge(age);

                DatabaseSingInstance.getInstance(AddUserActivity.this)
                        .getOurDatabase()
                        .userDAO()
                        .InsertUser(userEntity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(AddUserActivity.this,"user added success",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddUserActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        class updateAsync extends AsyncTask<Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                userEntities.setUserName(name);
                userEntities.setAge(age);
                DatabaseSingInstance.getInstance(AddUserActivity.this)
                        .getOurDatabase()
                        .userDAO()
                        .updateUser(userEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(AddUserActivity.this,"updated success",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddUserActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }




}
