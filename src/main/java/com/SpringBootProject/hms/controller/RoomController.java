package com.SpringBootProject.hms.controller;

import com.SpringBootProject.hms.constants.PathConstant;
import com.SpringBootProject.hms.entity.Room;
import com.SpringBootProject.hms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping(PathConstant.ADD_ROOM)
    public ResponseEntity<Room> addRoom(@RequestBody Room room){
        return ResponseEntity.ok((roomService.addRoom(room)));
    }

    @GetMapping(PathConstant.GET_ALL_ROOM)
    private ResponseEntity<List<Room>> getAllRooms(){
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping(PathConstant.GET_ROOM_BY_ID)
    private ResponseEntity<Optional<Room>> getRoomById(@PathVariable("id")Long id){
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping(PathConstant.UPDATE_ROOM)
    private ResponseEntity<Room> updateRoom(@PathVariable("id")Long id, @RequestBody Room room){
        return ResponseEntity.ok(roomService.updateRoom(id,room));
    }

    @GetMapping(PathConstant.GET_ROOM_BY_TYPE)
    private Room getRoomByType(@PathVariable("type")String type){
        return roomService.getRoomByType(type);
    }
}
