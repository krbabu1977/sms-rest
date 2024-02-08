package payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EmployeeController {

	private final EmployeeService service;
	private final EmployeeModelAssembler assembler;

	public EmployeeController(EmployeeService service, EmployeeModelAssembler assembler) {

		this.service = service;
		this.assembler = assembler;
	}


	// Aggregate root

	@GetMapping("/employees")
	public ResponseEntity<CollectionModel<EntityModel<EmployeeModel>>> all() {

		List<EntityModel<EmployeeModel>> employees = service.findAllEmployees().stream() //
				.map(assembler::toModel) //
				.collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel()));
	}

	@PostMapping("/employees")
	public ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

		EntityModel<EmployeeModel> entityModel = assembler.toModel(service.createEmployee(newEmployee));

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	// Single item

	@GetMapping("/employees/{id}")
	public ResponseEntity<EntityModel<EmployeeModel>> one(@PathVariable Long id) {

		return ResponseEntity.ok(assembler.toModel(service.findEmployeeById(id)));
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

		EntityModel<EmployeeModel> entityModel = assembler.toModel(service.updateEmployee(newEmployee, id));		
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}	

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

		service.deleteEmployeeById(id);

		return ResponseEntity.noContent().build();
	}
}
