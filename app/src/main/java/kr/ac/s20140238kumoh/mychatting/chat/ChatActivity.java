package kr.ac.s20140238kumoh.mychatting.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import kr.ac.s20140238kumoh.mychatting.data.ChatData;
import kr.ac.s20140238kumoh.mychatting.R;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerVeiw;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private String nick = "nick2";
    private String roomname;
    private String roomdb;
    private String roomkey;

    private EditText EditText_chat;
    private Button Button_send;

    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        Intent intent = getIntent();
        roomname = intent.getExtras().getString("user");
        roomdb = intent.getExtras().getString("room");
        roomkey = intent.getExtras().getString("roomkey");

        setTitle(roomname + "님과의 대화");


        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        nick = sharedPreferences.getString("name", "user");

        auth = FirebaseAuth.getInstance();

        mRecyclerVeiw = findViewById(R.id.my_recycler_view);
        mRecyclerVeiw.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerVeiw.setLayoutManager(mLayoutManager);


        chatList = new ArrayList<>();

        mAdapter = new ChatAdapter(chatList, ChatActivity.this, nick);
        mRecyclerVeiw.setAdapter(mAdapter);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message/" + roomkey + "/" + roomdb);



        Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString();
                EditText_chat.setText("");
                if(msg != null){
                    ChatData chat = new ChatData();
                    chat.setNickname(nick);
                    chat.setMsg(msg);
                    //myRef.push().setValue(chat);
                    //myRef = database.getReference(nick);
                    myRef.push().setValue(chat);
                }
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("CHATCHAT", dataSnapshot.getValue().toString());
                ChatData chat = dataSnapshot.getValue(ChatData.class);
                Log.d("CHATCHAT", chat.getMsg());
                ((ChatAdapter) mAdapter).addChat(chat);
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
