package com.roboteon.warehouse.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;
    @Lob
    @Column(name = "image_data")
    private byte[] data;
    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aisle> aisles;

    public Image(byte[] data) {
        this.data = data;
        this.uploadDate = new Date();
    }
}
