package com.example.consultationsystem.repository;
import com.example.consultationsystem.entity.Lesson;
import com.example.consultationsystem.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    // No extra queries needed for this use case.
}
