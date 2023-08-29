package ru.route.stats.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private String origin;

    @JsonSetter("origin_name")
    private String originName;

    private String destination;

    @JsonSetter("destination_name")
    private String destinationName;

    @JsonSetter("departure_date")
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate departureDate;

    @JsonSetter("departure_time")
    private String departureTime;

    @JsonSetter("arrival_date")
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate arrivalDate;

    @JsonSetter("arrival_time")
    private String arrivalTime;

    private String carrier;

    private int stops;

    private int price;

    public long getFlightTime() {
        if (departureTime.length() < 5) {
            this.setDepartureTime("0" + departureTime);
        }
        if (arrivalTime.length() < 5) {
            this.setArrivalTime("0" + arrivalTime);
        }
        LocalDateTime departure = LocalDateTime.of(departureDate, LocalTime.parse(departureTime));
        LocalDateTime arrival = LocalDateTime.of(arrivalDate, LocalTime.parse(arrivalTime));
        return Duration.between(departure, arrival).toSeconds();
    }
}
