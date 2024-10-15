package ru.test.proj.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.test.proj.model.PhoneDataEntity;

@Repository
public interface PhoneDataEntityRepository extends JpaRepositoryImplementation<PhoneDataEntity, Long> {

    boolean existsByPhone(String phone);

}