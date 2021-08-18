/********************************************************************
 *Purpose : Java Database connectivity using 7 steps
 *        1.Import java.sql*   2.Load and Register driver
 *        3. Create connection 4. Create Statement
 *        5. Execute Query     6. Process The Results
 *        7. close connection.
 * @Auther : Ganesh Gavhad
 * @since  : 15-08-2021
 * @version : 1.0
 **********************************************************************/
package com.bridgelabz;

import java.util.List;

public class EmployeePayrollService {

    EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();

    public enum IOService {
        DB_IO
    }

    private List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService() {
    }

    /**
     * Purpose : This method is To get the list of employee payroll from the database
     */
    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }
}
