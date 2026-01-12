package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
private static final String url="jdbc:mysql://localhost:3306/hospital";
private static final String username="root";
private static final String password="bhawna@2004";
public static void main(String[]args) {
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
	}catch(ClassNotFoundException e) {
		e.printStackTrace();
	}
	Scanner sc= new Scanner(System.in);
	try {
		Connection connection =DriverManager.getConnection(url, username, password);
		Patient pt =new Patient(connection,sc);
		Doctor dc = new Doctor(connection);
		while(true) {
		System.out.println("HOSPITAL MANAGEMENT SYSTEM");
		System.out.println("1. Add patient");
		System.out.println("2. view patient");
		System.out.println("3. view Doctor");
		System.out.println("4. book appointment");
		System.out.println("5. Exixt");
		System.out.println("Enter your choice");
		int choice = sc.nextInt();
		switch(choice) {
		case 1:
			//add patient
			pt.addPatient();
			break;
		case 2:
			//view patient
			pt.viewPaitent();
			break;
		case 3:
			//view doctor
			dc.viewDoctor();
			break;
		case 4:
			//book appointment
			bookAppointment(pt,dc,connection,sc);
			break;
		case 5:
			return;
			default:
				System.out.println("enter valid choice");
				break;
		}
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
}
public static void bookAppointment( Patient p,Doctor d,Connection connection,Scanner scanner) {
	System.out.println("Enter patient id");
	int patientId = scanner.nextInt();
System.out.println("Enter Doctor Id");
	int doctorID = scanner.nextInt();
	System.out.println("Enter Appointment Date(YYYY-MM-DD)");
	String appointmentDate= scanner.next();
	if(p.getPatientById(patientId)&& d.getDoctorById(doctorID)) {
		if(checkDoctorAvailability(doctorID,appointmentDate,connection)) {
			String appointmentQuery ="INSERT INTO APPOINTMENT(PATIENT_ID,DOCTOR_ID,APPOINTMENT_DATE)VALUES(?,?,?)";
			try {
				PreparedStatement ps =connection.prepareStatement(appointmentQuery);
			ps.setInt( 1,patientId);
				ps.setInt(2, doctorID);
				ps.setString(3, appointmentDate);
				int affectedRow = ps.executeUpdate();
				if(affectedRow>0) {
					System.out.println("appointment booked");
				}else {
					System.out.println("appointment failed");
				}
			
		}catch(SQLException e) {
				e.printStackTrace();

			}
		}
		else {
			System.out.println("Doctor not available on this date!!");
		}
		}
	else {
		System.out.println("Either doctor or patient does't exist!!!");
	}
}
public static boolean checkDoctorAvailability( int doctorID, String appointmentDate,Connection connection) {
	String query="SELECT COUNT(*) FROM APPOINTMENT WHERE DOCTOR_ID=? AND APPOINTMENT_DATE=?";
	try {
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setInt(1, doctorID);
		ps.setString(2, appointmentDate);
		ResultSet rs =ps.executeQuery();
		if(rs.next()) {
			int count =rs.getInt(1);
			if(count==0) {
				return true;
			}else {
				return false;
			}
			
		
	}
}catch(SQLException e) {
	e.printStackTrace();

}
	return false;
}
}