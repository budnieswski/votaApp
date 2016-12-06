package com.example.budnieswski.votaapp.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.model.Vote;
import com.example.budnieswski.votaapp.util.AppSingleton;
import com.example.budnieswski.votaapp.util.VoteOperations;
import com.example.budnieswski.votaapp.util.md5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by budnieswski on 03/12/16.
 */
public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private String urlLogin = "http://10.0.2.2:8080/sentinel/v2/vota/login?titulo=%s&senha=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.autenticar)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView message = (TextView) findViewById(R.id.message);
                String titulo = ((EditText) findViewById(R.id.titulo)).getText().toString();
                String senha = ((EditText) findViewById(R.id.senha)).getText().toString();

                message.setText("");

                if(!titulo.equals("") && !senha.equals("")) {
                    volleyLoginRequest(MainActivity.this, titulo, senha);
                } else {
                    message.setText("Titulo and Senha are required.");
                }
            }
        });
    }

    public void volleyLoginRequest(final Context context, String titulo, String senha) {
        String REQUEST_TAG = "VOTA_LOGIN";
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Verificando dados ...");
        pDialog.show();

        Log.d("Data", "senha: " + md5.generate(senha));
        Log.d("Data", "titulo: " + titulo);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                String.format(urlLogin, titulo, md5.generate(senha)), null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            TextView message = (TextView) findViewById(R.id.message);

                            if (response.getBoolean("error")) {
                                message.setText(response.getString("message"));
                            } else {
                                // Logged!
                                VoteOperations conn = new VoteOperations(MainActivity.this);
                                conn.open();

                                Vote vote = conn.getUser(response.getJSONArray("user").getInt(0));

                                // Usuario ainda nao cadastrado
                                if (vote == null)
                                    conn.setUser(response.getJSONArray("user").getInt(0));

                                Intent it = new Intent(context, WelcomeActivity.class);
                                it.putExtra("userID", response.getJSONArray("user").getInt(0));
                                startActivity(it);

                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Response", response.toString());
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TextView message = (TextView) findViewById(R.id.message);
                        NetworkResponse networkResponse = error.networkResponse;

                        if (networkResponse != null && networkResponse.statusCode==404) {
                            message.setText( "Server not found (404)" );
                        } else {
                            message.setText("Couldn't connect to server, try again later.");
                        }

                        pDialog.hide();
                    }
                }
        );

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest,REQUEST_TAG);
    }
}
