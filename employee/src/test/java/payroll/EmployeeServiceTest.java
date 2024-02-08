package payroll;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService = new EmployeeServiceImpl();
    
    @Test
    void whenFindAllEmployees_thenAllEmployeesShouldBeReturned() {
        // Given
        Employee employee1 = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        Employee employee2 = new Employee(2L, "San", "Kadiyala", "Student", "san.kadiyala@gmail.com");
        List<Employee> expectedEmployees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        // When
        List<Employee> actualEmployees = employeeService.findAllEmployees();

        // Then
        assertEquals(expectedEmployees, actualEmployees);
    }
    
 
    @Test
    void whenCreateEmployee_thenEmployeeShouldBeSavedAndReturned() {
        // Given
        Employee newEmployee = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        // When
        Employee savedEmployee = employeeService.createEmployee(newEmployee);

        // Then
        assertEquals(newEmployee, savedEmployee);
    }
    
    @Test
    void whenValidId_thenEmployeeShouldBeFound() {
        // Given
        Long id = 1L;
        Employee expectedEmployee = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        when(employeeRepository.findById(id)).thenReturn(Optional.of(expectedEmployee));

        // When
        Employee actualEmployee = employeeService.findEmployeeById(id);

        // Then
        assertEquals(expectedEmployee, actualEmployee);
    }
    
    @Test
    void whenUpdateEmployee_thenEmployeeShouldBeUpdatedAndReturned() {
        // Given

        Employee oldEmployee = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        Employee newEmployee = new Employee(1L, "San", "Kadiyala", "Student", "san.kadiyala@gmail.com");
        when(employeeRepository.findById(oldEmployee.getId())).thenReturn(Optional.of(oldEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Employee updatedEmployee = employeeService.updateEmployee(newEmployee, oldEmployee.getId());

        // Then
        assertEquals(newEmployee.getFirstName(), updatedEmployee.getFirstName());
        assertEquals(newEmployee.getLastName(), updatedEmployee.getLastName());
        assertEquals(newEmployee.getRole(), updatedEmployee.getRole());
    }
    
    @Test
    void whenDeleteEmployeeById_thenEmployeeShouldBeDeleted() {
        // Given

        Employee employee = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        // When
        employeeService.deleteEmployeeById(employee.getId());

        // Then
        verify(employeeRepository, times(1)).delete(employee);
    }
    
    @Test
    void whenDeleteNonExistingEmployeeById_thenExceptionShouldBeThrown() {
        // Given
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployeeById(id));
    }
    
    @Test
    void whenUpdateNonExistingEmployee_thenExceptionShouldBeThrown() {
        // Given
        Employee employee = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(employee, employee.getId()));
    }
    
    @Test
    void whenFindEmployeeByIdWithNonExistingId_thenExceptionShouldBeThrown() {
        // Given
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.findEmployeeById(id));
    }
}
