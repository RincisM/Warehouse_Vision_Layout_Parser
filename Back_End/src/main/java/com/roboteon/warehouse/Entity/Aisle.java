package com.roboteon.warehouse.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "aisles")
public class Aisle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aisleId;
    private int x;
    private int y;
    private int width;
    private int height;
    private int rackCount;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
