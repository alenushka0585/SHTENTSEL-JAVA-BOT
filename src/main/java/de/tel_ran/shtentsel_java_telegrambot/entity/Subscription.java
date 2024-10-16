package de.tel_ran.shtentsel_java_telegrambot.entity;


import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subscriptions",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"subscriptionName"})})
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscription_name")
    @NonNull
    private String subscriptionName;

    @Column(name = "base_currency")
    @NonNull
    private String baseCurrency;

    @Column(name = "required_currency")
    @NonNull
    private String requiredCurrency;

    @Column(name = "is_active")
    @NonNull
    private Boolean isActive;

    @ToString.Exclude
    @ManyToMany(mappedBy = "subscriptions", fetch = FetchType.EAGER)
    private Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(subscriptionName, that.subscriptionName) &&
                Objects.equals(baseCurrency, that.baseCurrency) &&
                Objects.equals(requiredCurrency, that.requiredCurrency) &&
                Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subscriptionName, baseCurrency, requiredCurrency, isActive);
    }
}
