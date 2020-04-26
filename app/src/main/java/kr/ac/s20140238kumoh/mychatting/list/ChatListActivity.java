package kr.ac.s20140238kumoh.mychatting.list;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import kr.ac.s20140238kumoh.mychatting.data.ChatData;
import kr.ac.s20140238kumoh.mychatting.R;
import kr.ac.s20140238kumoh.mychatting.chat.ChatAdapter;
import kr.ac.s20140238kumoh.mychatting.data.UserData;
import kr.ac.s20140238kumoh.mychatting.login.RegisterActivity;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerVeiw;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<UserData> userList;
    private List<String> myFriendList;
    private String nick = "nick2";

    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_activity);

        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        nick = sharedPreferences.getString("name", "user");


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        Intent intent = getIntent();
        List<UserData> a = (List<UserData>) intent.getSerializableExtra("List");

        userList = new ArrayList<>();

        for(int i=0; i<a.size(); i++){
            userList.add(a.get(i));
            Log.d("CHATCHAT", userList.get(i).getDisplayName());
        }

        mRecyclerVeiw = findViewById(R.id.my_recycler_view);
        mRecyclerVeiw.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerVeiw.setLayoutManager(mLayoutManager);

        myFriendList = new ArrayList<>();
        mAdapter = new ChatListAdapter(myFriendList, ChatListActivity.this);
        mRecyclerVeiw.setAdapter(mAdapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("List", (Serializable) userList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        myRef = database.getReference("message");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("CHATCHAT", dataSnapshot.getValue().toString());
                StringTokenizer tokenizer = new StringTokenizer(dataSnapshot.getValue().toString(), "-");

                String str[] = new String[10];
                int index=0, myindex=0;
                boolean ok = false;
                while(tokenizer.hasMoreTokens()){
                    str[index] = tokenizer.nextToken();
                    if(str[index].equals(nick)){
                        myindex = index;
                        ok = true;
                    }
                    index++;
                }
                if(ok){
                    String result = "";

                    for(int i=0; i<index; i++){
                        if(i!=myindex){
                            result += str[i];
                            break;
                        }
                    }

                    for(int i=0; i<index; i++){
                        if(i!=myindex){
                            if(!str[i].equals(result)){
                                result += ", " + str[i];
                            }
                        }
                    }

                    ((ChatListAdapter) mAdapter).addUser(result);
                }
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
