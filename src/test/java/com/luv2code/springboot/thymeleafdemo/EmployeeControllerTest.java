package com.luv2code.springboot.thymeleafdemo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.luv2code.springboot.thymeleafdemo.controller.EmployeeController;
import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class) // load only EmployeeController and other related component
public class EmployeeControllerTest {

    @Autowired // used for simulating HTTP request in Spring MVC project
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    /*
        test that /employees/list
        - returns 200 ok HTTP status
        - uses "employees/list-employees" as a view
        - adds a "employee" attribute to the model
     */

    @Test
    void testListEmployees() throws Exception {
//        Employee employee1 = new Employee("John", "Doe", "john.doe@example.com");
//        Employee employee2 = new Employee("Jane", "Smith", "jane.smith@example.com");
//        Employee employee3 = new Employee("Aaa", "Abc","aaa.abc@gmail.com");
//
//        when(employeeService.findAll()).thenReturn(Arrays.asList(employee1,employee2,employee3));
        when(employeeService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/employees/list")) // send a get request to /employees/list
                .andExpect(status().isOk())               // expect HTTP 200 OK
                .andExpect(view().name("employees/list-employees")) // verifies view name
                .andExpect(model().attributeExists("employees"));  // checks for is "employees" in model
    }

    /*
           test that /employees/showFormForAdd
           - returns 200 OK
           - uses "employees/employee-form" as the view
           - adds an empty "employees" attribute to the model

     */

    @Test
    void testShowFromForAdd() throws Exception {
        mockMvc.perform(get("/employees/showFormForAdd")) // send a get request to /employees/list
                .andExpect(status().isOk())               // expect HTTP 200 OK
                .andExpect(view().name("employees/employee-form")) // verifies view name
                .andExpect(model().attributeExists("employee"));  // checks for is "employees" in model
    }

    /*
        /employees/showFormForUpdate
        - return 200 OK
        - use "employees/employee-form" as the view name
        - adds and "employee" attribute populated from the empService

     */
    @Test
    void testSHowFormForUpdate() throws Exception {

        // send GET request to the given request
        Employee employee = new Employee("John","Doe", "jd@gmnail.com");
        when(employeeService.findById(1)).thenReturn(employee);
        mockMvc.perform(get("/employees/showFormForUpdate").param("employeeId","1"))
                // expect 200 OK status code
                .andExpect(status().isOk())
                // verify the view name
                .andExpect(view().name("employees/employee-form"))
                // verify employee attribute matches with data retrieved from DB
                .andExpect(model().attribute("employee", employee));

    }
    /*
        Test that /employees/save
        - redirect to /employees/list after saving an employee
     */
    @Test
    void testRedirectAfterSave() throws Exception {
        Employee employee = new Employee("John","Doe", "jd@gmnail.com");

        mockMvc.perform(post("/employees/save")  //send POST request to save endpoint
                        .flashAttr("employee",employee))  //add employee as model attribute
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employees/list"));

    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(get("/employees/delete")
                .param("employeeId","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employees/list"));
    }
}

