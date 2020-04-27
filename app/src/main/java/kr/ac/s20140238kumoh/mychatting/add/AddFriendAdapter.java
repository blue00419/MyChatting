package kr.ac.s20140238kumoh.mychatting.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.s20140238kumoh.mychatting.R;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder>
implements AddFriendClickListener {

    private List<String> userList;
    AddFriendClickListener listener;

    public void setOnItemClicklistener(AddFriendClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(MyViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView TextView_name;
        public View rootView;

        public MyViewHolder(final View v, final AddFriendClickListener listener) {
            super(v);
            TextView_name = v.findViewById(R.id.TextView_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(listener != null){
                            listener.onItemClick(MyViewHolder.this, view, position);
                        }
                    }
                }
            });
            rootView = v;
        }

        public void setItem(String user){
            TextView_name.setText(user);
        }

    }

    public String getItem(int position){
        return userList.get(position);
    }

    public void clear(){
        userList.clear();
    }

    public AddFriendAdapter(List<String> myDataset, Context context){
        userList = myDataset;
    }

    @NonNull
    @Override
    public AddFriendAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_friend, parent, false);
        MyViewHolder vh = new MyViewHolder(v, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String user = userList.get(position);

        holder.setItem(user);
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }
}
