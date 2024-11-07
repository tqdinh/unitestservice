package com.example.myapplication.Domain.UseCase

import com.example.myapplication.Domain.Model.Todo

interface GetTodos {
    suspend  operator fun invoke() :List<Todo>
}