package com.hmsapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "room_availability")
public class RoomAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "room_type", nullable = false)
    private String room_type;

    @Column(name = "total_rooms", nullable = false)
    private Integer total_rooms;

    @Column(name = "nightly_price", nullable = false)
    private Long nightly_price;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(name = "date", nullable = false)
    private LocalDate date;

}