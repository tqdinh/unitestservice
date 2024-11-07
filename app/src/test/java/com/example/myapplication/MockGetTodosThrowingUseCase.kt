package com.example.myapplication

import com.example.myapplication.Domain.Model.Todo
import com.example.myapplication.Domain.UseCase.GetTodos
import kotlin.jvm.Throws

class MockGetTodosThrowingUseCase :GetTodos {
    override suspend fun invoke(): List<Todo> {
        throw Exception("Usecase error")
    }
}