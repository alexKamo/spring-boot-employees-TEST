package com.luv2code.springboot.thymeleafdemo;

import com.luv2code.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    // EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    // Test findAll()
    @Test
    void testFindAll_ShouldReturnAllEmployeesOrderedByLastName() {
        // Given
        Employee employee1 = new Employee("John", "Doe", "john.doe@example.com");
        Employee employee2 = new Employee("Jane", "Smith", "jane.smith@example.com");
        Employee employee3 = new Employee("Aaa", "Abc","aaa.abc@gmail.com");

        when(employeeRepository.findAllByOrderByLastNameAsc()).thenReturn(Arrays.asList(employee1, employee2));

        // When
        List<Employee> employees = employeeService.findAll();

        // Then
        assertEquals(3, employees.size(), "The employee list size should be 2");
        assertEquals("Abc", employees.get(0).getLastName(), "The first employee's last name should be Abc");
        assertEquals("Doe", employees.get(1).getLastName(), "The first employee's last name should be Doe");
        assertEquals("Smith", employees.get(2).getLastName(), "The second employee's last name should be Smith");
    }
    // test findById

    @Test
    void testFindById_ShouldReturnEmployeeWhenExists(){

        //given
        int id = 1;
        Employee employee = new Employee(id,"Doe","John", "john.doe@gmail.com");
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        //when
        Employee result = employeeService.findById(id);

        //then
        assertNotNull(result, "The returned employee shouldn't be NULL");
        assertEquals(id,result.getId(),"id should be 1");
        assertEquals("john.doe@gmail.com",employee.getEmail());
    }

    // test findById when exc thrown
    @Test
    void testFindById_ShouldThrowExceptionWhenIdNOExist(){
        int id = -1;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,() -> employeeService.findById(id));

    }

    // test saveEmployee
    @Test
    void testSave(){
        Employee employee1 = new Employee("John", "Doe", "john.doe@example.com");
        employeeService.save(employee1);
        verify(employeeRepository,times(1)).save(employee1);
    }

    // test for deleteEmp
    @Test
    void testDelete(){
        int id = 1;
        Employee employee1 = new Employee("John", "Doe", "john.doe@example.com");
        employeeService.deleteById(id);
        verify(employeeRepository,times(1)).delete(employee1);
    }
}








