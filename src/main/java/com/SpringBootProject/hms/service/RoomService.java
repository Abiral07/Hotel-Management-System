package com.SpringBootProject.hms.service;

import com.SpringBootProject.hms.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    /**
     * @return list of all rooms
     */
    List<Room> getAllRooms();

    /**
     * @param id :id of room
     * @return the of corresponding id
     */
    Optional<Room> getRoomById(Long id);

    /**
     * @param id   :id of room to be updated
     * @param room room object containing update value
     * @return updated room
     */
    Room updateRoom(Long id, Room room);

    /**
     * @param room room body
     * @return added room
     */
    Room addRoom(Room room);

    /**
     * @param type :type of room to be searched by
     * @return list of rooms result of the search
     */
    Room getRoomByType(String type);
}
