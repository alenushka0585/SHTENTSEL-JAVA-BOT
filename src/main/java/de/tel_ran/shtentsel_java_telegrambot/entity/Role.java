package de.tel_ran.shtentsel_java_telegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"roleName"})})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    @NonNull
    private String roleName;

    @ToString.Exclude
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
