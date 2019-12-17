package com.hendra.transaction.demo.dao;

import com.hendra.transaction.demo.exception.GeneralException;
import com.hendra.transaction.demo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings({"SqlResolve", "Unused", "WeakerAccess"})
@Repository
public class EmployeeDAO
{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Employee mapRowToEmployee(ResultSet resultSet, int rowNum) throws SQLException
    {
        Employee employee = new Employee();
        employee.setId(resultSet.getLong("id"));
        employee.setFirstName(resultSet.getString("first_name"));
        employee.setLastName(resultSet.getString("last_name"));
        employee.setYearlyIncome(resultSet.getLong("yearly_income"));

        return employee;
    }

    /*
     *Check if the employee for the given id exists in the database;
     */
    public boolean exists(long id) throws GeneralException
    {
        try
        {
            return jdbcTemplate.queryForObject("select count(*) from employees where id = ?", Integer.class, id) == 1;
        }
        catch (Exception ex)
        {
            throw new GeneralException("error when update: " + ex);
        }
    }

    /*
     * Return the employee for the given id
     */
    public Employee findOne(String ID) throws GeneralException
    {
        try
        {
            Long id = Long.parseLong(ID);
            String sqlQuery = "Select id, first_name, last_name, yearly_income " +
                    "from employees where id = ?";

            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToEmployee, id);
        }
        catch (Exception ex)
        {
            throw new GeneralException("error when update: " + ex);
        }
    }

    /*
     * Return a list of all employees that exist in the database
     */
    public List<Employee> findAll() throws GeneralException
    {
        try
        {
            return jdbcTemplate.query("Select id, first_name, last_name, yearly_income from employees", this::mapRowToEmployee);
        }
        catch (Exception ex)
        {
            throw new GeneralException("error when update: " + ex);
        }
    }

    /*
     * Creates the employee in database
     */
    public int save(Employee employee) throws GeneralException {

        try
        {
            return jdbcTemplate.update("Insert into employees(first_name, last_name, yearly_income) values (?,?,?)"
                    , employee.getFirstName()
                    , employee.getLastName()
                    , employee.getYearlyIncome());
        }
        catch (Exception ex)
        {
            throw new GeneralException("Failed when save employee to the table" + ex);
        }
    }

    /** Get Id from table
     *
     * @return
     */
    public Employee getId(String firstName)
    {
        String query = "Select id from employees where first_name = ?";
        String first_name = firstName;
        return jdbcTemplate.queryForObject(query, this::mapRowToEmployee, first_name);
    }

    /*
     * Creates the employee in database and return the id of created employee
     */
    public long saveAndReturnId(Employee employee) throws GeneralException
    {
        long id = 0;

        try
        {
            String sqlQuery = "insert into employees(first_name, last_name, yearly_income) " +
                    "values (?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
                stmt.setString(1, employee.getFirstName());
                stmt.setString(2, employee.getLastName());
                stmt.setLong(3, employee.getYearlyIncome());
                return stmt;
            }, keyHolder);

            id = keyHolder.getKey().longValue();
        }
        catch (Exception ex)
        {
            throw new GeneralException("error: " + ex);
        }

        return id;
    }

    /**
     * Creates the employee in database and return the id of created
     * The method uses SimpleJdbcInsert instead of JdbcTemplate
     */
    public long simpleSave(Employee employee)
    {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("employees")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(employee.toMap()).longValue();
    }

    /**
     * Updates the employee in database, Please ensure the given employee must have a valid id
     */
    public void update(Employee employee) throws GeneralException
    {
        try
        {
            jdbcTemplate.update("Update employees set first_name = ? , last_name = ?, yearly_income = ? where id = ?"
                    , employee.getFirstName()
                    , employee.getLastName()
                    , employee.getYearlyIncome()
                    , employee.getId());
        }
        catch (Exception ex)
        {
            throw new GeneralException("error when update: " + ex);
        }
    }

    /**
     * Deletes the employee for the given id
     */
    public boolean delete(String ID)
    {
        Long id = Long.parseLong(ID);
        return jdbcTemplate.update("Delete from employees where id = ?", id) > 0;
    }
}
