package com.eranga.movietickets.api.controller;

import com.eranga.movietickets.api.dto.TransactionRequest;
import com.eranga.movietickets.api.dto.TransactionResponse;
import com.eranga.movietickets.service.TicketCostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path ="/")
public class TicketCostController {


    @Autowired
    private TicketCostService ticketCostService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketCostController.class);

    @PostMapping(value = "/api/ticketCost", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionResponse createTicketCosts(@Valid @RequestBody TransactionRequest transactionRequest){
       return ticketCostService.createTicketTransactions(transactionRequest);
    }

}
