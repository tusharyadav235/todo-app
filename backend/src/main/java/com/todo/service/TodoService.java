package com.todo.service;

import com.todo.model.Todo;
import com.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    // Saare todos laao
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // ID se ek todo laao
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    // Naya todo banao
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    // Todo update karo
    public Todo updateTodo(Long id, Todo updatedTodo) {
        return todoRepository.findById(id).map(todo -> {
            todo.setTitle(updatedTodo.getTitle());
            todo.setDescription(updatedTodo.getDescription());
            todo.setCompleted(updatedTodo.isCompleted());
            return todoRepository.save(todo);
        }).orElseThrow(() -> new RuntimeException("Todo nahi mila ID: " + id));
    }

    // Todo ka status toggle karo (complete/incomplete)
    public Todo toggleTodo(Long id) {
        return todoRepository.findById(id).map(todo -> {
            todo.setCompleted(!todo.isCompleted());
            return todoRepository.save(todo);
        }).orElseThrow(() -> new RuntimeException("Todo nahi mila ID: " + id));
    }

    // Todo delete karo
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    // Completed todos laao
    public List<Todo> getCompletedTodos() {
        return todoRepository.findByCompleted(true);
    }

    // Pending todos laao
    public List<Todo> getPendingTodos() {
        return todoRepository.findByCompleted(false);
    }
}
