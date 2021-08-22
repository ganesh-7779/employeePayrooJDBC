package com.bridgelabz;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EmployeePayrollService {
    private final EmployeePayrollDBService employeePayrollDBService;
    private List<EmployeePayrollData> employeePayrollList;

    public enum IOService {
        DB_IO
    }

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    /**
     * This method to get the list of employee payroll from the database
     */
    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws EmployeePayrollException {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    /**
     * This method to update the Employee Salary in the database
     */

    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if(result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if( employeePayrollData != null )
            employeePayrollData.salary = salary;
    }

    /**
     * This method to update the Employee Salary in the database
     */
    public void updateEmployeeSalaryUsingPreparedStatement(String name, double salary) throws EmployeePayrollException {
        int result = employeePayrollDBService.updateEmployeeDataPreparedStatement(name, salary);
        if(result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if( employeePayrollData != null )
            employeePayrollData.salary = salary;
    }

    /**
     * This method to check whether the EmployeePayrollData is in sync with the DB
     * Use to equals() to compare the values
     */

    public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    /**
     * This method to Check the EmployeePayrollData list for the name
     * If found, return the value else return null
     */

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * This method to Retrieve the data for a particular date range
     */
    public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) throws EmployeePayrollException {
        if( ioService.equals(IOService.DB_IO))
            return employeePayrollDBService.getEmployeeForDateRange(startDate, endDate);
        return null;
    }
    /*
     * UC6 This method to read the avg salary of employee  group by gender.
     */
    public Map<String, Double> readAvgSalaryByGender(IOService ioService) {
        if(ioService.equals(IOService.DB_IO))
            return employeePayrollDBService.getAverageSalaryByGender();
        return null;
    }
    /*
     * UC7 This method to add new employee in Employee payroll DB.
     */
    public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate start) throws SQLException {
        employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name,gender,salary,start));
    }
}