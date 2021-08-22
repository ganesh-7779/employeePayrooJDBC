package com.bridgelabz;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {
    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement employeePayrollDataStatement;

    private EmployeePayrollDBService() {
    }

    /**
     * This method For creating a singleton object
     */
    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    /**
     * This method to read employee payroll from database using JDBC.
     */
    public List<EmployeePayrollData> readData() throws EmployeePayrollException {
        String sql = "SELECT * FROM employee_payroll2";
        return getEmployeePayrollDataUsingDB(sql);
    }

    /**
     * This method to Update the salary in the DB using Statement Interface
     */
    public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    /**
     * This method to Update the salary in the DB using PreparedStatement Interface
     */
    public int updateEmployeeDataPreparedStatement(String name, double salary) throws EmployeePayrollException {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    /**
     * This method to  Create connection with the database
     */
    private Connection getConnection() throws SQLException {

        String jdbcURL = "jdbc:mysql://localhost:3307/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Ganesh@7779";
        Connection connection;
        System.out.println("Connecting to database: " + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful! " + connection);
        return connection;
    }

    /**
     * This method to  Update the salary in the DB using Statement Interface
     */
    private int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
        String sql = String.format("UPDATE employee_payroll2 SET salary = %.2f WHERE name = '%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the updateEmployeeDataUsingStatement() for detailed information!");
        }
    }

    /**
     * This method to  Update the salary in the DB using PreparedStatement Interface
     */
    private int updateEmployeeDataUsingPreparedStatement(String name, double salary) throws EmployeePayrollException {
        String sql = "UPDATE employee_payroll2 SET salary = ? WHERE name = ?";
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, salary);
            statement.setString(2, name);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the updateEmployeeDataUsingPreparedStatement() for detailed information!");
        }
    }

    /**
     * This method to Get the list of EmployeePayrollData using the assigned name.
     */
    public List<EmployeePayrollData> getEmployeePayrollData(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollData(name) for detailed information!");
        }
        return employeePayrollList;
    }

    /**
     * This method to assign the value of the attributes in a list and return it
     */
    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollData(resultSet) for detailed information!");
        }
        return employeePayrollList;
    }

    /**
     * This method to get the details of a particular employee from the DB using PreparedStatement Interface
     */
    private void preparedStatementForEmployeeData() throws EmployeePayrollException {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll2 WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the preparedStatementForEmployeeData() for detailed information!");
        }
    }

    /**
     * This method to create connection to execute query and read the value from the database
     * Assign the value in a list variable
     */
    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollDataUsingDB() for detailed information!");
        }
        return employeePayrollList;
    }

    /**
     * This method to Read the data for a certain date range from the database
     */
    public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) throws EmployeePayrollException {
        String sql = String.format("SELECT * FROM employee_payroll2 WHERE start BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return getEmployeePayrollDataUsingDB(sql);
    }

    public Map<String, Double> getAverageSalaryByGender() {
        String sql = "SELECT gender, AVG(salary) as avg_salary FROM employee_payroll2 GROUP BY gender;";
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try {
            Connection connection = this.getConnection();
            {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    String gender = resultSet.getString("gender");
                    Double salary = resultSet.getDouble(("avg_salary"));
                    genderToAverageSalaryMap.put(gender, salary);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }

    /*
     * UC 7 This method is for add new employee to employee payroll DB.
     */
    public EmployeePayrollData addEmployeeToPayrollUC7(String name, String gender, double salary, LocalDate start) {
        int id = -1;
        EmployeePayrollData employeePayrollData = null;
        String sql = String.format("INSERT INTO employee_payroll2 (name,gender,salary,start) VALUES " +
                "                 ('%S','%S','%S','%S')", name, gender, salary, Date.valueOf(start));
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) id = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(id, name, salary, start);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollData;
    }
//UC8
    public EmployeePayrollData addEmployeeToPayroll(String name, String gender, double salary, LocalDate start) throws SQLException {
        int id = -1;
        Connection connection = null;
        EmployeePayrollData employeePayrollData = null;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO employee_payroll2 (name,gender,salary,start) VALUES ('%S','%S','%S','%S')", name, gender, salary, Date.valueOf(start));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
        try(Statement statement = connection.createStatement()){
            double deduction = salary * 0.2;
            double taxablePay = salary-deduction;
            double tax = taxablePay*0.1;
            double netPay = salary - tax;
            String sql = String.format("INSERT INTO payroll_detail (employee_id,basic_pay, deduction,taxable_pay,tax,net_pay) VALUES" +
                                      " (%S,%S,%S,%S,%S,%S)",id,salary,deduction,taxablePay,tax,netPay);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {
                employeePayrollData= new EmployeePayrollData(id,name,salary,start);
            }
        } catch (SQLException e) {
            e.printStackTrace();
           try {
               connection.rollback();
               return employeePayrollData;
           } catch (SQLException ex){
               ex.printStackTrace();
           }
        }
        try{
            connection.commit();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return employeePayrollData;
    }
}