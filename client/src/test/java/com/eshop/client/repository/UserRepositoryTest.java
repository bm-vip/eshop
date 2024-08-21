package com.eshop.client.repository;

import com.eshop.client.enums.RoleType;
import com.eshop.client.entity.RoleEntity;
import com.eshop.client.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UserRepositoryTest() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @Order(1)
    @Commit
    public void save_shouldSaveUserEntityToDatabase() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("bm_vip");
        userEntity.setPassword(passwordEncoder.encode("System.Admin"));
        userEntity.setActive(true);
        RoleEntity roleEntity = roleRepository.findByRole(RoleType.USER).get();
        userEntity.setRoles(new HashSet<>(Arrays.asList(roleEntity)));
        userRepository.save(userEntity);

        Assertions.assertThat(userEntity.getId()).isEqualTo(2);
    }

    @Test
    @Order(2)
    @Commit
    public void deleteById_shouldDeleteByIdFromDatabase() {
        userRepository.deleteById(1L);
        Optional<UserEntity> childOptional = userRepository.findById(1L);

        Assertions.assertThat(childOptional).isEmpty();
    }

    @Test
    void save_shouldNotNullExceptionThrown_thenAssertionSucceeds() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(new UserEntity()));

        Assertions.assertThat(exception.getMessage()).contains("must not be null");
    }

    @Test
    void save_shouldEmailValidationExceptionThrown_thenAssertionSucceeds() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserEntity entity = new UserEntity();
            entity.setUserName("userName");
            entity.setPassword("password");
            entity.setEmail("email");
            entity.setActive(true);
            userRepository.saveAndFlush(entity);
        });

        Assertions.assertThat(exception.getMessage()).contains("must be a well-formed email address");
    }

    @Test
    public void update_shouldUpdateUserEntityToDatabase() {
        UserEntity userEntity = userRepository.findById(2L).get();
        userEntity.setEmail("a@a.com");
        UserEntity userUpdated = userRepository.save(userEntity);

        Assertions.assertThat(userUpdated.getEmail()).isEqualTo("a@a.com");
    }

    @Test
    public void findById_shouldReturnCompanyEntity() {
        Optional<UserEntity> optional = userRepository.findById(2L);

        Assertions.assertThat(optional).isPresent();
    }

    @Test
    public void findAll_shouldReturnPageableCompanyEntities() {
        Page<UserEntity> page = userRepository.findAll(PageRequest.ofSize(10));

        Assertions.assertThat(page).isNotEmpty().size().isEqualTo(1);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void countAll_shouldReturnTotalNumberOfCompanies() {
        long count = userRepository.count();

        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    public void findByUserName_shouldReturnUserEntityAndMatchesByRawPassword() {
        Optional<UserEntity> optional = userRepository.findByUserName("bm_vip");

        Assertions.assertThat(optional).isPresent();
        Assertions.assertThat(passwordEncoder.matches("System.Admin", optional.get().getPassword())).isTrue();
    }
}
