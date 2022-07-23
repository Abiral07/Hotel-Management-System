package com.SpringBootProject.hms.service;

import com.SpringBootProject.hms.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<Room> getAllRooms();

    Optional<Room> getRoomById(Long id);

    Room updateRoom(Long id,Room room);

    Room addRoom(Room room);

    Room getRoomByType(String type);
}
