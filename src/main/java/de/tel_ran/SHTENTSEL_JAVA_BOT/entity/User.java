package de.tel_ran.SHTENTSEL_JAVA_BOT.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String userName;

    @NonNull
    @Column(unique = true)
    private Long chatId;

    @NonNull
    @JsonIgnore
    private String password;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER
    )
    private Set<Role> authorities = new HashSet<>();

    @ToString.Exclude
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER
    )
    private Set<Subscribe> subscribes = new HashSet<>();
}
