package com.eranga.movietickets.api.controller.integration;


import com.eranga.movietickets.api.controller.TicketCostController;
import com.eranga.movietickets.api.dto.TransactionRequest;
import com.eranga.movietickets.api.dto.TransactionResponse;
import com.eranga.movietickets.api.models.Customer;
import com.eranga.movietickets.api.models.Ticket;
import com.eranga.movietickets.api.models.TicketType;
import com.eranga.movietickets.service.TicketCostService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TickCostControllerTest {


    @MockBean
    TicketCostService ticketCostService;

    @Autowired
    WebTestClient webTestClient;

    private String transactionRequestRequestBody() {
        return """
                {
                    "transactionId": 3,
                    "customers": [ {
                            "name": "Jesse James",
                            "age": 36
                        },
                        {
                            "name": "Daniel Anderson",
                            "age": 95
                         },
                         {
                            "name": "Mary Jones",
                            "age": 15
                         },
                         {
                             "name": "Michelle Parker",
                             "age": 10
                         }
                    ]
                 } 
                """;
    }




    private TransactionResponse transactionExpectedResponseBody(){

        TransactionResponse transactionResponse = new TransactionResponse();
        List<Ticket> tickets = new ArrayList<Ticket>();
        transactionResponse.setTransactionId(3);

        Ticket ticketAdult = new Ticket(TicketType.Adult,1,new BigDecimal(25.00).setScale(2));
        Ticket ticketChildren = new Ticket(TicketType.Children,1,new BigDecimal(5.00).setScale(2));
        Ticket ticketSenior = new Ticket(TicketType.Senior,1,new BigDecimal(17.50).setScale(2));
        Ticket ticketTeen = new Ticket(TicketType.Teen,1,new BigDecimal(12).setScale(2));

        tickets.add(ticketAdult);
        tickets.add(ticketChildren);
        tickets.add(ticketSenior);
        tickets.add(ticketTeen);

        transactionResponse.setTickets(tickets);
        transactionResponse.setTotalCost(new BigDecimal(59.50).setScale(2));

        return transactionResponse;

    }





    @Test
    void getCorrectTicketCosts(){


        when(ticketCostService.createTicketTransactions(any(TransactionRequest.class))).thenReturn(transactionExpectedResponseBody());
        var response = webTestClient.post()
                .uri("api/ticketCost")
                .bodyValue(transactionRequestRequestBody())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectBody(TransactionResponse.class)
                .returnResult().getResponseBody();


        assertThat(response).isEqualTo(transactionExpectedResponseBody());

    }


}
