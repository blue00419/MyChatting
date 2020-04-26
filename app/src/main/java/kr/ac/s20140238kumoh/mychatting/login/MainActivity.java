package kr.ac.s20140238kumoh.mychatting.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.ac.s20140238kumoh.mychatting.chat.ChatActivity;
import kr.ac.s20140238kumoh.mychatting.R;
import kr.ac.s20140238kumoh.mychatting.data.UserData;
import kr.ac.s20140238kumoh.mychatting.list.ChatListActivity;

public class MainActivity extends AppCompatActivity {

    private Button Button_login;
    private Button Button_register;
    private EditText EditText_email;
    private EditText EditText_pw;

    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseDatabase database;
    boolean ok;

    private List<UserData> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button_login = findViewById(R.id.Button_login);
        Button_register = findViewById(R.id.Button_register);
        EditText_email = findViewById(R.id.EditText_email);
        EditText_pw = findViewById(R.id.EditText_pw);

        userList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EditText_email.getText().toString();
                final String pw = EditText_pw.getText().toString();

                signIn(email, pw);
            }
        });

        Button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText_email.setText("");
                EditText_pw.setText("");
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("List", (Serializable) userList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        userList.clear();
        myRef = database.getReference("user");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserData a = dataSnapshot.getValue(UserData.class);
                userList.add(a);
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

    private void signIn(String email, String pw){

        boolean ok=false;
        String name = null;
        for(int i=0; i<userList.size(); i++) {
            if(userList.get(i).getEmail().equals(email) && userList.get(i).getPassword().equals(pw)) {
                ok=true;
                name = userList.get(i).getDisplayName();
                break;
            }
        }

        if(ok){
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.putString("name", name);
            editor.putString("password", pw);
            editor.commit();

            Toast.makeText(getApplicationContext(),"로그인 완료", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("List", (Serializable) userList);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_LONG).show();
        }
    }
}
