package org.mindswap.springtheknife.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "restaurants")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Restaurant must have a name.")
    private String name;
    @Setter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "street")),
            @AttributeOverride(name = "number", column = @Column(name = "door_number")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "zip_code"))
    })
    private Address address;
    @Setter
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Restaurant must have a phone number.")
    @Column(unique = true)
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    @Setter
    private Double rating;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<UserExperience> userExperienceList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Booking> bookingList;

    @ManyToMany(mappedBy = "favoriteRestaurants", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> usersWhoFavorited = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "restaurants_by_type",
               joinColumns = @JoinColumn(name = "restaurant_id"),
               inverseJoinColumns = @JoinColumn(name = "restaurant_type_id"))
    private List<RestaurantType> restaurantTypes;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    Set<RestaurantImage> restaurantImages;
}