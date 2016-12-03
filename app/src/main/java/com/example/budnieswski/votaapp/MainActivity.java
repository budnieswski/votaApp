package com.example.budnieswski.votaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String hardTitulo = "123";
    private String hardSenha = "123";

    private EditText titulo;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titulo = (EditText) findViewById(R.id.titulo);
        senha = (EditText) findViewById(R.id.senha);
    }

    public void autenticar(View view) {
        if(titulo.getText().toString().equals(hardTitulo) && senha.getText().toString().equals(hardSenha)) {
            Intent it = new Intent(this, MasterActivity.class);
            startActivity(it);
        } else {
            Toast.makeText(this, "Titulo ou senha incorretos", Toast.LENGTH_LONG).show();
        }
    }
}
