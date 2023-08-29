package ru.route.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.route.stats.model.Ticket;
import ru.route.stats.model.Tickets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteStatsByTickets {
    public static void main(String[] args) {
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        File ticketsJson = new File(args[0]);
        try {
            Tickets tickets = objectMapper.readValue(ticketsJson, Tickets.class);

            long sumOfPrices = 0;
            Map<String, Long> minFlightTimeByCarriers = new HashMap<>();
            List<Integer> pricesFromTicketsByRoute = new ArrayList<>();

            for (Ticket currentTicket : tickets.getTickets()) {
                if (currentTicket.getOrigin().equals(args[1].toUpperCase()) &&
                        currentTicket.getDestination().equals(args[2].toUpperCase())) {
                    if (!minFlightTimeByCarriers.containsKey(currentTicket.getCarrier())) {
                        minFlightTimeByCarriers.put(currentTicket.getCarrier(), currentTicket.getFlightTime());
                    } else {
                        if (currentTicket.getFlightTime() < minFlightTimeByCarriers.get(currentTicket.getCarrier())) {
                            minFlightTimeByCarriers.put(currentTicket.getCarrier(), currentTicket.getFlightTime());
                        }
                    }
                    pricesFromTicketsByRoute.add(currentTicket.getPrice());
                    sumOfPrices += currentTicket.getPrice();
                }
            }

            if (pricesFromTicketsByRoute.isEmpty()) {
                System.out.println("Tickets between " + args[1].toUpperCase() + " and " + args[2].toUpperCase() + " not present");
                return;
            }
            pricesFromTicketsByRoute.sort(Comparator.naturalOrder());

            double averagePrice = sumOfPrices / pricesFromTicketsByRoute.size();
            double medianOfPrice = 0L;
            if (pricesFromTicketsByRoute.size() / 2 == 1) {
                medianOfPrice = pricesFromTicketsByRoute.get(pricesFromTicketsByRoute.size() / 2);
            } else {
                int middle = pricesFromTicketsByRoute.size() / 2;
                medianOfPrice = (pricesFromTicketsByRoute.get(middle - 1) + pricesFromTicketsByRoute.get(middle)) / 2;
            }

            System.out.println("Tickets statistic in route " + args[1].toUpperCase() + " to " + args[2].toUpperCase());
            System.out.println("Difference between average price and median: " + (averagePrice - medianOfPrice));
            System.out.println("Minimum flight times by carriers:");
            for (String carrier : minFlightTimeByCarriers.keySet()) {
                long hours = minFlightTimeByCarriers.get(carrier) / 3600;
                long minutes = (minFlightTimeByCarriers.get(carrier) - hours * 3600) / 60;
                System.out.println(carrier + " : " + hours + " hours and " + minutes + " minutes");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
