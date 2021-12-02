package com.mazej.todo_list.database;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TodoListAPI {

    String BASE_URL = "http://164.8.206.86:3000/";

    @GET("api/GroupChores")
    Call<List<GetTodoList>> getTodoLists(
            @Header("Authorization") String access_token);
}
