package com.example.myapplication

import com.example.myapplication.Domain.Model.Todo
import com.example.myapplication.Domain.UseCase.GetTodos

class GetMockTodoUseCase : GetTodos {
    override suspend fun invoke(): List<Todo> {
        return listOf(
            Todo(1, "One", true),
            Todo(2, "Two", false),
            Todo(3, "Threee", true),
            Todo(4, "Four", false)
        )
    }
}