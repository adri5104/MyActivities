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
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    public final static String TEXTO_EXTRA = "com.example.Spinner.TEXTO";
    private Spinner MySpinner;
    private ArrayAdapter<String> adapter;
    private Button botonActualizar;
    private Button BotonProgramarActividadFutura;

    //Esta lista enlazada almacena temporalmente los nombres
    // de las actividades almacenadas en la BD para cargarlos en el spinner.
    // Se ha hecho de esta manera para que el contenido del spinner pueda
    // cambiar de forma dinamica. Se ha usado para ello la template list.
    List<String> list_nombres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySpinner=findViewById(R.id.spinner);
        MySpinner.setOnItemSelectedListener(this);

        //Para cargar los datos al spinner
        actualizar();
        botonActualizar = findViewById(R.id.update);
        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });

        BotonProgramarActividadFutura = (Button) findViewById(R.id.boton4);
        BotonProgramarActividadFutura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programarCalendar(view);
            }
        });

    }

    // Metodo privado para programar actividades en Google Calendar
    private void programarCalendar(View v)
    {
        Intent intent = new Intent (Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, MySpinner.getSelectedItem().toString());
        // intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
        intent.putExtra(CalendarContract.Events.EVENT_COLOR, "red");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, MySpinner.getSelectedItem().toString());
        intent.putExtra(CalendarContract.Events.ALL_DAY, false );

        startActivity(intent);
    }

    //Este metodo actualiza los datos del spinner cuando se han hecho cambios en la BD
    private void actualizar()
    {
        GestorSQLite admin = new GestorSQLite(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        Cursor columna = bd.rawQuery("select nombre from lista", null);

        list_nombres = new ArrayList<String>(); //Se inicializa la lista enlazada

        if (columna.moveToFirst()) {
            do {
                list_nombres.add(columna.getString(0));
            } while (columna.moveToNext());
        }



        columna.close();
        bd.close();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MySpinner.setAdapter(adapter);
    }

    //Abre la pantalla para anadir/ borrar actividades
    public void ModificarActividades(View view)
    {
        Intent solicitud = new Intent(this, ModificarActividades.class);
        startActivity(solicitud);
    }

    //Abre la pantalla de iniciar actividad
    public void NuevaPantalla(View view)
    {
        if(!list_nombres.isEmpty()) {
            Intent solicitud = new Intent(this, SegundaPantalla.class);
            String ElMensaje = MySpinner.getSelectedItem().toString();
            solicitud.putExtra(TEXTO_EXTRA, ElMensaje);
            startActivity(solicitud);
        }
        else
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.Texto_sin_actividad),Toast.LENGTH_SHORT).show();
    }

    //Abre la pantalla de iniciar actividad como deporte
    public void abrirPantallaDeporte(View view)
    {
        if(!list_nombres.isEmpty()) {
            Intent solicitud = new Intent(this, PantallaDeporte.class);
            String ElMensaje = MySpinner.getSelectedItem().toString();
            solicitud.putExtra(TEXTO_EXTRA, ElMensaje);
            startActivity(solicitud);
        }
        else
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.Texto_sin_actividad),Toast.LENGTH_SHORT).show();
    }


    //Abre el tutorial
    public void PantallaTutorial(View view)
    {
        AlertDialog.Builder tutorial = new AlertDialog.Builder(this);
        tutorial.setTitle(getResources().getString(R.string.tutorialTitulo));
        tutorial.setMessage(getResources().getString(R.string.tutorialTexto));
        tutorial.setNeutralButton(getResources().getString(R.string.tutorialCerrar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        tutorial.show();
    }

    //Este metodo nos permite obtener el item seleccionado en el spinner.
    //Se optiene la posicion que ocupa en la lista enlazada
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),list_nombres.get(position), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(),"" , Toast.LENGTH_LONG).show();
    }
}