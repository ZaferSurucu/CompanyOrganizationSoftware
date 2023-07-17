package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TownRepository extends JpaRepository<Town, Integer> {
    public List<Town> findAllByRegionId(int regionId);
}
