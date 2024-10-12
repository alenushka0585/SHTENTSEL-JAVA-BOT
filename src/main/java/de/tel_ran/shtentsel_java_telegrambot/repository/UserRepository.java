package de.tel_ran.shtentsel_java_telegrambot.repository;

import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);
}
