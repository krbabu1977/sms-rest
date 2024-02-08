package payroll;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
    private EmployeeRepository employeeRepository;
	/*
	private final EmployeeRepository employeeRepository;
    
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    	this.employeeRepository = employeeRepository;
    }*/

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee, Long id) {
    	Employee updatedEmployee = findEmployeeById(id);
    	updatedEmployee.setFirstName(employee.getFirstName());
    	updatedEmployee.setLastName(employee.getLastName());
    	updatedEmployee.setRole(employee.getRole());
        return employeeRepository.save(updatedEmployee);
    }
    
    @Override
    public Employee createEmployee(Employee employee) {    	
        return employeeRepository.save(employee);
    }


    @Override
    public void deleteEmployeeById(Long id) {
        Employee employee = findEmployeeById(id);
        employeeRepository.delete(employee);
    }
}
