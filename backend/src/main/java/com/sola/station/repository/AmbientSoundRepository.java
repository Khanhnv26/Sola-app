package com.sola.station.repository;

import com.sola.station.entity.AmbientSound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AmbientSoundRepository extends JpaRepository<AmbientSound, UUID> {
    List<AmbientSound> findByIsActiveTrueOrderByCodeAsc();
}
