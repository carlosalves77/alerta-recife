package com.carldev.alerta_recife.repository;

import com.carldev.alerta_recife.entity.FloodingPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FloodingPointsRepository extends JpaRepository<FloodingPoints, Long> {


    @Query("SELECT p FROM FloodingPoints p WHERE p.active = true")
    List<FloodingPoints> findFloodingPoints();

    @Query(value = "SELECT * FROM flooding_points l WHERE " +
            "ST_DWithin(l.coordinates::geography, ST_SetSRID(ST_Point(:lon, :lat), 4326)::geography, " +
            ":distance)",
            nativeQuery = true)
    Optional<FloodingPoints> findNearByActive(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("distance") double distanceInMeters);



}
