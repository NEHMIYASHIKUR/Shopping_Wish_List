package com.example.shopping_list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       // Intent intent=new Intent();
     //  intent.getExtras();

        final EditText Username=findViewById(R.id.UsernameInput);
        final EditText Password=findViewById(R.id.PasswordInput);
        final EditText Email=findViewById(R.id.EmailInput);
        final Button Registerbutton=findViewById(R.id.Registerbutton);
        final AsyncHttpClient client=new AsyncHttpClient();

        Registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=Username.getText().toString();
                String password=Password.getText().toString();
                String email=Email.getText().toString();
                if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("ERROR")
                            .setMessage("Please fill all the fields")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                }
                else{
                String url="http://dev.imagit.pl/wsg_zaliczenie/api/register";
                    RequestParams params=new RequestParams();
                    params.put("login",username);
                    params.put("pass",password);
                    params.put("email",email);
                    client.post(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response=new String(responseBody);
                        if(response.equals("OK")){
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,
                                    "Please try again",Toast.LENGTH_LONG).show();
                        }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }
            }
        });
    }
}
