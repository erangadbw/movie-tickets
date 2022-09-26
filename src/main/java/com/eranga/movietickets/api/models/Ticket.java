package com.eranga.movietickets.api.models;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {


    TicketType ticketType;
    Integer quantity;
    BigDecimal totalCost;

    public String ticketTypeToString(){
        return getTicketType().toString();
    }


}
