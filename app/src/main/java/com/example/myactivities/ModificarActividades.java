/* -----------------------------------------------
    No. Matricula       Nombre

        18755           María Suárez de Deza de Rábago

        17707           Javier Bascuñana Labrador

        18300           Adrián Siegbert Rieker González


        repositorio del proyecto: https://github.com/adri5104/MyActivities.git

--------------------------------------------------
 */

package com.example.myactivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;



public class ModificarActividades extends AppCompatActivity  {

    private EditText nomActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_actividades);

        nomActividad = (EditText) findViewById(R.id.campoActividad);

        Button boton1 = (Button) findViewById(R.id.nuevo);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alta(view);
            }
        });

        Button boton2 = (Button) findViewById(R.id.borrar);
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baja(view);
            }
        });

        Button boton3 = (Button) findViewById(R.id.borrarTodo);
        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarTodo(view);
            }
        });
    }

    public void alta(View view)
    {
        GestorSQLite admin = new GestorSQLite(view.getContext(), "administracion", null , 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String nombre = nomActividad.getText().toString();
        ContentValues registro = new ContentValues();

        registro.put("nombre", nombre);
        //registro.put("color_", colorSeleccionado);

        bd.insert("lista",null,registro);
        Toast.makeText(view.getContext(), getResources().getString(R.string.textoAlta), Toast.LENGTH_SHORT).show();
        bd.close();
        nomActividad.setText("");

    }


    //Este metodo no funciona y no sabemos por que
    public void baja(View view)
    {
        GestorSQLite admin = new GestorSQLite(view.getContext(), "administracion", null , 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String nombre = nomActividad.getText().toString();

        int cant = bd.delete("lista", "nombre = '" + nombre+"'", null);
        bd.close();
        nomActividad.setText("");

        if(cant == 1)
        {
            Toast.makeText(view.getContext(), getResources().getString(R.string.textoBaja), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(view.getContext(), getResources().getString(R.string.textoNoBaja), Toast.LENGTH_SHORT).show();
        }
    }

    public void borrarTodo(View view)
    {
        AlertDialog.Builder avisoBorrado = new AlertDialog.Builder(this);
        avisoBorrado.setTitle(getResources().getString(R.string.avisoBorrarTodo));
        avisoBorrado.setMessage(getResources().getString(R.string.confirmacionBorrarTodo));
        avisoBorrado.setPositiveButton(getResources().getString(R.string.confirmacion), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GestorSQLite admin = new GestorSQLite(view.getContext(), "administracion", null , 1);
                SQLiteDatabase bd = admin.getWritableDatabase();

                String nombre_ = nomActividad.getText().toString();

                int cant = bd.delete("lista", null, null);
                bd.close();
                nomActividad.setText("");
            }
        });

        avisoBorrado.setNeutralButton(getResources().getString(R.string.cancelacion), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //no se hace nada
            }
        });
        avisoBorrado.show();

        /*
         */
    }



}