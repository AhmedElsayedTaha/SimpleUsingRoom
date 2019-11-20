package apit.net.sa.simpleusingroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.noData)
    TextView noTv;
    @BindView(R.id.myRecycler)
    RecyclerView myRec;
    Unbinder unbinder;
    UserAdapter adapter;
    PopupWindow popupWindow;
    List<UserEntity> userEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        myRec.setLayoutManager(layoutManager);
        myRec.setItemAnimator(new DefaultItemAnimator());
        myRec.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        /*DatabaseSingInstance.getInstance(this)
                .getOurDatabase()
                .userDAO()
                .getUsersSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<List<UserEntity>>() {
                    @Override
                    public void onSuccess(final List<UserEntity> userEntities) {
                        adapter = new UserAdapter(userEntities, MainActivity.this, new UserAdapter.OnLongClickListner() {
                            @Override
                            public void onClick(int position) {
                                showPopupWindow(userEntities,position);
                            }
                        });
                        myRec.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
      /*  DatabaseSingInstance.getInstance(this)
                .getOurDatabase()
                .userDAO()
                .getUsersSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<UserEntity>>() {
                    @Override
                    public void onSuccess(final List<UserEntity> userEntities) {
                        adapter = new UserAdapter(userEntities, MainActivity.this, new UserAdapter.OnLongClickListner() {
                            @Override
                            public void onClick(int position) {
                                showPopupWindow(userEntities,position);
                            }
                        });
                        myRec.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });*/
       new userAsyncTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class userAsyncTask extends AsyncTask<Void,Void,List<UserEntity>> {

        @Override
        protected List<UserEntity> doInBackground(Void... voids) {
           return DatabaseSingInstance.getInstance(MainActivity.this)
                    .getOurDatabase()
                    .userDAO()
                    .getUserS();

        }

        @Override
        protected void onPostExecute(final List<UserEntity> userEntities) {
            super.onPostExecute(userEntities);
            if(userEntities!=null&&userEntities.size()>0){
                noTv.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"name is "+userEntities.get(0).getUserName(),Toast.LENGTH_LONG).show();
                adapter = new UserAdapter(userEntities, MainActivity.this, new UserAdapter.OnLongClickListner() {
                    @Override
                    public void onClick(int position) {
                        showPopupWindow(userEntities,position);
                    }
                });
                myRec.setAdapter(adapter);
                for(int i=0;i<userEntities.size();i++){
                    Log.e("data"," is "+userEntities.get(i).getUserName());
                }
            }
            else {
                noTv.setVisibility(View.VISIBLE);

            }
        }
    }

    @OnClick(R.id.add)
    public void addUser(){
        Intent intent = new Intent(this,AddUserActivity.class);
        startActivity(intent);
    }

    public void showPopupWindow(final List<UserEntity> userEntities,final int position){
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.popup_window_item,null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(view, width, height, true);

        TextView delTv = view.findViewById(R.id.delBtn);
        TextView updateTv = view.findViewById(R.id.updateBtn);

        delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRow(userEntities,position);
            }
        });

        updateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddUserActivity.class);
                intent.putExtra("edit",true);
                intent.putExtra("user",userEntities.get(position));
                startActivity(intent);
            }
        });

        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

    }

    public void deleteRow(final List<UserEntity> userEntities, final int position){


        @SuppressLint("StaticFieldLeak")
        class deleteAsyncTask extends AsyncTask<Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseSingInstance.getInstance(MainActivity.this)
                        .getOurDatabase()
                        .userDAO()
                        .deleteUser(userEntities.get(position));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                userEntities.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"deleted success",Toast.LENGTH_LONG).show();
            }
        }

        new deleteAsyncTask().execute();
    }
}
