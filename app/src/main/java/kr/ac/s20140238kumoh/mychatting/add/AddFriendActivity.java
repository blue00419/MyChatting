package kr.ac.s20140238kumoh.mychatting.add;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import kr.ac.s20140238kumoh.mychatting.R;
import kr.ac.s20140238kumoh.mychatting.data.ChatData;
import kr.ac.s20140238kumoh.mychatting.data.UserData;
import kr.ac.s20140238kumoh.mychatting.list.ChatListAdapter;

public class AddFriendActivity extends AppCompatActivity {

    private RecyclerView mRecyclerVeiw;
    private RecyclerView.LayoutManager mLayoutManager;
    private AddFriendAdapter mAdapter;
    private String nick = "nick2";
    private List<String> userList;
    private List<List<String>> room;

    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_activity);

        setTitle("친구 찾기");

        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        nick = sharedPreferences.getString("name", "user");


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        Intent intent = getIntent();
        List<UserData> a = (List<UserData>) intent.getSerializableExtra("List");

        userList = new ArrayList<>();
        room = new ArrayList<>();

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

                int count;
                boolean ok = true;
                Log.d("CHATCHAT", String.valueOf(room.size()));
                for(int i=0; i<room.size(); i++){
                    count = 0;
                    if(room.get(i).size() == 2){
                        for(int j=0; j<2; j++){
                            if(room.get(i).get(j).equals(nick))
                                count++;
                            if(room.get(i).get(j).equals(user))
                                count++;
                        }
                        if(count==2){
                            ok = false;
                            break;
                        }
                    }
                }

                if(ok){
                    Toast.makeText(getApplicationContext(),"대화창 추가 성공", Toast.LENGTH_LONG).show();
                    myRef = database.getReference("message"); // 수정
                    String key = myRef.push().getKey();


                    ChatData chat = new ChatData();
                    chat.setNickname(nick);
                    chat.setMsg(nick+"님이 " + user + "님을 초대하였습니다.");

                    myRef.child(key).child(nick + "-" + user).push().setValue(chat);
                    mAdapter.clear();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"대화창 추가 실패", Toast.LENGTH_LONG).show();
                    mAdapter.clear();
                    finish();
                }
            }
        });
        mRecyclerVeiw.setAdapter(mAdapter);


        myRef = database.getReference("message");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("CHATCHAT", dataSnapshot.getValue().toString());
                StringTokenizer tokenizer1 = new StringTokenizer(dataSnapshot.getValue().toString(), "=");

                String str1 = null;
                while(tokenizer1.hasMoreTokens()){
                    str1 = tokenizer1.nextToken();
                    break;
                }
                String str2 = str1.substring(1);

                StringTokenizer tokenizer = new StringTokenizer(str2, "-");

                List<String> a = new ArrayList<>();
                while(tokenizer.hasMoreTokens()){
                    a.add(tokenizer.nextToken());
                }

                room.add(a);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
