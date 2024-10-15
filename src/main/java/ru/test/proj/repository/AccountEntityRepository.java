package ru.test.proj.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.test.proj.model.AccountEntity;

import java.util.Optional;

@Repository
public interface AccountEntityRepository extends JpaRepositoryImplementation<AccountEntity, Long> {

    Optional<AccountEntity> findByUserId(Long userId);

}