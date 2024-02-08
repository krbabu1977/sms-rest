package payroll;

import java.util.List;

import lombok.Data;

@Data
public class EmployeeModelWrapper {

    private Embedded _embedded;
    
    @Data
    public static class Embedded {
        private List<EmployeeModel> employees;
    }
}