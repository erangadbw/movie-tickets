package com.eranga.movietickets.service;

import com.eranga.movietickets.api.dto.TransactionRequest;
import com.eranga.movietickets.api.dto.TransactionResponse;
import com.eranga.movietickets.api.models.Customer;
import com.eranga.movietickets.api.models.Ticket;
import com.eranga.movietickets.api.models.TicketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class TickCosetServiceTest {



    private TicketCostService ticketCostServiceSPY;



    @BeforeEach
    public void setup(){
        ticketCostServiceSPY = spy(new TicketCostService());
    }

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

    private List<Ticket> tickets(){
        List<Ticket> tickets = new ArrayList<>();

        Ticket adultTicket = new Ticket(TicketType.Adult,1,new BigDecimal("25.00").setScale(2));
        Ticket childrenTicket = new Ticket(TicketType.Children,1,new BigDecimal("5.00").setScale(2));
        Ticket SeniorTicket = new Ticket(TicketType.Senior,1,new BigDecimal("17.50").setScale(2));
        Ticket TeenTicket = new Ticket(TicketType.Teen,1,new BigDecimal("12.00").setScale(2));

        tickets.add(adultTicket);
        tickets.add(childrenTicket);
        tickets.add(SeniorTicket);
        tickets.add(TeenTicket);

        return tickets;
    }

    private TransactionResponse transactionResponse(){
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionId(1);
        transactionResponse.setTickets(tickets());
        transactionResponse.setTotalCost(new BigDecimal("59.50").setScale(2));
        return transactionResponse;
    }

    private TransactionRequest transactionRequest(){
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTransactionId(1);
        transactionRequest.setCustomers(customers());
        return transactionRequest;

    }

    private Ticket expectedAdultTicket(){
        Ticket ticket = new Ticket(TicketType.Adult,1,ticketCostServiceSPY.ADULT_COST);
        return ticket;
    }

    private Ticket expectedChildrenTicket(){
        Ticket ticket = new Ticket(TicketType.Children,1,ticketCostServiceSPY.CHILDREN_COST);
        return ticket;

    }
    private Ticket expectedTeenTicket(){
        Ticket ticket = new Ticket(TicketType.Teen,1,ticketCostServiceSPY.TEEN_COST);
        return ticket;
    }
    private Ticket expectedSeniorTicket(){
        Ticket ticket = new Ticket(TicketType.Senior,1,ticketCostServiceSPY.SENIOR_COST);
        return ticket;

    }
    @Test
    void ifTicketisNotNullAddsTicket(){

        Ticket ticketAdult = new Ticket(TicketType.Adult,1,ticketCostServiceSPY.ADULT_COST);
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();

        ticketCostServiceSPY.ifTicketNotNullAddToTickets(tickets,ticketAdult);

        assertThat(tickets.size()).isEqualTo(1);
    }


    @Test
    void createsTransactionResponse(){

        List<Ticket> tickets = tickets();
        when(ticketCostServiceSPY.ifTicketNotNullAddToTickets(anyList(),any(Ticket.class))).thenReturn(tickets);
        assertThat(ticketCostServiceSPY.createTicketTransactions(transactionRequest())).isEqualTo(transactionResponse());

    }


    @Test
    void ifTicketIsNullDoesNotAddTicket(){

        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        ticketCostServiceSPY.ifTicketNotNullAddToTickets(tickets,null);
        assertThat(tickets.size()).isEqualTo(0);
    }

    @Test
    void calculatesCorrectChildrenDiscount(){
        BigDecimal totalCost = new BigDecimal(100.00);
        int count =3 ;

        BigDecimal discountedCost = ticketCostServiceSPY.applyChildrenDiscountIfAble(totalCost,TicketType.Children,5);
        assertThat(discountedCost).isEqualTo(new BigDecimal(75.00).setScale(2));

    }

    @Test
    void returnsCorrectTeenTickets(){

        Mockito.when(ticketCostServiceSPY.applyChildrenDiscountIfAble(any(BigDecimal.class),any(TicketType.class),anyInt())).thenReturn(new BigDecimal(12));

        Ticket ticket = ticketCostServiceSPY
                .createTicketByType(customers(),TicketType.Teen,ticketCostServiceSPY.TEEN_LOWER_LIMIT,ticketCostServiceSPY.TEEN_UPPER_LIMIT,ticketCostServiceSPY.TEEN_COST);
        assertThat(ticket).isEqualTo(expectedTeenTicket());

    }

    @Test
    void returnsCorrectAdultTickets(){

        Mockito.when(ticketCostServiceSPY.applyChildrenDiscountIfAble(any(BigDecimal.class),any(TicketType.class),anyInt())).thenReturn(new BigDecimal(25));

        Ticket ticket = ticketCostServiceSPY
                .createTicketByType(customers(),TicketType.Adult,ticketCostServiceSPY.ADULT_LOWER_LIMIT,ticketCostServiceSPY.ADULT_UPPER_LIMIT,ticketCostServiceSPY.ADULT_COST);
        assertThat(ticket).isEqualTo(expectedAdultTicket());
    }

    @Test
    void returnsCorrectSeniorTickets(){
        Mockito.when(ticketCostServiceSPY.applyChildrenDiscountIfAble(any(BigDecimal.class),any(TicketType.class),anyInt())).thenReturn(new BigDecimal(17.5));

        Ticket ticket = ticketCostServiceSPY
                .createTicketByType(customers(),TicketType.Senior,ticketCostServiceSPY.SENIOR_LOWER_LIMIT,ticketCostServiceSPY.SENIOR_UPPER_LIMIT,ticketCostServiceSPY.SENIOR_COST);
        assertThat(ticket).isEqualTo(expectedSeniorTicket());
    }

    @Test
    void returnsCorrectChildrenTickets(){

        Mockito.when(ticketCostServiceSPY.applyChildrenDiscountIfAble(any(BigDecimal.class),any(TicketType.class),anyInt())).thenReturn(new BigDecimal(5));
        Ticket ticket = ticketCostServiceSPY
                .createTicketByType(customers(),TicketType.Children,ticketCostServiceSPY.CHILDREN_LOWER_LIMIT,ticketCostServiceSPY.CHILDREN_UPPER_LIMIT,ticketCostServiceSPY.CHILDREN_COST);
        assertThat(ticket).isEqualTo(expectedChildrenTicket());
    }

}
