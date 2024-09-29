package de.tel_ran.SHTENTSEL_JAVA_BOT.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "subscribes")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String name;

    @NonNull
    private String baseCurrency;

    @NonNull
    private String requiredCurrency;

    @NonNull
    private Boolean isActive;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = true
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = true
    )
    private User user;

    public void setName() {
        this.name =
                baseCurrency + ":" + requiredCurrency;
    }
}
