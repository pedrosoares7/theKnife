package org.mindswap.springtheknife.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurant_type")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String type;

    @ManyToMany(mappedBy = "restaurantTypes")
    private Set<Restaurant> restaurants;



}
