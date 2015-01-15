package com.example.dam2015.p33ejer6;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;

public class adaptadorCanciones extends ArrayAdapter<Cancion>{
    Activity context;
    ArrayList<Cancion> ca;

    adaptadorCanciones(Activity context,ArrayList<Cancion> c){
        super(context, R.layout.activity_main,c);
        this.context=context;
        this.ca=c;
    }
    public void addCancion(int id,String titulo, String autor, String duracion){
        ca.add(new Cancion (id,titulo,autor,duracion));
    }

    public void editCancion(Cancion c,int posicion){
        ca.set(posicion,c);
    }
    public void delCancion(ArrayList<Cancion> c, int posi){
        c.remove(posi);//Borra cancion selecionada
    }
    public void deleteAll(ArrayList<Cancion> c){
        c.clear();
    }
    public View getView(int position, View convertView, ViewGroup parent)
{
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.mi_layout, null);

        ImageView imagen = (ImageView)item.findViewById(R.id.foto);
        imagen.setImageResource(ca.get(position).getIdentificador());

        TextView titulo = (TextView)item.findViewById(R.id.lblTitulo);
        titulo.setText(ca.get(position).getTitulo());
        TextView autor =(TextView)item.findViewById(R.id.lblAutor);
        autor.setText(ca.get(position).getAutor());
        TextView duracion =(TextView)item.findViewById(R.id.lblDuracion);
        duracion.setText(ca.get(position).getDuracion());
        return(item);
    }
}