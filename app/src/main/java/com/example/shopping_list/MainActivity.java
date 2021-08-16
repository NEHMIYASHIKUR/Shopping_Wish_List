package com.example.shopping_list;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final SharedPreferences preferences=getSharedPreferences("USER",MODE_PRIVATE);
       final String user_id=preferences.getString("id","");

       final ListView ShoppingListView=findViewById(R.id.ContainerListView);
        final ArrayList<String> ShoppingArrayList=new ArrayList<String>();
        final ArrayAdapter<String> ShoppingArrayAdopter=new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,ShoppingArrayList);
        final Button AddButton=findViewById(R.id.AddButton);
        final AsyncHttpClient client=new AsyncHttpClient();


                String url="http://dev.imagit.pl/wsg_zaliczenie/api/items/"+user_id;
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String JString=new String(responseBody);
                        try {JSONArray JArray=new JSONArray(JString);

                            for(int i=0;i<JArray.length();i++){
                                JSONObject JObject=JArray.getJSONObject(i);
                                String Name=JObject.getString("ITEM_NAME");
                                String Description=JObject.getString("ITEM_DESCRIPTION");
                               final String item_id=JObject.getString("ITEM_ID");

                                ShoppingArrayList.add(Name +","+Description+","+item_id);
                            }
                            ShoppingListView.setAdapter(ShoppingArrayAdopter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                ShoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("DELETE")
                                .setMessage("Do you want to delete this item ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String a=ShoppingListView.getItemAtPosition(position).toString();

                                        String[] splittedstring = a.split(",");
                                        String newitemname=splittedstring[0];
                                        String newitemdescription=splittedstring[1];
                                        String newitemid=splittedstring[splittedstring.length-1];
                                        String url="http://dev.imagit.pl/wsg_zaliczenie/api/item/delete/"+user_id+"/"+newitemid;
                                        client.get(url, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                Toast.makeText(MainActivity.this,"The item is deleted",Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(MainActivity.this,MainActivity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        ;
                        builder.show();




                    }
                });

                AddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,AddItemActivity.class);
                        startActivity(intent);
                    }
                });


    }
}
