package com.example.budnieswski.votaapp.app;

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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.budnieswski.votaapp.R;
import com.example.budnieswski.votaapp.util.AppSingleton;
import com.example.budnieswski.votaapp.util.VoteOperations;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmaActivity extends AppCompatActivity {

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

        if (!prefeitoJSON.isEmpty())
            showPrefeito(prefeitoJSON);

        if (!vereadorJSON.isEmpty())
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

                        VoteOperations conn = new VoteOperations(ConfirmaActivity.this);
                        conn.open();

                        conn.setConfirma(WelcomeActivity.userID);

                        Button confirmar = (Button) findViewById(R.id.confirmar);
                        confirmar.setVisibility(View.GONE);

                        Button alterar = (Button) findViewById(R.id.confirmar);
                        alterar.setVisibility(View.VISIBLE);

                        // ENVIAR DADOS
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmaActivity.this);
                        builder.setMessage("Votos enviados com sucesso.\nObrigado!")
                                .setTitle("Votos computados");


                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                        AlertDialog dialogComputado = builder.create();
                        dialogComputado.show();
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
    }

    private void showPrefeito(String prefeitoJson) {
        try {
            JSONObject data = new JSONObject(prefeitoJson);
            ImageLoader imageLoader = AppSingleton.getInstance(this).getImageLoader();

            TextView nome = (TextView) findViewById(R.id.prefeitoNome);
            TextView partido = (TextView) findViewById(R.id.prefeitoPartido);
            NetworkImageView foto = (NetworkImageView) findViewById(R.id.prefeitoFoto);

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

            TextView nome = (TextView) findViewById(R.id.vereadorNome);
            TextView partido = (TextView) findViewById(R.id.vereadorPartido);
            NetworkImageView foto = (NetworkImageView) findViewById(R.id.vereadorFoto);

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
