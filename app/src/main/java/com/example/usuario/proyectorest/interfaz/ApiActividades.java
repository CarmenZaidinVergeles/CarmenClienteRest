package com.example.usuario.proyectorest.interfaz;

import com.example.usuario.proyectorest.clases.Actividad;
import com.example.usuario.proyectorest.clases.Profesor;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface ApiActividades {//Interfaz
    @GET("restful/api/actividad/carmen")
    Call<List<Actividad>> getActividades(); //Obtener actividades a través de @GET

    @GET("restful/api/profesor")
    Call<List<Profesor>> getProfesores(); //Obtener profesores a través de @GET

    @POST("restful/api/actividad")
    Call<Actividad> createActividad(@Body Actividad actividad); //Crear a través de @POST

    @PUT("restful/api/actividad")
    Call<Actividad> editaActividad(@Body Actividad actividad); //Editar a través de @PUT

    @DELETE("restful/api/actividad/{id}")
    Call<Actividad> deleteActividad(@Path("id") String id); //Borrar a través de @DELETE
}
