package kr.ac.s20140238kumoh.mychatting;

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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int chat = 1000;

    private Button Button_login;
    private Button Button_register;
    private EditText EditText_email;
    private EditText EditText_id;
    private EditText EditText_pw;

    DatabaseReference myRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button_login = findViewById(R.id.Button_login);
        Button_register = findViewById(R.id.Button_register);
        EditText_email = findViewById(R.id.EditText_email);
        EditText_id = findViewById(R.id.EditText_id);
        EditText_pw = findViewById(R.id.EditText_pw);

        auth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EditText_email.getText().toString();
                final String id = EditText_id.getText().toString();
                final String pw = EditText_pw.getText().toString();

                signIn(email, pw);

                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", id);
                editor.putString("email", email);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        Button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EditText_email.getText().toString();
                final String id = EditText_id.getText().toString();
                final String pw = EditText_pw.getText().toString();
                if(email != null && id != null && pw != null){
                    if(pw.length() >= 6){
                        signUp(email, id, pw);
                    }
                    else{
                        EditText_pw.setText("");
                        Toast.makeText(getApplicationContext(), "pw가 짧습니다.", Toast.LENGTH_LONG).show();
                    }
                }
                else if(email != null){
                    Toast.makeText(getApplicationContext(), "email를 입력하세요.", Toast.LENGTH_LONG).show();
                }
                else if(id != null){
                    Toast.makeText(getApplicationContext(), "id를 입력하세요.", Toast.LENGTH_LONG).show();
                }
                else if(pw != null){
                    Toast.makeText(getApplicationContext(), "pw를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signIn(String email, String pw){
        auth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                });
    }

    private void signUp(final String email, final String id, final String pw){
        auth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener
                        (this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

//                                    final String uid = task.getResult().getUser().getUid();
//                                    UserModel userModel = new UserModel();
//
//                                    userModel.displayName = id;
//                                    userModel.uid = uid;
//                                    userModel.pw = pw;
//                                    userModel.email = email;
//
//                                    myRef.setValue(userModel);

                                    Toast.makeText(getApplicationContext(),"회원가입 완료", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    EditText_email.setText("");
                                    EditText_id.setText("");
                                    EditText_pw.setText("");
                                }
                            }
                        });
    }

    private void signOut(){
        auth.signOut();
        FirebaseUser user = null;
        auth.updateCurrentUser(user);
    }
}
