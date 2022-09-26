package com.eranga.movietickets.api.models;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @NotNull(message ="name cannot be null")
    String name;
    @NotNull(message = "age cannot be null")
    Integer age;

}
