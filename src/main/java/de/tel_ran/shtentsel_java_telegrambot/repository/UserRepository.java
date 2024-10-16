package de.tel_ran.shtentsel_java_telegrambot.repository;

import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

    List<User> findBySubscriptionsIsNotEmpty();
    List<User> findByRolesRoleName(String roleName);
}
