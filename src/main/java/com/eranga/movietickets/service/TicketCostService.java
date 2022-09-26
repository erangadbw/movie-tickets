package com.eranga.movietickets.service;

import com.eranga.movietickets.api.dto.TransactionRequest;
import com.eranga.movietickets.api.dto.TransactionResponse;
import com.eranga.movietickets.api.exceptions.EmptyCustomersException;
import com.eranga.movietickets.api.models.Customer;
import com.eranga.movietickets.api.models.Ticket;
import com.eranga.movietickets.api.models.TicketType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public  class TicketCostService {

    BigDecimal ADULT_COST = new BigDecimal(25.00);
    BigDecimal SENIOR_COST = new BigDecimal(25.00*70/100);
    BigDecimal TEEN_COST = new BigDecimal(12.00);
    BigDecimal CHILDREN_COST = new BigDecimal(5.00);

    BigDecimal CHILDREN_DISCOUNT = new BigDecimal(0.75);
    int CHILDREN_DISCOUNT_COUNT = 3;


    int ADULT_LOWER_LIMIT = 18;
    int ADULT_UPPER_LIMIT = 65;
    int SENIOR_LOWER_LIMIT = 65;
    int SENIOR_UPPER_LIMIT = 999;
    int TEEN_LOWER_LIMIT = 11;
    int TEEN_UPPER_LIMIT = 18;
    int CHILDREN_LOWER_LIMIT = 0;
    int CHILDREN_UPPER_LIMIT = 11;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketCostService.class);

    public TransactionResponse createTicketTransactions(TransactionRequest transactionRequest){

        List<Ticket> tickets = new ArrayList<Ticket>();

        LOGGER.info("transaction recevied the transactionID = "+transactionRequest.getTransactionId());
        LOGGER.info("transaction recevied with customers = "+transactionRequest.getCustomers().size());

        var customers = transactionRequest.getCustomers();

        tickets = ifTicketNotNullAddToTickets(tickets,createTicketByType(customers,TicketType.Adult,ADULT_LOWER_LIMIT,ADULT_UPPER_LIMIT,ADULT_COST));
        tickets = ifTicketNotNullAddToTickets(tickets,createTicketByType(customers,TicketType.Children,CHILDREN_LOWER_LIMIT,CHILDREN_UPPER_LIMIT,CHILDREN_COST));
        tickets = ifTicketNotNullAddToTickets(tickets,createTicketByType(customers,TicketType.Senior,SENIOR_LOWER_LIMIT,SENIOR_UPPER_LIMIT,SENIOR_COST));
        tickets = ifTicketNotNullAddToTickets(tickets,createTicketByType(customers,TicketType.Teen,TEEN_LOWER_LIMIT,TEEN_UPPER_LIMIT,TEEN_COST));


        tickets.sort(Comparator.comparing(Ticket::ticketTypeToString));

        BigDecimal totalCost = tickets.stream().map(ticket->ticket.getTotalCost().setScale(2)).reduce(BigDecimal.ZERO,BigDecimal::add);

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .transactionId(transactionRequest.getTransactionId())
                .tickets(tickets)
                .totalCost(totalCost).build();

        return transactionResponse;
    }


    public Ticket createTicketByType(List<Customer> customers,TicketType ticketType,int lowerAgeLimit,int upperAgeLimit, BigDecimal cost){
        int count = 0; //change to customercount

        for(int i =0;i<customers.size();i++){
            if(customers.get(i).getAge() >= lowerAgeLimit && customers.get(i).getAge() < upperAgeLimit){
                count = count+1;
            }
        }

        BigDecimal bDcount = BigDecimal.valueOf(count);
        BigDecimal totalCost =  cost.setScale(2).multiply(bDcount);

        totalCost = applyChildrenDiscountIfAble(totalCost,ticketType,count);

        if(count >0) {
            Ticket ticket = new Ticket(ticketType,count,totalCost);
            return ticket;
        } else {
            return null;
        }
    }

    public BigDecimal applyChildrenDiscountIfAble(BigDecimal totalCost, TicketType ticketType, int count){
        if((ticketType == TicketType.Children) && (count >= CHILDREN_DISCOUNT_COUNT)){
            return totalCost.multiply(CHILDREN_DISCOUNT);
        } else {
            return totalCost;
        }

    }

    public List<Ticket> ifTicketNotNullAddToTickets(List<Ticket> tickets, Ticket ticket){
        if(!Objects.isNull(ticket)){
            tickets.add(ticket);
        }
        return tickets;
    }

}
