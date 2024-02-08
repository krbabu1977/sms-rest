package payroll;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerMockMvcUnitTests {
	
	@Autowired private MockMvc mvc;
	@MockBean private EmployeeService service;
	@Autowired private ObjectMapper objectMapper;
	
	@Test
	public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {
	    Employee bilbo = new Employee(1L, "Sam", "Kadiyala", "Student","sam.kadiyala@gmail.com");
	    Employee frodo = new Employee(2L, "San", "Kadiyala", "Student", "san.kadiyala@gmail.com");

	    List<Employee> employees = Arrays.asList(bilbo, frodo);

	    given(service.findAllEmployees()).willReturn(employees);

	    mvc.perform(get("/employees")) //
	            .andDo(print()) //
	            .andExpect(status().isOk()) //
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
	            .andExpect(jsonPath("$._embedded.employees[0].id", is(1)))
	            .andExpect(jsonPath("$._embedded.employees[0].firstName", is(bilbo.getFirstName())))
	            .andExpect(jsonPath("$._embedded.employees[0].lastName", is(bilbo.getLastName())))
	            .andExpect(jsonPath("$._embedded.employees[0].role", is(bilbo.getRole())))
				.andExpect(jsonPath("$._embedded.employees[0]._links.self.href", is("http://localhost/employees/1")))
				.andExpect(jsonPath("$._embedded.employees[0]._links.employees.href", is("http://localhost/employees")))
	            .andExpect(jsonPath("$._embedded.employees[1].id", is(2)))
	            .andExpect(jsonPath("$._embedded.employees[1].firstName", is(frodo.getFirstName())))
	            .andExpect(jsonPath("$._embedded.employees[1].lastName", is(frodo.getLastName())))
	            .andExpect(jsonPath("$._embedded.employees[1].role", is(frodo.getRole())))
			    .andExpect(jsonPath("$._embedded.employees[1]._links.self.href", is("http://localhost/employees/2")))
				.andExpect(jsonPath("$._embedded.employees[1]._links.employees.href", is("http://localhost/employees")))		
				.andExpect(jsonPath("$._links.self.href", is("http://localhost/employees")));
	}
	
	@Test
	public void givenEmployee_whenGetEmployee_thenStatus200() throws Exception {
	    Employee bilbo = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");

	    given(service.findEmployeeById(1L)).willReturn(bilbo);

	    mvc.perform(get("/employees/1")) //
	            .andDo(print()) //
	            .andExpect(status().isOk()) //
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
	            .andExpect(jsonPath("$.id", is(1)))
	            .andExpect(jsonPath("$.firstName", is(bilbo.getFirstName())))
	            .andExpect(jsonPath("$.lastName", is(bilbo.getLastName())))
	            .andExpect(jsonPath("$.role", is(bilbo.getRole())))
	            .andExpect(jsonPath("$._links.self.href", is("http://localhost/employees/1")))
	            .andExpect(jsonPath("$._links.employees.href", is("http://localhost/employees")));
	}
	
	@Test
	public void givenNoEmployee_whenGetEmployee_thenStatus404() throws Exception {
	    Long nonExistentId = 1L;

	    given(service.findEmployeeById(nonExistentId)).willThrow(new ResourceNotFoundException( "Employee", "id", nonExistentId));

	    mvc.perform(get("/employees/" + nonExistentId)) //
	            .andDo(print()) //
	            .andExpect(status().isNotFound())
	            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
	}
	
	@Test
	public void givenEmployee_whenPostEmployee_thenStatus201() throws Exception {
	    Employee bilbo = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");

	    given(service.createEmployee(any(Employee.class))).willReturn(bilbo);

	    mvc.perform(post("/employees")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(bilbo)))
	            .andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
	            .andExpect(jsonPath("$.id", is(1)))
	            .andExpect(jsonPath("$.firstName", is(bilbo.getFirstName())))
	            .andExpect(jsonPath("$.lastName", is(bilbo.getLastName())))
	            .andExpect(jsonPath("$.role", is(bilbo.getRole())))
	            .andExpect(jsonPath("$._links.self.href", is("http://localhost/employees/1")))
	            .andExpect(jsonPath("$._links.employees.href", is("http://localhost/employees")));
	}
	
	@Test
	public void givenEmployee_whenPutEmployee_thenStatus201() throws Exception {
	    Employee bilbo = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");

	    given(service.updateEmployee(any(Employee.class), any())).willReturn(bilbo);

	    mvc.perform(put("/employees/" + bilbo.getId())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(bilbo)))
	            .andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
	            .andExpect(jsonPath("$.id", is(1)))
	            .andExpect(jsonPath("$.firstName", is(bilbo.getFirstName())))
	            .andExpect(jsonPath("$.lastName", is(bilbo.getLastName())))
	            .andExpect(jsonPath("$.role", is(bilbo.getRole())))	            
	            .andExpect(jsonPath("$._links.self.href", is("http://localhost/employees/1")))
	            .andExpect(jsonPath("$._links.employees.href", is("http://localhost/employees")));
	}
	
	@Test
	public void givenNoEmployee_whenPutEmployee_thenStatus404() throws Exception {
	    Employee bilbo = new Employee(1L, "Sam", "Kadiyala", "Student", "sam.kadiyala@gmail.com");

	    given(service.updateEmployee(any(Employee.class), any())).willThrow(new ResourceNotFoundException( "Employee", "id", 1L));

	    mvc.perform(put("/employees/" + bilbo.getId())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(bilbo)))
	            .andExpect(status().isNotFound())
	            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
	}
	
	@Test
	public void givenEmployee_whenDeleteEmployee_thenStatus204() throws Exception {
	    Long existingId = 1L;

	    doNothing().when(service).deleteEmployeeById(existingId);

	    mvc.perform(delete("/employees/" + existingId))
	            .andExpect(status().isNoContent());
	}
	
	@Test
	public void givenNoEmployee_whenDeleteEmployee_thenStatus404() throws Exception {
	    Long nonExistentId = 1L;

	    doThrow(new ResourceNotFoundException( "Employee", "id", 1L)).when(service).deleteEmployeeById(nonExistentId);

	    mvc.perform(delete("/employees/" + nonExistentId))
	            .andExpect(status().isNotFound());
	}
}
