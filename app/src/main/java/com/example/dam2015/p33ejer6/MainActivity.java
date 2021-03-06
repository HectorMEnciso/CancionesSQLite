package com.example.dam2015.p33ejer6;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private TextView lblTitulo;
    private TextView lblAutor;
    private TextView lblDuracion;
    private ListView LstOpciones;

    adaptadorCanciones adaptador;
    private ArrayList<Cancion> datos = new ArrayList<Cancion>();
    int posi;
    SQLiteDatabase db=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Abrimos la base de datos 'DBCanciones' en modo escritura
        CancionesSQLiteHelper candb= new CancionesSQLiteHelper(this,"DBCanciones",null,1);

       db = candb.getWritableDatabase();

        adaptador = new adaptadorCanciones(this, datos);
        LstOpciones = (ListView) findViewById(R.id.LstOpciones);
        LstOpciones.setAdapter(adaptador);
        addOnClickView();
        registerForContextMenu(LstOpciones);
    }

    //Generacion del menu a partir del menu_main.xml
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void addOnClickView() {
        LstOpciones.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent data = new Intent(MainActivity.this, editActivity.class);
                data.putExtra("titulo", adaptador.getItem(position).getTitulo().toString());
                data.putExtra("autor", adaptador.getItem(position).getAutor().toString());
                data.putExtra("duracion", adaptador.getItem(position).getDuracion().toString());
                data.putExtra("p",adaptador.getItem(position).toString());
                startActivityForResult(data, 2);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        if (v.getId() == R.id.LstOpciones) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            menu.setHeaderTitle(LstOpciones.getAdapter().getItem(info.position).toString());

            inflater.inflate(R.menu.opciones_elementos, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.EliminarSeleccionada:
                posi = info.position;
                Toast.makeText(getBaseContext(), datos.get(posi).getTitulo(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), datos.get(posi).getAutor(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), datos.get(posi).getDuracion(), Toast.LENGTH_SHORT).show();
                adaptador.delCancion(datos, posi);
                adaptador.notifyDataSetChanged();//Refresca adaptador.
                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AnadirCancionOverflow:
                Intent i = new Intent(this, addActivity.class);
                startActivityForResult(i, 1);
                return true;
            case R.id.AnadirCancion:
                Intent in = new Intent(this, addActivity.class);
                startActivityForResult(in, 1);
                return true;
            case R.id.BorrarTodas:
                adaptador.deleteAll(datos);
                adaptador.notifyDataSetChanged();//Refresca adaptador.
                return true;
            case R.id.GuardarFichero:
                saveCanciones(datos);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                adaptador.addCancion(R.drawable.rihanna, bundle.getString("titulo"), bundle.getString("autor"), bundle.getString("duracion"),datos);
                Toast.makeText(getBaseContext(), "Canción añadida", Toast.LENGTH_SHORT).show();
                adaptador.notifyDataSetChanged();//Refresca adaptador.
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                //AdapterView
                adaptador.editCancion(new Cancion(R.drawable.rihanna, bundle.getString("titulo"), bundle.getString("autor"), bundle.getString("duracion")), Integer.parseInt(bundle.getString("pos")),datos);
                adaptador.notifyDataSetChanged();//Refresca adaptador.
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (datos.isEmpty()) {//Si el arraylist esta vacio
            loadCanciones();
        }
    }

    public void onPause() {
        super.onPause();
        Log.e("Entro onDestroy","");
        saveCanciones(datos);
    }
    public void saveCanciones(ArrayList<Cancion> d) {
        db.delete("Canciones",null,null);
        String titulo=null,autor=null,duracion=null;
        ContentValues nuevoRegistro=new ContentValues();
        int idfoto=0;
        for (int x = 0; x < d.size(); x++) {
            idfoto = d.get(x).getIdentificador();
            titulo=d.get(x).getTitulo();
            autor= d.get(x).getAutor();
            duracion= d.get(x).getDuracion();

            nuevoRegistro.put("idfoto", idfoto);
            nuevoRegistro.put("titulo", titulo);
            nuevoRegistro.put("autor", autor);
            nuevoRegistro.put("duracion", duracion);
            db.insert("Canciones", null, nuevoRegistro);
            Log.e("prueeebaaaaaaa",nuevoRegistro.toString());
            nuevoRegistro.clear();

        }
    }
    public void loadCanciones() {
        int idfoto = 0;
        String titulo = null;
        String autor = null;
        String duracion = null;
        Cursor c = db.rawQuery("SELECT idfoto,titulo,autor,duracion FROM Canciones", null);
        if (c.moveToFirst()) {
            do {
                idfoto = c.getInt(0);
                titulo = c.getString(1);
                autor=c.getString(2);
                duracion=c.getString(3);
                adaptador.addCancion(idfoto, titulo, autor, duracion,datos);
            } while(c.moveToNext());
            adaptador.notifyDataSetChanged();//Refresca adaptador.
        }
    }
}