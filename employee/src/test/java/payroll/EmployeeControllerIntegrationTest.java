package payroll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//mvn test -Dtest=EmployeeControllerIntegrationTest#whenGetAllEmployees_thenEmployeeDetailsShouldMatch

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerIntegrationTest {
	
    @Autowired
    private TestRestTemplate restTemplate;

    @org.junit.jupiter.api.Order(1)
    @Test
    void whenGetAllEmployees_thenReceiveOkStatusAndEmployeeList() {
        // Given
        List<EmployeeModel> expectedEmployees = Arrays.asList(
            new EmployeeModel(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com"),
            new EmployeeModel(2L, "San", "Kadiyala", "Student", "san.kadiyala@gmail.com")
        );

        // When
        ResponseEntity<CollectionModel<EntityModel<EmployeeModel>>> response = restTemplate.exchange("/employees", HttpMethod.GET, null, new ParameterizedTypeReference<CollectionModel<EntityModel<EmployeeModel>>>() {});

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getContent());

        // Assert that all employees have the expected properties
        List<EntityModel<EmployeeModel>> actualEmployees = new ArrayList<>(response.getBody().getContent());
        for (int i = 0; i < expectedEmployees.size(); i++) {
            assertEquals(expectedEmployees.get(i).getId(), actualEmployees.get(i).getContent().getId());
            assertEquals(expectedEmployees.get(i).getFirstName(), actualEmployees.get(i).getContent().getFirstName());
            assertEquals(expectedEmployees.get(i).getLastName(), actualEmployees.get(i).getContent().getLastName());
            assertEquals(expectedEmployees.get(i).getRole(), actualEmployees.get(i).getContent().getRole());
        }
    }
    
    @org.junit.jupiter.api.Order(2)
    @Test
    void whenPostEmployee_thenReceiveCreatedStatusAndEmployeeDetails() {
        // Given
    	EmployeeModel newEmployee = new EmployeeModel(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");

        // When
        HttpEntity<EmployeeModel> request = new HttpEntity<>(newEmployee);
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees", HttpMethod.POST, request, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getContent());

        // Assert that the employee has the expected properties
        assertEquals(newEmployee.getId(), response.getBody().getContent().getId());
        assertEquals(newEmployee.getFirstName(), response.getBody().getContent().getFirstName());
        assertEquals(newEmployee.getLastName(), response.getBody().getContent().getLastName());
        assertEquals(newEmployee.getRole(), response.getBody().getContent().getRole());
    }
    
    @org.junit.jupiter.api.Order(3)
    @Test
    void whenGetEmployeeById_thenReceiveOkStatusAndEmployeeDetails() {
        // Given
    	EmployeeModel expectedEmployee = new EmployeeModel(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");

        // When
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees/" + expectedEmployee.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getContent());

        // Assert that the employee has the expected properties
        assertEquals(expectedEmployee.getId(), response.getBody().getContent().getId());
        assertEquals(expectedEmployee.getFirstName(), response.getBody().getContent().getFirstName());
        assertEquals(expectedEmployee.getLastName(), response.getBody().getContent().getLastName());
        assertEquals(expectedEmployee.getRole(), response.getBody().getContent().getRole());
        assertEquals(expectedEmployee.getRole(), response.getBody().getContent().getRole());
    }
    
    @org.junit.jupiter.api.Order(4)
    @Test
    void whenPutEmployee_thenEmployeeIsUpdated() {
        // Given
    	EmployeeModel newEmployee = new EmployeeModel(1L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");
      
        // When
        HttpEntity<EmployeeModel> request = new HttpEntity<>(newEmployee);
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees/" + newEmployee.getId(), HttpMethod.PUT, request, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getContent());
        assertEquals(newEmployee.getId(), response.getBody().getContent().getId());
        assertEquals(newEmployee.getFirstName(), response.getBody().getContent().getFirstName());
        assertEquals(newEmployee.getLastName(), response.getBody().getContent().getLastName());
        assertEquals(newEmployee.getRole(), response.getBody().getContent().getRole());
    }
   
    
    @org.junit.jupiter.api.Order(5)
    @Test
    void whenDeleteEmployeeById_thenReceiveNoContentStatus() {
        // Given
        Long id = 3L; // replace with an ID that exists in your database

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/employees/" + id, HttpMethod.DELETE, null, Void.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    
    @org.junit.jupiter.api.Order(6)
    @Test
    void whenGetNonExistentEmployee_thenReceiveNotFoundStatus() {
        // Given
        Long id = 999L; // replace with an ID that does not exist in your database

        // When
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees/" + id, HttpMethod.GET, null, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @org.junit.jupiter.api.Order(7)
    @Test
    void whenDeleteNonExistentEmployee_thenReceiveNotFoundStatus() {
        // Given
        Long id = 999L; // replace with an ID that does not exist in your database

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/employees/" + id, HttpMethod.DELETE, null, Void.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @org.junit.jupiter.api.Order(8)
    @Test
    void whenUpdateNonExistentEmployee_thenReceiveNotFoundStatus() {
        // Given
    	EmployeeModel updateEmployee = new EmployeeModel(999L, "Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com");

        // When
        HttpEntity<EmployeeModel> request = new HttpEntity<>(updateEmployee);
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees/" + updateEmployee.getId(), HttpMethod.PUT, request, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    /*
    @org.junit.jupiter.api.Order(9)
    @Test
    // TODO: This test is failing. Fix it.
    
    void whenPostEmployeeWithDuplicateInfo_thenReceiveConflictStatus() {
        // Given
    	EmployeeModel newEmployee1 = new EmployeeModel(999L, "Sam", "Kadiyla", "Student");
    	EmployeeModel newEmployee2 = new EmployeeModel(999L, "Sam", "Kadiyla", "Student"); // Duplicate info

        // When
        HttpEntity<EmployeeModel> request1 = new HttpEntity<>(newEmployee1);
        restTemplate.exchange("/employees", HttpMethod.POST, request1, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        HttpEntity<EmployeeModel> request2 = new HttpEntity<>(newEmployee2);
        ResponseEntity<EntityModel<EmployeeModel>> response = restTemplate.exchange("/employees", HttpMethod.POST, request2, new ParameterizedTypeReference<EntityModel<EmployeeModel>>() {});

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }*/
   
}