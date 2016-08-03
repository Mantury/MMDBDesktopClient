package controller;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import log.debug;
import objects.*;

/**
 * Created by Christoph on 02.08.16.
 */

public class DBM {

    private static Connection conn;

    private final static String USER="j.dietz";
    private final static String PASSWORD ="mmdb";
    private final static String DATABASE ="jdbc:mysql://132.199.139.24:3306/mmdb16_juliandietz";

    public static void openDBConnection() {
        try {
            conn = (Connection) DriverManager.getConnection(DATABASE, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        debug.printout("Connected to database");
    }

    private static void closeDBConnection() throws SQLException{
        conn.close();
        debug.printout("Disconnected from database via debug");
    }


    public static boolean getLogin (String name, char[] passwordchar) throws SQLException{
        openDBConnection();

        PreparedStatement pstmt = null;
        boolean check=false;

        try {
            String password=String.valueOf(passwordchar);
            pstmt = (PreparedStatement) conn.prepareStatement("SELECT * FROM   employee  WHERE emp_sign = ? AND password = ?");
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.executeQuery();
            ResultSet rs = pstmt.getResultSet();

            debug.printout(rs.first());

            check=rs.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        finally {
            closeDBConnection();

        }

        return check;
    }

    public static boolean DriverOrManager (String name) throws SQLException {
        openDBConnection();
        String query = "SELECT * FROM driver INNER JOIN employee ON driver.emp_id=employee.emp_id WHERE emp_sign = ?";
        PreparedStatement p = (PreparedStatement) conn.prepareStatement(query);
        p.setString(1,name);
        p.executeQuery();
        ResultSet rs = p.getResultSet();

        if(rs.first()) {
            closeDBConnection(); return true;} else {
            closeDBConnection();return false;}

    }

    public static Driver getDriverData (String name) throws SQLException {
        Driver driver=null;
        openDBConnection();
        String query = "SELECT * FROM employee INNER JOIN driver ON employee.emp_id=driver.emp_id WHERE emp_sign=?";
        PreparedStatement p = (PreparedStatement) conn.prepareStatement(query);
        p.setString(1,name);
        p.executeQuery();
        ResultSet rs = p.getResultSet();

        if(rs.first()) {
            driver= new Driver(rs);
            closeDBConnection();
        } else {
            closeDBConnection();}
        return driver;
    }


    public static Location getLocation (int adress_id) throws SQLException {
        openDBConnection();
        String query = "SELECT avenue, street FROM address WHERE address_id = ?";
        PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
        ps.setInt(1,adress_id);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        rs.next();
        int Avenue = rs.getInt(1);
        int Street = rs.getInt(2);
        int locId = adress_id;
        closeDBConnection();
        return new Location(locId,Avenue,Street);
    }

    public static Vehicle getVehicle(int vehicle_id) throws SQLException{
        openDBConnection();
        String query = "SELECT space, model FROM vehicle WHERE vehicle_id = ?";
        PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
        ps.setInt(1,vehicle_id);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        rs.next();
        int space = rs.getInt(1);
        String type = rs.getString(2);
        closeDBConnection();
        return new Vehicle(vehicle_id, type, space);

    }




}

