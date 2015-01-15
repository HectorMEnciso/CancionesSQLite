package com.example.dam2015.p33ejer6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by dam2015 on 11/12/2014.
 */
public class addActivity extends Activity {

    private EditText Titulo;
    private EditText Autor;
    private EditText Duracion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_canciones);
    }
    public void onClick(View v){
        Intent data = new Intent();
        Titulo = (EditText) findViewById(R.id.entradaTitulo);
        Autor = (EditText) findViewById(R.id.entradaAutor);
        Duracion = (EditText) findViewById(R.id.entradaDuracion);
        Bundle b= new Bundle();
        b.putString("titulo",Titulo.getText().toString());
        b.putString("autor", Autor.getText().toString());
        b.putString("duracion", Duracion.getText().toString());
        data.putExtras(b);

        setResult(RESULT_OK,data);

        //---closes the activity---
        finish();
    }
}
