package com.example.budnieswski.votaapp.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.util.AppSingleton;
import com.example.budnieswski.votaapp.util.VoteOperations;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmaActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private String urlConfirma = "http://10.0.2.2:8080/sentinel/v2/vota/confirm?prefeito=%s&vereador=%s&id=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma);

        setTitle("Confirmar Votos");
        Button confirmar = (Button) findViewById(R.id.confirmar);
        Button alterar = (Button) findViewById(R.id.alterar);

        VoteOperations conn = new VoteOperations(this);
        conn.open();

        String prefeitoJSON = conn.getPrefeito(WelcomeActivity.userID);
        String vereadorJSON = conn.getVereador(WelcomeActivity.userID);

        if (prefeitoJSON != null && !prefeitoJSON.isEmpty())
            showPrefeito(prefeitoJSON);

        if (vereadorJSON != null && !vereadorJSON.isEmpty())
            showVereador(vereadorJSON);

        // Evitando votar sem ter os 2 candidatos
        if (!prefeitoJSON.isEmpty() && !vereadorJSON.isEmpty())
            confirmar.setEnabled(true);

        Log.d("DADOS", "Confirma" + conn.getConfirma(WelcomeActivity.userID) );
        // Exibindo o alterar voto somente se já estiver confirmado
        if (conn.getConfirma(WelcomeActivity.userID) != null && conn.getConfirma(WelcomeActivity.userID).equals("S")) {
            confirmar.setVisibility(View.GONE);
            alterar.setVisibility(View.VISIBLE);
        }

        confirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("EVENT", "On confirma click");

                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmaActivity.this);
                builder.setMessage("Deseja realmente confirmar seus votos?")
                        .setTitle("Computar voto");

                builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        String prefeitoID = ((TextView) findViewById(R.id.vereadorID)).getText().toString();
                        String vereadorID = ((TextView) findViewById(R.id.vereadorID)).getText().toString();

                        // ENVIAR DADOS
                        volleyComputaVoto( String.format(urlConfirma, prefeitoID, vereadorID, WelcomeActivity.userID) );
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        alterar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmaActivity.this);
                builder.setMessage("Deseja realmente MODIFICAR seus votos?")
                        .setTitle("Modificar votos");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                        // ENVIAR DADOS
                        volleyModificaVoto( String.format(urlConfirma, "0", "0", WelcomeActivity.userID) );
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void volleyComputaVoto(String url) {
        String REQUEST_TAG = "VOTA_COMPUTE";
        pDialog = new ProgressDialog(ConfirmaActivity.this);
        pDialog.setMessage("Enviando dados ...");
        pDialog.show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmaActivity.this);
                        builder.setMessage("Votos enviados com sucesso.\nObrigado!")
                                .setTitle("Votos computados");

                        VoteOperations conn = new VoteOperations(ConfirmaActivity.this);
                        conn.open();

                        conn.setConfirma(WelcomeActivity.userID);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                startActivity(ConfirmaActivity.this.getIntent());
                            }
                        });

                        AlertDialog dialogComputado = builder.create();
                        dialogComputado.show();

                        pDialog.hide();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                    }
                }
        );

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest,REQUEST_TAG);
    }

    public void volleyModificaVoto(String url) {
        String REQUEST_TAG = "VOTA_MODIFY";
        pDialog = new ProgressDialog(ConfirmaActivity.this);
        pDialog.setMessage("Fazendo solicitação ...");
        pDialog.show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmaActivity.this);
                        builder.setMessage("Modificação habilitada.\nAgora você já pode votar novamente!")
                                .setTitle("Votos resetados");

                        VoteOperations conn = new VoteOperations(ConfirmaActivity.this);
                        conn.open();

                        conn.resetVoto(WelcomeActivity.userID);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                startActivity(ConfirmaActivity.this.getIntent());
                            }
                        });

                        AlertDialog dialogComputado = builder.create();
                        dialogComputado.show();

                        pDialog.hide();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                    }
                }
        );

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest,REQUEST_TAG);
    }

    private void showPrefeito(String prefeitoJson) {
        try {
            JSONObject data = new JSONObject(prefeitoJson);
            ImageLoader imageLoader = AppSingleton.getInstance(this).getImageLoader();

            TextView id = (TextView) findViewById(R.id.prefeitoID);
            TextView nome = (TextView) findViewById(R.id.prefeitoNome);
            TextView partido = (TextView) findViewById(R.id.prefeitoPartido);
            NetworkImageView foto = (NetworkImageView) findViewById(R.id.prefeitoFoto);

            id.setText(data.getString("id"));
            nome.setText(data.getString("nome"));
            partido.setText(data.getString("partido"));
            foto.setImageUrl(data.getString("foto"), imageLoader);

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.wrapPrefeito);
            layout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showVereador(String vereadorJson) {
        try {
            JSONObject data = new JSONObject(vereadorJson);
            ImageLoader imageLoader = AppSingleton.getInstance(this).getImageLoader();

            TextView id = (TextView) findViewById(R.id.vereadorID);
            TextView nome = (TextView) findViewById(R.id.vereadorNome);
            TextView partido = (TextView) findViewById(R.id.vereadorPartido);
            NetworkImageView foto = (NetworkImageView) findViewById(R.id.vereadorFoto);

            id.setText(data.getString("id"));
            nome.setText(data.getString("nome"));
            partido.setText(data.getString("partido"));
            foto.setImageUrl(data.getString("foto"), imageLoader);

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.wrapVereador);
            layout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
