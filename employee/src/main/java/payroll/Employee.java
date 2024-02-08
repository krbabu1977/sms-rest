package payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor

public class Employee {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String firstName;
	private String lastName;
	private String role;
	private String email;
	
	public Employee(String firstName, String lastName, String role, String email) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.email = email;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

}
