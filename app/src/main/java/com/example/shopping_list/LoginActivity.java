package com.example.shopping_list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameinput=findViewById(R.id.Usernametext);
        final EditText passwordinput=findViewById(R.id.Passwordtext);
        final TextView gotoregister=findViewById(R.id.gotoRegister);
        final Button loginbutton=findViewById(R.id.Loginbutton);
        final AsyncHttpClient client=new AsyncHttpClient();
        final SharedPreferences preferences=getSharedPreferences("USER",MODE_PRIVATE);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameinput.getText().toString();
                String password=passwordinput.getText().toString();
                if(username.isEmpty() || password.isEmpty()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("ERROR")
                            .setMessage("Please fill in the the input fields")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();

                }
                else{
                    String url="http://dev.imagit.pl/wsg_zaliczenie/api/login/"+username+"/"+password;
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                             String response=new String(responseBody);
                            if(android.text.TextUtils.isDigitsOnly(response)){
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("id",response);
                                editor.commit();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Incorrect username or password",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                        }
                    });
  }
            }
        });

        gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("first","a");
                startActivity(intent);
            }
        });

    }
}
