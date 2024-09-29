package de.tel_ran.SHTENTSEL_JAVA_BOT.repository;

import de.tel_ran.SHTENTSEL_JAVA_BOT.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
}
