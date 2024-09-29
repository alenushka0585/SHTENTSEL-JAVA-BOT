package de.tel_ran.SHTENTSEL_JAVA_BOT.repository;

import de.tel_ran.SHTENTSEL_JAVA_BOT.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository <Currency, Long> {

    Currency findByCode(String code);
}
