package hospital;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "uday";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection , scanner);
            Doctor doctor = new Doctor(connection);
            System.out.println("Hospital Management System");
            while(true){
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");

                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(connection , scanner , doctor , patient);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Thank You. Visit Again!!");
                        System.out.println();
                        return;

                    default:
                        System.out.println("Enter valid choice");
                        System.out.println();
                        break;
                }


            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    static void bookAppointment(Connection connection , Scanner in , Doctor doctor , Patient patient){
        System.out.print("Enter Patient ID: ");
        int patientId = in.nextInt();
        System.out.print("Enter Doctor ID: ");
        int doctorId = in.nextInt();
        System.out.print("Enter Appointment Date: ");
        String appointmentDate = in.next();

        if (patient.getPatientByID(patientId) && doctor.getDoctorByID(doctorId)){
            if (checkDoctorAvailability(doctorId , appointmentDate, connection)){
                String appointmentQuery = "insert into appointment(patient_id,doctor_id,appointment_date) values(?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1 , patientId);
                    preparedStatement.setInt(2 , doctorId);
                    preparedStatement.setString(3 , appointmentDate);
                    int rows_affected = preparedStatement.executeUpdate();
                    if (rows_affected > 0) System.out.println("Appointment booked");
                    else System.out.println("Failed to book Appointment");

                } catch(SQLException e){
                    System.out.println(e.getMessage());
                }
            }
            else{
                System.out.println("Doctor not available on this date");
            }
        }
        else{
            System.out.println("Either patient or doctor does not exist");
        }
    }

    static boolean checkDoctorAvailability(int doctorId , String appointmentDate, Connection connection){
        try{
            String query = "select count(*) from appointment where doctor_id = ? and appointment_date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1 , doctorId);
            preparedStatement.setString(2 , appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                return (count == 0);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }
}
