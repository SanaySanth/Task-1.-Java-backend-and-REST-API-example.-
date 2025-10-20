package com.example.demo.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecution {
    private Date startTime;
    private Date endTime;
    private String output;
}
