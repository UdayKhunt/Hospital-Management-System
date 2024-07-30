package hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection , Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Enter Patient age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Patient gender: ");
        String gender = scanner.next();

        String query = "insert into patient(name , age , gender) values(?,?,?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1 , name);
            preparedStatement.setInt(2 , age);
            preparedStatement.setString(3 , gender);

            int rows_affected = preparedStatement.executeUpdate();
            if (rows_affected > 0){
                System.out.println("Insertion successful");
            }
            else System.out.println("Insertion failed");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void viewPatients(){
        String query = "select * from patient";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+----------------+-------------+-------+-----------+");
            System.out.println("| Patient Id     | Name        | Age   | Gender    |");
            System.out.println("+----------------+-------------+-------+-----------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-14s | %-11s | %-5s | %-9s |\n",id,name,age,gender);
                System.out.println("+----------------+-------------+-------+-----------+");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public boolean getPatientByID(int id){
        String query = "select * from patient where id = ? ";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1 , id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }

}
