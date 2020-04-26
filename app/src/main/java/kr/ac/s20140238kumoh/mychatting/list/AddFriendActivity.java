package kr.ac.s20140238kumoh.mychatting.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import kr.ac.s20140238kumoh.mychatting.R;
import kr.ac.s20140238kumoh.mychatting.data.ChatData;
import kr.ac.s20140238kumoh.mychatting.data.UserData;

public class AddFriendActivity extends AppCompatActivity {

    private RecyclerView mRecyclerVeiw;
    private RecyclerView.LayoutManager mLayoutManager;
    private AddFriendAdapter mAdapter;
    private String nick = "nick2";
    private List<String> userList;

    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_activity);

        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        nick = sharedPreferences.getString("name", "user");


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        Intent intent = getIntent();
        List<UserData> a = (List<UserData>) intent.getSerializableExtra("List");

        userList = new ArrayList<>();


        for(int i=0; i<a.size(); i++){
            if(!a.get(i).getDisplayName().equals(nick)){
                userList.add(a.get(i).getDisplayName());
            }
        }


        Log.d("CHATCHAT", String.valueOf(userList.size()));
        mRecyclerVeiw = findViewById(R.id.my_recycler_view);
        mRecyclerVeiw.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerVeiw.setLayoutManager(mLayoutManager);

        mAdapter = new AddFriendAdapter(userList,AddFriendActivity.this);

        mAdapter.setOnItemClicklistener(new AddFriendClickListener() {
            @Override
            public void onItemClick(AddFriendAdapter.MyViewHolder holder, View view, int position) {
                String user = mAdapter.getItem(position);
                myRef = database.getReference("message"); // 수정
                myRef.push().setValue(nick + "-" + user);
                mAdapter.clear();
                finish();
            }
        });
        mRecyclerVeiw.setAdapter(mAdapter);




    }
}
