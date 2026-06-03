package com.carldev.alerta_recife.entity;


import com.carldev.alerta_recife.utils.IntensityOfTheFlooding;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EnableJpaAuditing
@Table(name = "flooding_points")
public class FloodingPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String logger;

    @Column(name = "reference_point")
    private String referencePoint;

    private String description;

    @Column(nullable = false)
    private String neighborhood;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point coordinates;

    @Enumerated(EnumType.STRING)
    private IntensityOfTheFlooding intensity;

    private boolean active = true;

    @CreatedDate
    private LocalDateTime registryDate = LocalDateTime.now();

    private LocalDateTime actualizationDate;

    private Integer confirmationVotes = 0;

    @OneToMany(mappedBy = "floodingPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FloodingPointImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAuth createdBy;

    public void addImage(FloodingPointImage image) {

        if (this.images == null) {
            this.images = new ArrayList<>();
        }

        images.add(image);
        image.setFloodingPoint(this);
    }
}
