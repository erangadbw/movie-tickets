package com.eranga.movietickets.service;

import com.eranga.movietickets.api.models.Customer;
import com.eranga.movietickets.api.models.Ticket;
import com.eranga.movietickets.api.models.TicketType;
import com.eranga.movietickets.service.TicketCostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class TickCostServiceIntTest {

    @Autowired
    private TicketCostService ticketCostService;


    private List<Customer> customers(){
        List<Customer> customers = new ArrayList<>();

        Customer adultCustomer = new Customer("Jesse",30);
        Customer SeniorCustomer = new Customer("Thomas",94);
        Customer childCustomer = new Customer("kim",9);
        Customer teenageCcustomer = new Customer("Alex",16);

        customers.add(adultCustomer);
        customers.add(SeniorCustomer);
        customers.add(childCustomer);
        customers.add(teenageCcustomer);

        return customers;
    }

    private Ticket expectedAdultTicket(){
        Ticket ticket = new Ticket(TicketType.Adult,1,ticketCostService.ADULT_COST.setScale(2));
        return ticket;
    }

    private Ticket expectedChildrenTicket(){
        Ticket ticket = new Ticket(TicketType.Children,1,ticketCostService.CHILDREN_COST.setScale(2));
        return ticket;

    }
    private Ticket expectedTeenTicket(){
        Ticket ticket = new Ticket(TicketType.Teen,1,ticketCostService.TEEN_COST.setScale(2));
        return ticket;
    }
    private Ticket expectedSeniorTicket(){
        Ticket ticket = new Ticket(TicketType.Senior,1,ticketCostService.SENIOR_COST.setScale(2));
        return ticket;

    }


    @Test
    void returnsCorrectTeenTicketsINT(){
        Ticket ticket = ticketCostService
                .createTicketByType(customers(),TicketType.Teen,ticketCostService.TEEN_LOWER_LIMIT,ticketCostService.TEEN_UPPER_LIMIT,ticketCostService.TEEN_COST);
        assertThat(ticket).isEqualTo(expectedTeenTicket());

    }

    @Test
    void returnsCorrectAdultTicketsINT(){
        Ticket ticket = ticketCostService
                .createTicketByType(customers(),TicketType.Adult,ticketCostService.ADULT_LOWER_LIMIT,ticketCostService.ADULT_UPPER_LIMIT,ticketCostService.ADULT_COST);
        assertThat(ticket).isEqualTo(expectedAdultTicket());
    }

    @Test
    void returnsCorrectSeniorTicketsINT(){
        Ticket ticket = ticketCostService
                .createTicketByType(customers(),TicketType.Senior,ticketCostService.SENIOR_LOWER_LIMIT,ticketCostService.SENIOR_UPPER_LIMIT,ticketCostService.SENIOR_COST);
        assertThat(ticket).isEqualTo(expectedSeniorTicket());
    }

    @Test
    void returnsCorrectChildrenTicketsINT(){
        Ticket ticket = ticketCostService
                .createTicketByType(customers(),TicketType.Children,ticketCostService.CHILDREN_LOWER_LIMIT,ticketCostService.CHILDREN_UPPER_LIMIT,ticketCostService.CHILDREN_COST);
        assertThat(ticket).isEqualTo(expectedChildrenTicket());
    }
}
