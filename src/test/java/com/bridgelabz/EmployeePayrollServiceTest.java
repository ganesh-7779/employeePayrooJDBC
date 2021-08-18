/********************************************************************
 *Purpose : Test Cases for Java Database connectivity Program using 7 steps
 *        1.Import java.sql*   2.Load and Register driver
 *        3. Create connection 4. Create Statement
 *        5. Execute Query     6. Process The Results
 *        7. close connection.
 * @Auther : Ganesh Gavhad
 * @since  : 15-08-2021
 * @version : 1.0
 **********************************************************************/

package com.bridgelabz;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;

    public class EmployeePayrollServiceTest {

        /**
         * Purpose : To test whether the number of records present in the database matches with the expected value
         */
        @Test
        public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
            EmployeePayrollService employeePayrollService = new EmployeePayrollService();
            List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
            Assert.assertEquals(3, employeePayrollData.size());
        }
}
