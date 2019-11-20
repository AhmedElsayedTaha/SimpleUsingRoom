package apit.net.sa.simpleusingroom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<UserEntity> userEntities ;
    private Context context;
    OnLongClickListner onLongClickListner;

    interface OnLongClickListner{
        void onClick(int position);
    }

    public UserAdapter(List<UserEntity> userEntities, Context context,OnLongClickListner onLongClickListner) {
        this.userEntities = userEntities;
        this.context = context;
        this.onLongClickListner=onLongClickListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_layout,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        UserEntity userEntity = userEntities.get(i);
        viewHolder.userName.setText(userEntity.getUserName());
        viewHolder.userAge.setText(userEntity.getAge());
    }

    @Override
    public int getItemCount() {
        return userEntities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName,userAge;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.name);
            userAge = itemView.findViewById(R.id.age);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onLongClickListner.onClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
