package com.samplebackend.controller;



import org.springframework.web.bind.annotation.*;

import com.samplebackend.model.Ticket;
import com.samplebackend.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class traincontroller {

    private final List<Ticket> tickets = new ArrayList<>();
    private final AtomicInteger seatCounter = new AtomicInteger(0);

    @PostMapping("/purchase")
    public Ticket purchaseTicket(@RequestBody User user) {
        String from = "London";
        String to = "France";
        double pricePaid = 20.0;

        String seatSection = seatCounter.getAndIncrement() % 2 == 0 ? "A" : "B";

        Ticket ticket = new Ticket(from, to, user, pricePaid, seatSection);
        tickets.add(ticket);
        return ticket;
    }

    @GetMapping("/receipt")
    public Ticket getReceipt(@RequestParam String email) {
        return tickets.stream()
                .filter(ticket -> ticket.getUser().getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/seats")
    public List<User> getUsersBySection(@RequestParam String section) {
        List<User> users = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getSeatSection().equalsIgnoreCase(section)) {
                users.add(ticket.getUser());
            }
        }
        return users;
    }

    @DeleteMapping("/remove")
    public String removeUser(@RequestParam String email) {
        Ticket ticketToRemove = null;
        for (Ticket ticket : tickets) {
            if (ticket.getUser().getEmail().equals(email)) {
                ticketToRemove = ticket;
                break;
            }
        }
        if (ticketToRemove != null) {
            tickets.remove(ticketToRemove);
            return "User removed successfully";
        } else {
            return "User not found";
        }
    }

    @PutMapping("/modify")
    public String modifySeat(@RequestParam String email, @RequestParam String newSection) {
        for (Ticket ticket : tickets) {
            if (ticket.getUser().getEmail().equals(email)) {
                ticket.setSeatSection(newSection);
                return "Seat section modified successfully";
            }
        }
        return "User not found";
    }
}
