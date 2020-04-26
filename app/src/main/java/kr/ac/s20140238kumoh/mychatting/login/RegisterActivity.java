package kr.ac.s20140238kumoh.mychatting.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import kr.ac.s20140238kumoh.mychatting.R;
import kr.ac.s20140238kumoh.mychatting.data.UserData;

public class RegisterActivity extends AppCompatActivity {

    private Button Button_back;
    private Button Button_register;
    private EditText EditText_email;
    private EditText EditText_id;
    private EditText EditText_pw;

    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseDatabase database;
    boolean ok;

    private List<UserData> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        Intent intent = getIntent();
        List<UserData> a = (List<UserData>) intent.getSerializableExtra("List");

        userList = new ArrayList<>();

        for(int i=0; i<a.size(); i++){
            userList.add(a.get(i));
        }

        Button_back = findViewById(R.id.Button_back);
        Button_register = findViewById(R.id.Button_register);
        EditText_email = findViewById(R.id.EditText_email);
        EditText_id = findViewById(R.id.EditText_id);
        EditText_pw = findViewById(R.id.EditText_pw);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EditText_email.getText().toString();
                final String id = EditText_id.getText().toString();
                final String pw = EditText_pw.getText().toString();
                if(!email.equals("") && !id.equals("") && !pw.equals("")){
                    if(pw.length() >= 6){
                        EditText_email.setText("");
                        EditText_id.setText("");
                        EditText_pw.setText("");
                        signUp(email, id, pw);
                    }
                    else{
                        EditText_pw.setText("");
                        Toast.makeText(getApplicationContext(), "pw가 짧습니다.", Toast.LENGTH_LONG).show();
                    }
                }
                else if(email.equals("")){
                    Toast.makeText(getApplicationContext(), "email를 입력하세요.", Toast.LENGTH_LONG).show();
                }
                else if(id.equals("")){
                    Toast.makeText(getApplicationContext(), "id를 입력하세요.", Toast.LENGTH_LONG).show();
                }
                else if(pw.equals("")){
                    Toast.makeText(getApplicationContext(), "pw를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signUp(String email, String id, String pw){

        boolean ok=true;
        for(int i=0; i<userList.size(); i++) {
            if(userList.get(i).getEmail().equals(email)) {
                ok=false;
                break;
            }
        }

        if(ok){

            myRef = database.getReference("user");

            UserData model = new UserData();
            model.setDisplayName(id);
            model.setEmail(email);
            model.setPassword(pw);
            myRef.push().setValue(model);

            Toast.makeText(getApplicationContext(),"회원가입 완료", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"회원가입 실패", Toast.LENGTH_LONG).show();
        }
    }
}
