package ru.test.proj.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.test.proj.model.EmailDataEntity;

@Repository
public interface EmailDataEntityRepository extends JpaRepositoryImplementation<EmailDataEntity, Long> {

    boolean existsByEmail(String email);

}