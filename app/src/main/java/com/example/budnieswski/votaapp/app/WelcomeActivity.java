package com.example.budnieswski.votaapp.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.budnieswski.votaapp.util.VoteOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    public static int userID;
    private ProgressDialog pDialog;
    private static List<Candidato> candidatosList;
    private String urlPrefeito = "https://dl.dropboxusercontent.com/u/40990541/prefeito.json";
    private String urlVereador = "https://dl.dropboxusercontent.com/u/40990541/vereador.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        candidatosList = new ArrayList<Candidato>();

        Intent it = getIntent();
        if (it != null) {
            Bundle params = it.getExtras();
            if (params != null) {
                userID = params.getInt("userID");
            }
        }

        if (((ListView) findViewById(R.id.lista)).getAdapter().getCount() < 1) {
            ((TextView) findViewById(R.id.topMessage)).setText("Escolha sua opção no menu");
        }

        ((ListView) findViewById(R.id.lista)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String json = ((TextView) view.findViewById(R.id.json)).getText().toString();
                String type = ((TextView) view.findViewById(R.id.type)).getText().toString();

                Intent it = new Intent(WelcomeActivity.this, DetailActivity.class);

                Bundle b = new Bundle();
                b.putString("json", json);
                b.putString("type", type);

                it.putExtras(b);

                Log.d("Settado", type);

                startActivity(it);
            }
        });

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
                Intent it = new Intent(WelcomeActivity.this, ConfirmaActivity.class);
                startActivity(it);
                return true;
            case R.id.menuSair:
                this.resetAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        this.resetAll();

        super.onDestroy();
    }

    public void resetAll() {
        VoteOperations conn = new VoteOperations(this);
        conn.open();
        String confirma = conn.getConfirma(WelcomeActivity.userID);

        if (!confirma.equals("S"))
            conn.resetVoto(WelcomeActivity.userID);

        finish();
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
                                candidato.setId(data.getInt("id"));
                                candidato.setNome(data.getString("nome"));
                                candidato.setPartido(data.getString("partido"));
                                candidato.setFotoURL(data.getString("foto"));
                                candidato.setType(type);
                                candidato.setJson(data.toString());

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
