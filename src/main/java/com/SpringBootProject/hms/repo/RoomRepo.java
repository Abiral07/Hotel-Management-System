package com.SpringBootProject.hms.repo;

import com.SpringBootProject.hms.entity.Room;
import com.SpringBootProject.hms.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
    @Query(value = "select * from hms.rooms as r where r.type = :type AND r.status='false' limit 1",nativeQuery = true)
    Room findByType(String type);

    @Modifying
    @Query(value = "UPDATE Room r SET r.status = :status WHERE r.rid = :rid")
    void updateRoomStatus(Long rid, boolean status);
}
