package com.internship.deltasmartsoftware.responses;

import com.internship.deltasmartsoftware.model.Region;
import com.internship.deltasmartsoftware.model.Town;
import lombok.Data;

import java.util.List;

@Data
public class RegionAndTownResponse {

    List<Region> regions;
    List<Town> towns;
}
