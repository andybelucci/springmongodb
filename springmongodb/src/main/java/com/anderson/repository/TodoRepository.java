package com.anderson.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anderson.model.TodoDTO;

public interface TodoRepository extends MongoRepository<TodoDTO, String> {

}
