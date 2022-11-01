import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog {
    private JLabel registrationLabel;
    private JTextField tfname;
    private JTextField tfsid;
    private JTextField tfemail;
    private JPasswordField pfpass;
    private JPasswordField pfconpass;
    private JButton btnregister;
    private JButton btncancel;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnregister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfname.getText();
        String sid = tfsid.getText();
        String email = tfemail.getText();
        String password = String.valueOf(pfpass.getPassword());
        String conPassword = String.valueOf(pfconpass.getPassword());

        if (name.isEmpty() || sid.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(conPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        user=addUserToDatabase(name, sid, email, password);
        if (user != null) {
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
    public User user;
    private User addUserToDatabase(String name, String sid, String email, String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost/efaz";
        final String USERNAME = "root";
        final String PASSWORD = "efaz13@@@";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.printf("hello");

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO user (name, sid, email, password) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, sid);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            //Insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.sid = sid;
                user.email = email;
                user.password = password;
            }
            stmt.close();
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args){
        RegistrationForm myForm = new RegistrationForm(null);
        User user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of: " + user.name);
        }
        else {
            System.out.println("Registration canceled");
        }
    }
}
