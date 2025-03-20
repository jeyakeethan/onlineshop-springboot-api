package com.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;  // Unique identifier for the image

    @Lob  // Indicates that this field will be stored as a BLOB in the database
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;  // Binary data representing the image

    @Column(name = "image_name", nullable = false)
    private String imageName;  // The name of the image (optional)

    @Column(name = "image_type", nullable = false)
    private String imageType;  // The type of the image (e.g., image/jpeg, image/png)

    @ManyToOne  // If this image is associated with a SKU, for example
    @JoinColumn(name = "sku_id")
    private SKU sku;  // The SKU this image belongs to, if relevant

}
