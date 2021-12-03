package com.mazej.todo_list.database;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TodoListAPI {

    String BASE_URL = "https://www.miklavc.space/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Seznami opravil
    @GET("api/GroupChores")
    Call<List<GetTodoList>> getTodoLists(
            @Header("Authorization") String access_token);

    @POST("api/GroupChores")
    Call<PostTodoList> postTodoList(
            @Body PostTodoList todoList,
            @Header("Authorization") String access_token);

    @DELETE("api/GroupChores/{id}")
    Call<Void> deleteTodoList(
            @Header("Authorization") String access_token,
            @Path("id") String id);

    // Posamezna opravila
    @POST("api/Chore/{id}")
    Call<PostTask> postTask(
            @Body PostTask task,
            @Header("Authorization") String access_token,
            @Path("id") String id);

    @DELETE("api/Chore/{id}/{choreId}")
    Call<Void> deleteTask(
            @Header("Authorization") String access_token,
            @Path("id") String id,
            @Path("choreId") String choreId);

    @PUT("api/Chore/{id}/{choreId}")
    Call<PutTask> putTask(
            @Header("Authorization") String access_token,
            @Path("id") String id,
            @Path("choreId") String choreId);
}
