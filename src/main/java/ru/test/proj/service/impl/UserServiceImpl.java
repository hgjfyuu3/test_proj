package ru.test.proj.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.test.proj.dto.UserCreateDto;
import ru.test.proj.dto.UserSearchDto;
import ru.test.proj.dto.UserUpdateDto;
import ru.test.proj.dto.UsersDto;
import ru.test.proj.mapper.UsersMapper;
import ru.test.proj.model.*;
import ru.test.proj.repository.EmailDataEntityRepository;
import ru.test.proj.repository.PhoneDataEntityRepository;
import ru.test.proj.repository.UsersEntityRepository;
import ru.test.proj.service.UserCacheService;
import ru.test.proj.service.UserService;
import ru.test.proj.util.PrincipalUtil;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersEntityRepository usersEntityRepository;

    private final UserCacheService userCacheService;

    private final UsersMapper usersMapper;

    private final EmailDataEntityRepository emailDataEntityRepository;

    private final PhoneDataEntityRepository phoneDataEntityRepository;

    @Override
    public UsersDto getUsersById(Long id) {
        UsersEntity byId = userCacheService.getUserById(id);
        return usersMapper.toDto(byId);
    }

    @Override
    public UsersDto getUsersById(Principal principal) {
        Long id = PrincipalUtil.getIdFromPrincipal(principal);
        UsersEntity byId = userCacheService.getUserById(id);
        return usersMapper.toDto(byId);
    }

    @Override
    public Page<UsersDto> searchUsers(UserSearchDto userSearchDto, Pageable pageable) {
        Specification<UsersEntity> spec = (root, query, cb) -> {
            Join<UsersEntity, EmailDataEntity> usersEntityEmailDataEntityJoin = root.join("emailData", JoinType.LEFT);
            Join<UsersEntity, PhoneDataEntity> usersEntityPhoneDataEntityJoin = root.join("phoneData", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(userSearchDto.getName())
                    .map(e -> cb.like(root.get(UsersEntity_.name), e + "%"))
                    .ifPresent(predicates::add);

            Optional.ofNullable(userSearchDto.getDateOfBirth())
                    .map(e -> cb.greaterThan(root.get(UsersEntity_.birthday), e))
                    .ifPresent(predicates::add);

            Optional.ofNullable(userSearchDto.getEmail())
                    .map(e -> cb.equal(usersEntityEmailDataEntityJoin.get(EmailDataEntity_.email), e))
                    .ifPresent(predicates::add);

            Optional.ofNullable(userSearchDto.getPhone())
                    .map(e -> cb.equal(usersEntityPhoneDataEntityJoin.get(PhoneDataEntity_.phone), e))
                    .ifPresent(predicates::add);

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return usersEntityRepository.findAll(spec, pageable)
                .map(usersMapper::toDto);
    }

    @Override
    @Transactional
    public UsersDto updateUser(Principal principal, UserUpdateDto userUpdateDto) {
        Long id = PrincipalUtil.getIdFromPrincipal(principal);

        UsersEntity byId = userCacheService.getUserById(id);

        String oldEmail = userUpdateDto.getOldEmail();
        String newEmail = userUpdateDto.getNewEmail();
        String oldPhone = userUpdateDto.getOldPhone();
        String newPhone = userUpdateDto.getNewPhone();

        if (oldEmail != null && newEmail != null) {
            if (emailDataEntityRepository.existsByEmail(newEmail)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Email %s already exists", newEmail));
            }

            EmailDataEntity emailToUpdate = byId.getEmailData().stream()
                    .filter(emailData -> emailData.getEmail().equals(oldEmail))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old email not found"));

            emailToUpdate.setEmail(newEmail);
            emailDataEntityRepository.saveAndFlush(emailToUpdate);
        }
        if (oldPhone != null && newPhone != null) {
            if (phoneDataEntityRepository.existsByPhone(newPhone)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Phone %s already exists", newPhone));
            }

            PhoneDataEntity phoneToUpdate = byId.getPhoneData().stream()
                    .filter(emailData -> emailData.getPhone().equals(oldPhone))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old phone not found"));

            phoneToUpdate.setPhone(newPhone);
            phoneDataEntityRepository.saveAndFlush(phoneToUpdate);
        }
        return getUsersById(principal);
    }

    @Override
    @Transactional
    public void addPhoneOrEmail(Principal principal, UserCreateDto userCreateDto) {
        Long id = PrincipalUtil.getIdFromPrincipal(principal);
        UsersEntity byId = userCacheService.getUserById(id);

        String email = userCreateDto.getEmail();
        String phone = userCreateDto.getPhone();

        if (email != null) {
            if (emailDataEntityRepository.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Email %s already exists", email));
            }
            EmailDataEntity emailData = new EmailDataEntity();
            emailData.setUser(byId);
            emailData.setEmail(email);
            emailDataEntityRepository.saveAndFlush(emailData);
        }
        if (phone != null) {
            if (phoneDataEntityRepository.existsByPhone(phone)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Phone %s already exists", phone));
            }
            PhoneDataEntity phoneData = new PhoneDataEntity();
            phoneData.setUser(byId);
            phoneData.setPhone(phone);
            phoneDataEntityRepository.saveAndFlush(phoneData);
        }
    }

    @Override
    @Transactional
    public void deletePhoneOrEmail(Principal principal, UserCreateDto userCreateDto) {
        Long id = PrincipalUtil.getIdFromPrincipal(principal);
        UsersEntity byId = userCacheService.getUserById(id);

        String email = userCreateDto.getEmail();
        String phone = userCreateDto.getPhone();

        if (email != null) {
            EmailDataEntity emailToDelete = byId.getEmailData().stream()
                    .filter(emailData -> emailData.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old email not found"));

            emailDataEntityRepository.delete(emailToDelete);
        }
        if (phone != null) {
            PhoneDataEntity phoneToDelete = byId.getPhoneData().stream()
                    .filter(emailData -> emailData.getPhone().equals(phone))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old phone not found"));

            phoneDataEntityRepository.delete(phoneToDelete);
        }
    }

}