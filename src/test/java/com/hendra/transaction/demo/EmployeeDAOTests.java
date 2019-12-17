package com.hendra.transaction.demo;

import com.hendra.transaction.demo.dao.EmployeeDAO;
import com.hendra.transaction.demo.exception.GeneralException;
import com.hendra.transaction.demo.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeDAOTests
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private EmployeeDAO employeeDAO;

    private Employee employee  = new Employee();

    void hendraDTO()
    {
        employee.setId(1);
        employee.setFirstName("Hendra");
        employee.setLastName("dao test");
        employee.setYearlyIncome(2500);
    }

    void fauzi()
    {
        employee.setId(92);
        employee.setFirstName("fauzi");
        employee.setLastName("dao test");
        employee.setYearlyIncome(4500);
    }

    void nugraha()
    {
        employee.setId(93);
        employee.setFirstName("nugraha");
        employee.setLastName("dao test");
        employee.setYearlyIncome(1500);
    }

    @BeforeEach
    void injectedComponentsAreNotNull()
    {
        assertThat(jdbcTemplate).isNotNull();
        employeeDAO = new EmployeeDAO(jdbcTemplate);
    }

    @AfterEach
    void cleanUp()
    {
        jdbcTemplate.update("Delete from employees where id > 90");
    }

    @Test
    void testExistForExistingEmployees() throws GeneralException {
        boolean exists = employeeDAO.exists(1);

        assertThat(exists).isTrue();
    }

    @Test
    void testExistForNotExistingEmployee() throws GeneralException {
        boolean exists = employeeDAO.exists(-1);

        assertThat(exists).isFalse();
    }
}