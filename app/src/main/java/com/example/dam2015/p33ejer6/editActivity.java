package com.example.dam2015.p33ejer6;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Hector on 15/12/2014.
 */
public class editActivity extends Activity {

    private EditText Titulo;
    private EditText Autor;
    private EditText Duracion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_canciones);
      // Intent i = getIntent();//Recogemos el intent
        Titulo = (EditText) findViewById(R.id.entradaTitulo);
        Autor = (EditText) findViewById(R.id.entradaAutor);
        Duracion = (EditText) findViewById(R.id.entradaDuracion);
        String titulo,autor,duracion;
        titulo=getIntent().getStringExtra("titulo");
        autor=getIntent().getStringExtra("autor");
        duracion=getIntent().getStringExtra("duracion");

        Titulo.setText(titulo);
        Autor.setText(autor);
        Duracion.setText(duracion);

    }
    public void onClick(View v){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("¿Está seguro que desea modificar la canción? ");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent data = new Intent();
                        Titulo = (EditText) findViewById(R.id.entradaTitulo);
                        Autor = (EditText) findViewById(R.id.entradaAutor);
                        Duracion = (EditText) findViewById(R.id.entradaDuracion);
                        Bundle b= new Bundle();
                        b.putString("titulo",Titulo.getText().toString());
                        b.putString("autor", Autor.getText().toString());
                        b.putString("duracion", Duracion.getText().toString());
                         String posi=getIntent().getStringExtra("p");
                        b.putString("pos",posi.toString());
                        data.putExtras(b);

                        setResult(RESULT_OK,data);

                        //---closes the activity---
                        finish();
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}
