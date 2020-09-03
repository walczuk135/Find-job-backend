package com.findjob.findjobgradle.repository;

import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.domain.security.Role;
import com.findjob.findjobgradle.domain.security.RoleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(value = SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"})
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository repository;

    @Test
    void testFindByNameSuccess(){
        //given
        Role role=new Role(RoleType.ROLE_ADMIN);
        //when
        entityManager.persistAndFlush(role);
        Optional<Role> roleReturned = repository.findByName(RoleType.ROLE_ADMIN);
        //then
        assertThat(roleReturned.isPresent()).isTrue();
    }

    @Test
    void testFindByNameNotSuccess(){
        //given
        //when
        Optional<Role> roleReturned = repository.findByName(RoleType.ROLE_ADMIN);
        //then
        assertThat(roleReturned.isPresent()).isFalse();
    }
}
