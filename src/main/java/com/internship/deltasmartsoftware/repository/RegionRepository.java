package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Region;
import com.internship.deltasmartsoftware.repository.SoftDelete.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends SoftDeleteRepository<Region, Integer> {

    public List<Region> findAllByCityId(int cityId);
}
