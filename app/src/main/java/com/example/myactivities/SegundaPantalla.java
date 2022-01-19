/* -----------------------------------------------
    No. Matricula       Nombre

        18755           María Suárez de Deza de Rábago

        17707           Javier Bascuñana Labrador

        18300           Adrián Siegbert Rieker González


        repositorio del proyecto: https://github.com/adri5104/MyActivities.git

--------------------------------------------------
 */

package com.example.myactivities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.content.ContextCompat;


import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

public class SegundaPantalla extends AppCompatActivity {


    private String MyAsignatura;
    private TextView MyTextView;

    private long startTime ; //Momento inicio actividad
    private long endTime; // Momento final de la actividad

    private TextView timerText;

    private Button botonStart;
    private Button botonTerminar;

    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    boolean timerStarted = false;
    boolean firsttimerStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.segunda_pantalla);

        Intent solicitud  = getIntent();
        //Se recupera el dato mandado desde la otra pantalla
        MyAsignatura = solicitud.getStringExtra(MainActivity.TEXTO_EXTRA);
        MyTextView = (TextView) findViewById(R.id.Asignatura);
        MyTextView.setText(MyAsignatura);
        timerText = (TextView) findViewById(R.id.timerText);

        botonTerminar = (Button) findViewById(R.id.BotonTerminar_sp);
        botonTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terminarActividad(view);
            }
        });

        botonStart = (Button) findViewById(R.id.BotonStart_sp);
        botonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop(view);
            }
        });

        timer = new Timer();


    }

    //Este metodo es el que anade las actividades a google calendar
    private void anadirCalendar(View view)
    {
        Intent intent = new Intent (Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, MyAsignatura);
        // intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
        intent.putExtra(CalendarContract.Events.EVENT_COLOR, "red");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, MyAsignatura);
        intent.putExtra(CalendarContract.Events.ALL_DAY, false );
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime );
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime );
        startActivity(intent);
    }

    //Este metodo para el timer y calcula los tiempos
    private void terminarActividad(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle(getResources().getString(R.string.terminarTitulo));
        resetAlert.setMessage(getResources().getString(R.string.terminarTexto));

        resetAlert.setPositiveButton(getResources().getString(R.string.confirmacion), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(timerTask != null)
                {
                    timerTask.cancel();
                    setButtonUI(getResources().getString(R.string.boton_start_crono), R.color.purple_500);
                    time = 0.0;
                    timerText.setText(formatTime(0,0,0));
                    firsttimerStarted = false;
                    if(timerStarted == true)
                    {
                        Calendar c = Calendar.getInstance();
                        endTime = c.getTimeInMillis();
                    }
                    timerStarted = false;
                    anadirCalendar(view);
                }
            }
        });

        resetAlert.setNeutralButton(getResources().getString(R.string.cancelacion), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });
        resetAlert.show();

    }

    //Metodo para activar/ pausar el cronometro
    private void startStop(View view)
    {
        Calendar c = Calendar.getInstance();
        if(timerStarted == false)
        {
            timerStarted = true;
            setButtonUI(getResources().getString(R.string.stop), R.color.purple_500);
            if(firsttimerStarted == false)
            {
                startTime = c.getTimeInMillis(); //Para calendar
            }
            firsttimerStarted = true;
            startTimer();
        }
        else
        {
            timerStarted = false;
            setButtonUI(getResources().getString(R.string.start), R.color.white);
            endTime = c.getTimeInMillis();
            timerTask.cancel();
        }
    }

    //cambia color del boton
    private void setButtonUI(String start, int color)
    {
        botonStart.setText(start);
        botonStart.setTextColor(ContextCompat.getColor(this, color));
    }


    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }

    // Obtiene el tiempo en formato hora,dia,segundos
    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int segundos = ((rounded % 86400) % 3600) % 60;
        int minutos = ((rounded % 86400) % 3600) / 60;
        int horas = ((rounded % 86400) / 3600);

        return formatTime(segundos, minutos, horas);
    }


    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }
}