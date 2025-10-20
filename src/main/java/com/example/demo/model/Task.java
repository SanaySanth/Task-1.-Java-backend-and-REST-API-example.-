package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    private String name;
    private String owner;
    private String command;
    private List<TaskExecution> taskExecutions = new ArrayList<>();
}
