/* -----------------------------------------------
    No. Matricula       Nombre

        18755           María Suárez de Deza de Rábago

        17707           Javier Bascuñana Labrador

        18300           Adrián Siegbert Rieker González


        repositorio del proyecto: https://github.com/adri5104/MyActivities.git

--------------------------------------------------
 */


package com.example.myactivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;



//Los metodos de esta clase son los mismos que los de "SegundaPantalla"

public class FragmentoSuperior_deporte extends Fragment {

    private TextView myTextView;
    private String myActividad;

    private long startTime ; //Momento inicio actividad
    private long endTime; // Momento final de la actividad

    private TextView timerText;
    private Button stopStartButton;
    private Button botonTerminar;


    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    boolean timerStarted = false;
    boolean firsttimerStarted = false;

    public FragmentoSuperior_deporte() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        timer = new Timer();
        View view = inflater.inflate(R.layout.fragmento_superior_deporte,container,false);

        myActividad = getActivity().getIntent().getStringExtra(MainActivity.TEXTO_EXTRA).toString();

        myTextView = (TextView) view.findViewById(R.id.actividadDeportiva);
        myTextView.setText(myActividad);
        timerText = (TextView) view.findViewById(R.id.timerText_deporte);
        stopStartButton = (Button) view.findViewById(R.id.startStopButton2);
        stopStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop(getView());
            }
        });

        botonTerminar = view.findViewById(R.id.deporte_terminar);
        botonTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terminar(getView());
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void anadirCalendar(View view)
    {
        Intent intent = new Intent (Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, myActividad);
        // intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
        intent.putExtra(CalendarContract.Events.EVENT_COLOR, "red");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, myActividad);
        intent.putExtra(CalendarContract.Events.ALL_DAY, false );
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime );
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime );
        startActivity(intent);
    }

    private void startStop(View view)
    {
        Calendar c = Calendar.getInstance();
        if(timerStarted == false)
        {
            timerStarted = true;
            setButtonUI("STOP", R.color.white);
            if(firsttimerStarted == false)
            {
                startTime = c.getTimeInMillis();
            }
            firsttimerStarted = true;
            startTimer();
        }
        else
        {
            timerStarted = false;
            setButtonUI("START", R.color.white);
            endTime = c.getTimeInMillis();
            timerTask.cancel();
        }
    }

    private void setButtonUI(String start, int color)
    {
        stopStartButton.setText(start);
        stopStartButton.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                getActivity().runOnUiThread(new Runnable()
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

    private void terminar(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(getContext());
        resetAlert.setTitle("Terminar actividad");
        resetAlert.setMessage("¿Has terminado la actividad?\n\n Se mandaran los datos a Google Calendar.");
        resetAlert.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(timerTask != null)
                {
                    timerTask.cancel();
                    setButtonUI("START", R.color.purple_500);
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

        resetAlert.setNeutralButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing
            }
        });
        resetAlert.show();

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