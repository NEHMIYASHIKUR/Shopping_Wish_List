package com.example.shopping_list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final SharedPreferences preferences=getSharedPreferences("USER",MODE_PRIVATE);
        final String User_id=preferences.getString("id","");

        final EditText NameInput=findViewById(R.id.Nameinput);
        final EditText DescriptionInput=findViewById(R.id.DescriptionInput);
        final Button AddButton=findViewById(R.id.AddButton);

        final AsyncHttpClient client=new AsyncHttpClient();

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name=NameInput.getText().toString();
                final String Description=DescriptionInput.getText().toString();

                final RequestParams params=new RequestParams();

                String url="http://dev.imagit.pl/wsg_zaliczenie/api/item/add";

                params.put("user",User_id);
                params.put("name",Name);
                params.put("desc",Description);

                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if(Name.isEmpty() || Description.isEmpty()){
                            AlertDialog.Builder builder=new AlertDialog.Builder(AddItemActivity.this);
                            builder.setTitle("ERROR")
                                    .setMessage("Please fill in all the required fields")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                        }
                        else{
                            Intent intent=new Intent(AddItemActivity.this,MainActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });

    }
}
