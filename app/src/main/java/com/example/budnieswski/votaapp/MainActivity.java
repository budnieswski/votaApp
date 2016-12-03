package com.example.budnieswski.votaapp;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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
                                message.setText(response.getString("message"));
                                Intent it = new Intent(context, WelcomeActivity.class);
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
                        message.setText( "Houve um erro:\n" + error.getMessage() );

                        pDialog.hide();
                        Log.d("Error", error.getMessage());
                    }
                }
        );

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest,REQUEST_TAG);
    }
}
