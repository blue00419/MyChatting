package kr.ac.s20140238kumoh.mychatting.list;

import android.view.View;

public interface ChatListClickListener {
    public void onItemClick(ChatListAdapter.MyViewHolder holder, View view, int position);
}