package com.eshop.app.service;

import com.eshop.app.model.UserModel;
import com.eshop.app.entity.RoleEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.repository.UserRepository;
import com.eshop.exception.common.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;
    @SpyBean
    UserService userService;

    @Test
    public void findByUserName_passUserNameThenShouldReturn1User() {
        //mock db
        UserEntity userEntity = new UserEntity(){{setUserName("admin");setPassword("admin");setActive(true);setEmail("a@a.com");setRoles(Set.of(new RoleEntity(){{setRole("ADMIN");}}));setFirstName("System");setLastName("Admin");}};
        when(userRepository.findByUserName("admin")).thenReturn(Optional.of(userEntity));

        //test findByUserName service
        UserModel userModel = userService.findByUserName("admin");
        assertThat(userModel).isNotNull();
        assertThat(userModel.getEmail()).isNotEmpty().isEqualTo("a@a.com");
    }

    @Test
    public void register_passUserModelThenShouldReturn1User() {
        //mock db
        UserEntity userEntity = new UserEntity() {{setId(UUID.randomUUID());setUserName("test");setPassword("test");setActive(true);setEmail("test@test.com");setRoles(Set.of(new RoleEntity() {{setRole("USER");}}));setFirstName("test");setLastName("test");}};
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        //test register service
        UserModel actual = userService.register(new UserModel());
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getPassword()).isNotEmpty().isEqualTo("test");
    }

    @Test
    public void register_passExistedUserModelThenShouldThrowsException() {
        //mock db
        UserEntity userEntity = new UserEntity() {{setId(UUID.randomUUID());setUserName("test");setPassword("test");setActive(true);setEmail("test@test.com");setRoles(Set.of(new RoleEntity() {{setRole("USER");}}));setFirstName("test");setLastName("test");}};
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(userEntity));

        //test register service
        UserModel userModel = new UserModel(){{setUserName("test");}};
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.register(userModel));
        assertThat(exception.getMessage()).isEqualTo("userName is already taken!");
    }
}
