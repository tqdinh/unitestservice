package com.example.myapplication

import com.example.myapplication.Domain.UseCase.GetTodos
import com.example.myapplication.Presentation.TodoViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestViewModel {
    @Test
    fun test_ViewModel_should_exist()
    {
        val vm =TodoViewModel(GetMockTodoUseCase())

        assert(vm!=null)
    }

    @Test
    fun test_TodoList_should_return_empty_list()
    {
        val vm = TodoViewModel(GetMockTodoUseCase())
        assert(vm.todos.count() == 0)
    }

    @Test
    fun test_TodoList_should_return_4_Todo()
    {
        val vm = TodoViewModel(GetMockTodoUseCase())
        runBlocking {
            vm.getTodo()
        }
        assert(vm.todos.count() ==4)
    }

    @Test
    fun test_TodoList_should_show_error()
    {
        val vm = TodoViewModel(MockGetTodosThrowingUseCase())

            runBlocking {
                vm.getTodo()
            }
        assert(0 ==vm.todos.count() )
        assert("Usecase error" == vm.errorMessage)

    }

}