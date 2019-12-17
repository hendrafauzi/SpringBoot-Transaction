package com.hendra.transaction.demo.controller;

import com.hendra.transaction.demo.config.AppExceptionHandler;
import com.hendra.transaction.demo.dao.EmployeeDAO;
import com.hendra.transaction.demo.exception.GeneralException;
import com.hendra.transaction.demo.exception.NoContentException;
import com.hendra.transaction.demo.model.Employee;
import com.hendra.transaction.demo.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController
{
    private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeController(EmployeeDAO employeeDAO)
    {
        this.employeeDAO = employeeDAO;
    }

    /**
     * Getting All of Employees from the employee table
     * @return all employee
     */
    @GetMapping
    public List<Employee> getAllEmployee()
    {
        try
        {
            return employeeDAO.findAll();
        }
        catch (Exception ex)
        {
            throw new NoContentException(ex.toString());
        }
    }

    /**
     * Find one employee by id
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Employee getEmployeeById(@PathVariable("id") String id)
    {
        try
        {
            return employeeDAO.findOne(id);
        }
        catch (Exception ex)
        {
            throw new NoContentException(ex.toString());
        }
    }

    /**
     * Adding new Employee and return ID
     * @param employee .
     * @return ID
     */
    @PostMapping(value = "/add")
    public ResponseEntity addNewEmployee(@RequestBody Employee employee)
    {
        Response response = new Response();

        try
        {
            response.setStatus("200");
            response.setMessage(HttpStatus.OK.toString());
            response.setId(employeeDAO.saveAndReturnId(employee));
            return new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            throw new NoContentException(e.toString());
        }
    }

    @PostMapping(value = "/update")
    public ResponseEntity updateEmployee(@RequestBody Employee employee)
    {
        Response response = new Response();
        try
        {
            employeeDAO.update(employee);
            response.setStatus("200");
            response.setMessage(HttpStatus.OK.toString());
            return new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            throw new NoContentException(e.toString());
        }
    }

    @GetMapping(value = "/delete/{id}")
    public ResponseEntity deleteEmployeeById(@PathVariable("id") String id)
    {
        if (!employeeDAO.delete(id))
        {
            throw new NoContentException();
        }

        return new ResponseEntity(new Response("200", "Employees Deleted !!!"), HttpStatus.OK);
    }
}