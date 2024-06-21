package com.roboteon.warehouse.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RackDomain {
    private int x;
    private int y;
    private int width;
    private int height;
}
