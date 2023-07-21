package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.City;
import com.internship.deltasmartsoftware.repository.SoftDelete.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends SoftDeleteRepository<City, Integer> {
}
