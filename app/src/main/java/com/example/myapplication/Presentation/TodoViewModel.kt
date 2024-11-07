package com.example.myapplication.Presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.Domain.Model.Todo
import com.example.myapplication.Domain.UseCase.GetTodos

class TodoViewModel constructor(private val getTodos: GetTodos) : ViewModel() {
    private val _todos = mutableListOf<Todo>()
    val todos: List<Todo> get() = _todos


    private val _errorMessage = mutableStateOf<String>("")
    val errorMessage: String get() = _errorMessage.value

    suspend fun getTodo() {
        try {
            _todos.clear()
            val result = getTodos()
            _todos.addAll(result)
        } catch (err: Exception) {
            _errorMessage.value = err.message.toString()
        }

    }
}