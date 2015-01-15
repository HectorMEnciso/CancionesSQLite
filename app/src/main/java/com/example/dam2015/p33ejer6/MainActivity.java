package com.example.dam2015.p33ejer6;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                adaptador.addCancion(R.drawable.rihanna, bundle.getString("titulo"), bundle.getString("autor"), bundle.getString("duracion"));
                Toast.makeText(getBaseContext(), "Canción añadida", Toast.LENGTH_SHORT).show();
                adaptador.notifyDataSetChanged();//Refresca adaptador.
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                //AdapterView
                adaptador.editCancion(new Cancion(R.drawable.rihanna, bundle.getString("titulo"), bundle.getString("autor"), bundle.getString("duracion")), Integer.parseInt(bundle.getString("pos")));
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
        saveCanciones(datos);
    }

    public void saveCanciones(ArrayList<Cancion> d) {
        Cancion cancion = null;
        //  if(datos.isEmpty()){
        // Toast.makeText(getBaseContext(),"No hay canciones que guardar.",Toast.LENGTH_SHORT).show();
        // }
        //else{
        try {
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), "lista_canciones.txt");
            PrintWriter printWriter = new PrintWriter(f);
            for (int x = 0; x < datos.size(); x++) {
                cancion = new Cancion(datos.get(x).getIdentificador(), datos.get(x).getTitulo(), datos.get(x).getAutor(), datos.get(x).getDuracion());
                printWriter.println(cancion.toString());
            }
            printWriter.close();
        } catch (Exception e) {
            Log.e("Ficheros", "Error al escribir en SD");
        }
        //}
    }

    public void loadCanciones() {
        int id = 0;
        String titulo = null;
        String autor = null;
        String duracion = null;
        String sCadena;
        File ruta_sd = Environment.getExternalStorageDirectory();
        File f = new File(ruta_sd.getAbsolutePath(), "lista_canciones.txt");
        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            while ((sCadena = fin.readLine()) != null) {
                id = Integer.parseInt(sCadena);
                titulo = fin.readLine();
                autor = fin.readLine();
                duracion = fin.readLine();
                adaptador.addCancion(id, titulo, autor, duracion);
            }
            adaptador.notifyDataSetChanged();//Refresca adaptador.
        } catch (Exception e) {
            Log.e("Ficheros", "Error al leer en SD");
        }
    }
}