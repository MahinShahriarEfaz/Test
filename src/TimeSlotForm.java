import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TimeSlotForm extends JFrame {
    private JComboBox comboselect;
    private JButton btnconfirm;
    private JPanel timeSlotPanel;
    private JLabel tmslot;
    private JTextArea textArea;


    public TimeSlotForm() {

        setTitle("TimeSlot");
        setContentPane(timeSlotPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        updateCombo();

        boolean hasRegisteredUsers = connectToDatabase();

        if (hasRegisteredUsers) {
            //show Login form
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if (user != null) {
                tmslot.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                dispose();
            }
        } else {
            //show Registration form
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if (user != null) {
                tmslot.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                dispose();
            }
        }

        btnconfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String slot = comboselect.getSelectedItem().toString();
                int n = slot.charAt(9);
                System.out.printf("bye"+"   "+n);;
                updateSeats(String.valueOf(n));


            }
        });
    }

    private void updateSeats(String n) {
        final String DB_URL = "jdbc:mysql://localhost/efaz";
        final String USERNAME = "root";
        final String PASSWORD = "efaz13@@@";
            if (n == "49") {
                String sql = "Update slots Set seats = seats - 1 where idslots=1";
                Connection conn = DriverManager.getConnection(DB_URL,USERNAME, PASSWORD);
                Statement statement = conn.createStatement();
                ResultSet resultSet =statement.executeQuery(sql);
                System.out.printf(String.valueOf(resultSet));

                statement.close();
                conn.close();

            } else {
                String sql = "Update slots " +
                        "Set seats = seats - 1 where idslots=2";
                Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                System.out.printf(String.valueOf(resultSet));

                statement.close();
                conn.close();
            }

        }





    private void updateCombo() {
        final String DB_URL = "jdbc:mysql://localhost/efaz";
        final String USERNAME = "root";
        final String PASSWORD = "efaz13@@@";

        String sql = "SELECT * from slots";
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            ResultSet resultSet =statement.executeQuery(sql);
            System.out.printf(String.valueOf(resultSet));
            while (resultSet.next()){
                comboselect.addItem(resultSet.getString(2));
            }
            statement.close();
            conn.close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    private boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost/efaz";
        final String USERNAME = "root";
        final String PASSWORD = "efaz13@@@";

        try{
            //First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS efaz");
            statement.close();
            conn.close();

            //Second, connect to the database and create the table "users" if cot created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "sid INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(100) NOT NULL,"
                    + "email VARCHAR(100) NOT NULL UNIQUE,"
                    + "password VARCHAR(100) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            //check if we have users in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM user");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegisteredUsers = true;
                }
            }

            statement.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }
    public static void main(String[] args) {
        TimeSlotForm myForm = new TimeSlotForm();
    }
}
