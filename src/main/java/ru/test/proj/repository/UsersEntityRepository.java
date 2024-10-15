package ru.test.proj.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.test.proj.model.UsersEntity;

import java.util.Optional;

@Repository
public interface UsersEntityRepository extends JpaRepositoryImplementation<UsersEntity, Long> {

    @Query("select u from UsersEntity u " +
            "left join u.emailData e " +
            "left join u.phoneData p " +
            "where e.email = :username or p.phone = :username")
    Optional<UsersEntity> findByEmailOrPhone(@Param("username") String username);

}