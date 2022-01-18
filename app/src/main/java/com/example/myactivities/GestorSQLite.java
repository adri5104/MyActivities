/* -----------------------------------------------
    No. Matricula       Nombre

        18755           María Suárez de Deza de Rábago

        17707           Javier Bascuñana Labrador

        18300           Adrián Siegbert Rieker González


        repositorio del proyecto: https://github.com/adri5104/MyActivities.git

--------------------------------------------------
 */

package com.example.myactivities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;


//La base de datos tiene una segunda columna de color, para poder implementar
//en un futuro distintos colores para las actividades
public class GestorSQLite extends SQLiteOpenHelper{


    private static final String TABLE_NAME = "list";

    public GestorSQLite(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,nombre,factory,version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table lista(nombre text primary key, color_ text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists lista");
        sqLiteDatabase.execSQL("create table lista(nombre text primary key, color_ text)");

    }

}
