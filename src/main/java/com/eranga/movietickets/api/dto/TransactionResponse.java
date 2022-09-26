package com.eranga.movietickets.api.dto;

import com.eranga.movietickets.api.models.Ticket;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {


    Integer transactionId;
    List<Ticket> tickets;
    BigDecimal totalCost;

}
