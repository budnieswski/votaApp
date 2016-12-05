package com.example.budnieswski.votaapp.app;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.adapter.CandidatosAdapter;
import com.example.budnieswski.votaapp.model.Candidato;
import com.example.budnieswski.votaapp.util.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private List<Candidato> candidatosList;
    private String urlPrefeito = "https://dl.dropboxusercontent.com/u/40990541/prefeito.json";
    private String urlVereador = "https://dl.dropboxusercontent.com/u/40990541/vereador.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        candidatosList = new ArrayList<Candidato>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView topMessage = (TextView) findViewById(R.id.topMessage);
        topMessage.setVisibility(View.INVISIBLE);

        switch (item.getItemId()) {
            case R.id.menuPrefeito:
                volleyDataRequest(urlPrefeito, "prefeito");
                setTitle("Candidatos a Prefeito");
                return true;
            case R.id.menuVereador:
                volleyDataRequest(urlVereador, "vereador");
                setTitle("Candidatos a Vereador");
                return true;
            case R.id.menuConfirmar:
                Toast.makeText(this, "Confirmar", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuSair:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void volleyDataRequest(String url, final String type) {
        String REQUEST_TAG = "VOTA_LOAD_"+type;
        pDialog = new ProgressDialog(WelcomeActivity.this);
        pDialog.setMessage("Carregando dados ...");
        pDialog.show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            candidatosList = new ArrayList<Candidato>();
                            ListView lista = (ListView) findViewById(R.id.lista);

                            JSONArray candidatos = response.getJSONArray(type);
                            for (int i=0; i<candidatos.length(); i++) {
                                JSONObject data = candidatos.getJSONObject(i);

                                Candidato candidato = new Candidato();
                                candidato.setNome(data.getString("nome"));
                                candidato.setPartido(data.getString("partido"));
                                candidato.setFotoURL(data.getString("foto"));

                                candidatosList.add(candidato);
                            }

                            CandidatosAdapter adapter = new CandidatosAdapter(WelcomeActivity.this, candidatosList);
                            lista.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error", e.getMessage());
                        }

                        pDialog.hide();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        TextView topMessage = (TextView) findViewById(R.id.topMessage);
                        NetworkResponse networkResponse = error.networkResponse;

                        topMessage.setVisibility(View.VISIBLE);

                        if (networkResponse != null && networkResponse.statusCode == 404) {
                            topMessage.setText( "Server not found (404)" );
                        } else {
                            topMessage.setText( "Couldn't connect to server, try again later." );
                        }
                    }
                }
        );

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest,REQUEST_TAG);
    }
}
