package com.example.dam2015.p33ejer6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hector on 19/01/2015.
 */
public class CancionesSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Canciones (id INTEGER, titulo TEXT,autor TEXT, duracion TEXT)";

    public CancionesSQLiteHelper(Context contexto, String nombre,SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creaci�n de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,int versionNueva) {
        //Se elimina la versi�n anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Canciones");

        //Se crea la nueva versi�n de la tabla
        db.execSQL(sqlCreate);
    }
}
