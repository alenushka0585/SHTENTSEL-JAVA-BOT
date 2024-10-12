package de.tel_ran.shtentsel_java_telegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "currencies",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "name"})})
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String code;
    @NonNull
    private String name;
}
