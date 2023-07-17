package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Integer> {

    public List<Region> findAllByCityId(int cityId);
}
