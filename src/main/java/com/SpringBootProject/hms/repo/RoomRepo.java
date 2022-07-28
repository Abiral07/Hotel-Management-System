package com.SpringBootProject.hms.repo;

import com.SpringBootProject.hms.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
    @Query(value = "select * from hms.rooms as r where r.type = :type AND r.status='false' limit 1", nativeQuery = true)
    Room findByType(String type);

    @Query(value = "SELECT r.* FROM hms.rooms as r left join hms.reservation as rv on r.rid = rv.rid where r.type = :type AND r.status = 0 limit 1", nativeQuery = true)
    Room findEmptyRoom(String type);

    @Modifying
    @Query(value = "UPDATE Room r SET r.status = :status WHERE r.rid = :rid")
    void updateRoomStatus(Long rid, boolean status);
}
