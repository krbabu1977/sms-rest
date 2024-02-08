package payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {

		return args -> {
			employeeRepository.save(new Employee("Sam", "Kadiyla", "Student", "sam.kadiyala@gmail.com"));
			employeeRepository.save(new Employee("San", "Kadiyala", "Student", "san.kadiyala@gmail.com"));
			employeeRepository.save(new Employee("Ram", "Kadiyala", "engineer", "ram.kadiyala@gmail.com"));

			employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

			
			orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
			orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

			orderRepository.findAll().forEach(order -> {
				log.info("Preloaded " + order);
			});
			
		};
	}
}
