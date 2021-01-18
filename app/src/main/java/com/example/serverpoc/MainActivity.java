package com.example.serverpoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String getUrl = "https://na-povodke.ru/get_users.php";
    private static final String postUrl = "https://na-povodke.ru/add_user.php";
    RequestQueue mRequestQueue; // очередь запросов
    List<String[]> users = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText)findViewById(R.id.editText1);

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    enterUsername(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);
    }

    private void makeRequests(String username) {
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("name", username);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        final TextView textView2 = (TextView)findViewById(R.id.textView2);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, postUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textView2.setText(String.format("%s added", username));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(req);


        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    users.clear();
                    for(int i = 0; i < response.length(); i++){
                        users.add(new String[]{String.valueOf(response.getJSONObject(i).getInt("id")), response.getJSONObject(i).getString("name")});
                    }

                    final TextView textView = (TextView)findViewById(R.id.textView);
                    StringBuilder sb = new StringBuilder();
                    for(String[] i: users)
                        sb.append(i[0] + " " + i[1] + "\n");
                    textView.setText(sb.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // в случае возникновеня ошибки
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request); // добавляем запрос в очередь
    }

    public void userButtonClick(View view){
        final EditText editText = (EditText)findViewById(R.id.editText1);
        enterUsername(editText.getText().toString());
    }

    private void enterUsername(String username){
        if(username != null && username.replaceAll("\\s+","").length() != 0){
            makeRequests(username);
        }
    }
}