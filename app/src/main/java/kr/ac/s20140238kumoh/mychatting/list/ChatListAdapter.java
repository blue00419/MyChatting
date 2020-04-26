package kr.ac.s20140238kumoh.mychatting.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.s20140238kumoh.mychatting.R;
import kr.ac.s20140238kumoh.mychatting.data.UserData;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private List<String> userList;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView TextView_name;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            TextView_name = v.findViewById(R.id.TextView_name);
            rootView = v;
        }

        public void setItem(String user){
            TextView_name.setText(user);
        }
    }

    public ChatListAdapter(List<String> myDataset, Context context){
        userList = myDataset;
    }

    @NonNull
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_friend, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
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

    public void addUser(String user) {
        userList.add(user);
        notifyItemInserted(userList.size() - 1); // 갱신
    }
}
