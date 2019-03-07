package com.example.employeeprofiles.repository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import com.example.employeeprofiles.PersistenceConfiguration;
import com.example.employeeprofiles.model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = ASSIGNABLE_TYPE,
        classes = {PersistenceConfiguration.class}
))
@ActiveProfiles(profiles = "test")
public class EmployeeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        Employee found = employeeRepository.findByLastName(alex.getLastName());

        // then
        assertThat(found.getLastName())
                .isEqualTo(alex.getLastName());
    }
}