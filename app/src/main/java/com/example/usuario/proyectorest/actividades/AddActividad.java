package com.example.usuario.proyectorest.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.usuario.proyectorest.R;
import com.example.usuario.proyectorest.clases.Actividad;
import com.example.usuario.proyectorest.clases.Profesor;
import com.example.usuario.proyectorest.interfaz.ApiActividades;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AddActividad extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    public static final int FECHAINI=0, FECHAFIN=1;
    private Spinner spTipo;
    private Spinner spProfesor;
    private GregorianCalendar fechaInicio,fechaFin;
    private TextView tvInicio,tvFin;
    private String strfechaInicial, strfechaFin;
    private EditText edIni,edFin,edDesc;
    List<String> lista=new ArrayList<>();
    private String[] arrActividad = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        // Elegir actividad
        spTipo=(Spinner) findViewById(R.id.spTipo);
        arrActividad[0]=this.getString(R.string.excursion);
        arrActividad[1]=this.getString(R.string.extraescolar);
        arrActividad[2]=this.getString(R.string.complementaria);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, arrActividad);
        spTipo.setAdapter(spinnerArrayAdapter);
        spTipo.setOnItemSelectedListener(this);
        spTipo.setSelection(0);

        //Elegir profesor
        spProfesor=(Spinner) findViewById(R.id.spProf);
        recogerProf();

        String[] arrNom={"Pilar","Carmelo","Jaime","Modesto"};
        ArrayAdapter spinnerArrayAdapter2 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, arrNom);
        spProfesor.setAdapter(spinnerArrayAdapter2);
        spProfesor.setOnItemSelectedListener(this);
        spProfesor.setSelection(0);

        //fechas
        fechaInicio=new GregorianCalendar();
        fechaFin=new GregorianCalendar();

        tvInicio=(TextView) findViewById(R.id.tvFechaIni);
        tvFin=(TextView) findViewById(R.id.tvFechaFin);

        edIni=(EditText)findViewById(R.id.edLugarIni);
        edFin=(EditText)findViewById(R.id.edLugarFin);
        edDesc=(EditText)findViewById(R.id.edDesc);
    }



    public void recogerProf() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://ieszv.x10.bz/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiActividades apiActividades = retrofit.create(ApiActividades.class);
        Call<List<Profesor>> call = apiActividades.getProfesores();
        call.enqueue(new Callback<List<Profesor>>() {
            @Override
            public void onResponse(Response<List<Profesor>> response, Retrofit retrofit) {
                for (Profesor a : response.body()) {
                    lista.add(a.getNombre());

                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case FECHAINI:
                    fechaInicio.setTimeInMillis(data.getExtras().getLong("aux"));
                    Date d=fechaInicio.getTime();
                    String hora=data.getExtras().getString("hora");
                    if(hora.equals("")){
                        hora="00:00";
                    }
                    strfechaInicial=dateString(d)+" "+hora;
                    tvInicio.setText(strfechaInicial);
                    break;
                case FECHAFIN:
                    fechaFin.setTimeInMillis(data.getExtras().getLong("aux"));
                    Date d2=fechaFin.getTime();
                    String hora2=data.getExtras().getString("hora");
                    if(hora2.equals("")){
                        hora2="00:00";
                    }
                    strfechaFin=dateString(d2)+" "+hora2;
                    tvFin.setText(strfechaFin);
                    break;
            }
        }
    }

    public String dateString(Date d){
        return (d.getYear()+(1900)+"-"+(d.getMonth()+1)+"-"+d.getDate());
    }

    public void aceptar(View v){
        int pos=spProfesor.getSelectedItemPosition();
        String tipo=spTipo.getSelectedItem().toString();
        String strLugarIni=edIni.getText().toString()
                ,strLugarFin=edFin.getText().toString()
                ,strDesc=edDesc.getText().toString();

        Actividad a=new Actividad("",""+pos,tipo,strfechaInicial, strfechaFin,strLugarIni,strLugarFin,strDesc);

        crea(a);
        setResult(RESULT_OK);
        finish();

    }

    public void crea(Actividad a) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ieszv.x10.bz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiActividades apiActividades = retrofit.create(ApiActividades.class);

        final Call<Actividad> call = apiActividades.createActividad(a);

        System.out.println("CREAR ACTIVIDAD "+a);

        call.enqueue(new Callback<Actividad>() {
            @Override
            public void onResponse(Response<Actividad> response, Retrofit retrofit) {
                System.out.println("La URL es: " + response.raw());
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("FALLO EN DAR DE ALTA");
            }
        });
    }

    public void cancelar(View v){ //Boton cancelar
        setResult(RESULT_CANCELED);
        finish();
    }
    public void fechaIni(View v){ //Boton fecha inicial
        Intent i=new Intent(this,PulsaFecha.class);
        startActivityForResult(i,FECHAINI);
    }
    public void fechaFin(View v){ //Boton fecha final
        Intent i=new Intent(this,PulsaFecha.class);
        startActivityForResult(i,FECHAFIN);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
