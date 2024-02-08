package payroll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerMockitoIntegrationTest {
	
	@Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private EmployeeService service;
    
    @MockBean
    private EmployeeModelAssembler assembler;

    @Test
    void whenGetAllEmployees_thenReceiveOkStatusAndEmployeeList() {
        // Given
        Employee employee1 = new Employee(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");
        Employee employee2 = new Employee(2L, "San", "Kadiyala", "Student", "san.kadiyala@gmail.com");
        List<Employee> employees = Arrays.asList(employee1, employee2);

        EmployeeModel employeeModel1 = new EmployeeModel(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        EmployeeModel employeeModel2 = new EmployeeModel(2L, "San", "Kadiyala", "Student", "san.kadiyala@gmail.com");
        List<EntityModel<EmployeeModel>> employeeModels = Arrays.asList(
                EntityModel.of(employeeModel1, linkTo(methodOn(EmployeeController.class).one(employee1.getId())).withSelfRel()),
                EntityModel.of(employeeModel2, linkTo(methodOn(EmployeeController.class).one(employee2.getId())).withSelfRel())
        );

        when(service.findAllEmployees()).thenReturn(employees);
        when(assembler.toModel(employee1)).thenReturn(employeeModels.get(0));
        when(assembler.toModel(employee2)).thenReturn(employeeModels.get(1));

        // When
        ResponseEntity<CollectionModel<EntityModel<EmployeeModel>>> response = restTemplate.exchange("/employees", HttpMethod.GET, null, new ParameterizedTypeReference<CollectionModel<EntityModel<EmployeeModel>>>() {});

        // Then
        assertNotNull(service);
        assertNotNull(assembler);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getContent());
        assertEquals(2, response.getBody().getContent().size());
    }
    
    @Test
    void whenPostEmployee_thenReceiveCreatedStatusAndEmployeeDetails() {
        // Given
        Employee newEmployee = new Employee(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");
        EmployeeModel employeeModel = new EmployeeModel(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        EntityModel<EmployeeModel> entityModel = EntityModel.of(employeeModel, linkTo(methodOn(EmployeeController.class).one(newEmployee.getId())).withSelfRel());

        when(service.createEmployee(any(Employee.class))).thenReturn(newEmployee);
        when(assembler.toModel(any(Employee.class))).thenReturn(entityModel);

        // When
        HttpEntity<Employee> request = new HttpEntity<>(newEmployee);
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees",  HttpMethod.POST, request, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getContent());
        assertEquals(newEmployee.getFirstName(), response.getBody().getContent().getFirstName());
        assertEquals(newEmployee.getRole(), response.getBody().getContent().getRole());
    }
    
    @Test
    void whenGetEmployee_thenReceiveOkStatusAndEmployeeDetails() {
        // Given
        Long id = 1L;
        Employee employee = new Employee(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");
        EmployeeModel employeeModel = new EmployeeModel(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        EntityModel<EmployeeModel> entityModel = EntityModel.of(employeeModel, linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel());

        when(service.findEmployeeById(id)).thenReturn(employee);
        when(assembler.toModel(any(Employee.class))).thenReturn(entityModel);

        // When
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees/" + id, HttpMethod.GET, null, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getContent());
        assertEquals(employee.getFirstName(), response.getBody().getContent().getFirstName());
        assertEquals(employee.getRole(), response.getBody().getContent().getRole());
    }
    
    @Test
    void whenPutEmployee_thenReceiveCreatedStatusAndEmployeeDetails() {
        // Given
        Long id = 1L;
        Employee updatedEmployee = new Employee(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");
        EmployeeModel employeeModel = new EmployeeModel(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");
        EntityModel<EmployeeModel> entityModel = EntityModel.of(employeeModel, linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel());

        when(service.updateEmployee(any(Employee.class), eq(id))).thenReturn(updatedEmployee);
        when(assembler.toModel(any(Employee.class))).thenReturn(entityModel);

        // When
        HttpEntity<Employee> request = new HttpEntity<>(updatedEmployee);
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees/" + id, HttpMethod.PUT, request, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getContent());
        assertEquals(updatedEmployee.getFirstName(), response.getBody().getContent().getFirstName());
        assertEquals(updatedEmployee.getRole(), response.getBody().getContent().getRole());
    }
    
    @Test
    void whenDeleteEmployee_thenReceiveNoContentStatus() {
        // Given
        Long id = 1L;
        doNothing().when(service).deleteEmployeeById(id);

        // When
        ResponseEntity<String> response = restTemplate.exchange("/employees/" + id, HttpMethod.DELETE, null, String.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    

    @Test
    void whenGetNonExistingEmployee_thenReceiveNotFoundStatus() {
        // Given
        Long id = 1L;
        when(service.findEmployeeById(id)).thenThrow(new ResourceNotFoundException("Employee", "id", id));

        // When
        ResponseEntity<String> response = restTemplate.getForEntity("/employees/" + id, String.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    void whenPostEmployeeWithInvalidData_thenReceiveBadRequestStatus() {
        // Given
        Employee newEmployee = new Employee("", "", "", ""); // Invalid employee data

        // When
        HttpEntity<Employee> request = new HttpEntity<>(newEmployee);
        ResponseEntity<String> response = restTemplate.postForEntity("/employees", request, String.class);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
    @Test
    void whenPutNonExistingEmployee_thenReceiveNotFoundStatus() {
        // Given
        Employee updatedEmployee = new Employee(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");

        when(service.updateEmployee(any(Employee.class), eq(updatedEmployee.getId()))).thenThrow(new ResourceNotFoundException("Employee", "id", updatedEmployee.getId()));

        // When
        HttpEntity<Employee> request = new HttpEntity<>(updatedEmployee);
        ResponseEntity<String> response = restTemplate.exchange("/employees/" + updatedEmployee.getId(), HttpMethod.PUT, request, String.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    void whenDeleteNonExistingEmployee_thenReceiveNotFoundStatus() {
        // Given
        Long id = 1L;
        doThrow(new ResourceNotFoundException("Employee", "id", id)).when(service).deleteEmployeeById(id);

        // When
        ResponseEntity<String> response = restTemplate.exchange("/employees/" + id, HttpMethod.DELETE, null, String.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
