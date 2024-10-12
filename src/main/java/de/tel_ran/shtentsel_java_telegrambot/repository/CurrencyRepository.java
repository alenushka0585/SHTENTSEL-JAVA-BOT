package de.tel_ran.shtentsel_java_telegrambot.repository;

import de.tel_ran.shtentsel_java_telegrambot.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository <Currency, Long> {

    Currency findByCode(String code);
}
