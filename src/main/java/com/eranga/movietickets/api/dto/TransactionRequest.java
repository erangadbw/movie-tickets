package com.eranga.movietickets.api.dto;

import com.eranga.movietickets.api.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
        @NotNull(message = "TransactionId cannot be Null")
        Integer transactionId;
        @NotNull
        @Size(min =1, message="Size must be at least 1")
        @Valid
        List<Customer> customers;

}
