package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Town;
import com.internship.deltasmartsoftware.repository.SoftDelete.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TownRepository extends SoftDeleteRepository<Town, Integer> {
    public List<Town> findAllByRegionId(int regionId);
}
