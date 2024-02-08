package payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<EmployeeModel>> {
	
    @Autowired private ModelMapper modelMapper;
    
	@Override
	public EntityModel<EmployeeModel> toModel(Employee employee) {
		
		EmployeeModel employeeModel = modelMapper.map(employee, EmployeeModel.class);
		
		return EntityModel.of(employeeModel, //
				linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
	}
}
