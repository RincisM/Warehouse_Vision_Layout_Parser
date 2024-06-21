package com.roboteon.warehouse.Domain;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AisleDomain {
    private List<RackDomain> racks;
    private OrientationDomain orientation;

    public AisleDomain(RackDomain firstRack) {
        this.racks = new ArrayList<>();
        this.racks.add(firstRack);
        this.orientation = null;
    }

    public void add(RackDomain rack) {
        this.racks.add(rack);
    }
}
