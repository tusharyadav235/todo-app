package com.todo.repository;

import com.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    // Completed status ke basis par filter karo
    List<Todo> findByCompleted(boolean completed);

    // Title mein search karo
    List<Todo> findByTitleContainingIgnoreCase(String keyword);
}
