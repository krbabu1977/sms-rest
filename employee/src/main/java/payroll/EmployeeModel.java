package payroll;

import org.springframework.hateoas.server.core.Relation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)

@Relation(collectionRelation = "employees", itemRelation = "employee")
public class EmployeeModel {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String role;
	private String email;
}
