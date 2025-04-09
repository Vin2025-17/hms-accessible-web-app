package com.hmsapp.repository;

import com.hmsapp.entity.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {

    @Query("SELECT r FROM RoomAvailability r WHERE r.room_type = :roomType AND r.date BETWEEN :fromDate AND :toDate AND r.property.id = :propId")
    List<RoomAvailability> findRoomAvailability(@Param("fromDate") LocalDate fromDate,
                                                @Param("toDate") LocalDate toDate,
                                                @Param("roomType") String roomType,
                                                @Param("propId") Long propId);
}