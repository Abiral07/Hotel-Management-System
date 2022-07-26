package com.SpringBootProject.hms.service.Impl;

import com.SpringBootProject.hms.entity.Room;
import com.SpringBootProject.hms.entity.RoomType;
import com.SpringBootProject.hms.exceptions.ResourceNotFoundException;
import com.SpringBootProject.hms.repo.RoomRepo;
import com.SpringBootProject.hms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepo roomRepo;

    @Override
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    @Override
    public Optional<Room> getRoomById(Long id) {
        return roomRepo.findById(id);
    }

    @Override
    public Room updateRoom(Long id, Room room) {
        Room updateRoom = roomRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room", "Id", id));

        if (Objects.nonNull(room.getType()) && !"".equalsIgnoreCase(room.getType().toString())) {
            try {
                updateRoom.setType(room.getType());
            } catch (Exception e) {
                System.out.println("ROOM-TYPE ERROR: " + e.getMessage());
            }
        }
        if (Objects.nonNull(room.getPrice()) && !"0.0".equalsIgnoreCase(room.getPrice().toString())) {
            updateRoom.setPrice(room.getPrice());
        }
        if (Objects.nonNull(room.getStatus()) && !"".equalsIgnoreCase(room.getStatus().toString())) {
            updateRoom.setStatus(room.getStatus());
        }
        return roomRepo.save(updateRoom);
    }

    @Override
    public Room addRoom(Room room) {
        return roomRepo.save(room);
    }

    @Override
    public Room getRoomByType(String type) {
        RoomType roomType = RoomType.valueOf(type.toUpperCase());
        System.out.println(roomType);
        return roomRepo.findByType(type);
    }
}
