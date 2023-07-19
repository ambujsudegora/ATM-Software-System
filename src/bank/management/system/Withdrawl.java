package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.ResultSet;

public class Withdrawl extends JFrame implements ActionListener {

    JTextField amount;
    JButton withdraw, back;
    String pinnumber;

    Withdrawl(String pinnumber) {
        this.pinnumber = pinnumber;
        setLayout(null);
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 900, 900);
        add(image);

        JLabel text = new JLabel("Enter the amount you want to Withdraw");
        text.setBounds(170, 300, 400, 16);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("System", Font.BOLD, 16));
        image.add(text);

        amount = new JTextField();
        amount.setFont(new Font("Raleway", Font.BOLD, 22));
        amount.setBounds(170, 350, 320, 25);
        add(amount);

        withdraw = new JButton("Withdraw");
        withdraw.setBounds(350, 485, 150, 30);
        withdraw.addActionListener(this);
        image.add(withdraw);

        back = new JButton("Back");
        back.setBounds(350, 520, 150, 30);
        back.addActionListener(this);

        image.add(back);

        setSize(900, 900);
        setLocation(300, 0);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == withdraw) {
            String number = amount.getText();
            Date date = new Date();
            if (number.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter the amount you want to Withdraw");
            } else if (Double.parseDouble(number) < 0) {
                JOptionPane.showMessageDialog(null, "Withdrawal amount cannot be negative");
            }
            else {
                try {
    Conn conn = new Conn();
    ResultSet rs = conn.s.executeQuery("SELECT SUM(CASE WHEN type='Deposit' THEN amount ELSE -amount END) as balance FROM bank WHERE pin='" + pinnumber + "'");
    rs.next();
    double balance = rs.getDouble("balance");
    var withdrawalAmount = Double.parseDouble(number);
    if (withdrawalAmount <= 0) {
        JOptionPane.showMessageDialog(null, "Invalid amount entered. Please enter a positive value.");
        return;
    } else if (withdrawalAmount > balance) {
        JOptionPane.showMessageDialog(null, "Insufficient balance");
        return;
    } else if (withdrawalAmount > 50000) {
        JOptionPane.showMessageDialog(null, "Withdrawal amount exceeds the limit of 50,000. Please enter a smaller amount.");
        return;
    }
    String query = "INSERT INTO bank (pin, date, type, amount) VALUES ('" + pinnumber + "', '" + date + "','Withdrawal','" + withdrawalAmount + "')";
    conn.s.executeUpdate(query);
    JOptionPane.showMessageDialog(null, "Rs :" + withdrawalAmount + " Withdrawn Successfully");
    setVisible(false);
    new Transactions(pinnumber).setVisible(true);
} catch (Exception e) {
    System.out.println(e);
}

            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Transactions(pinnumber).setVisible(true);
        }

    }

    public static void main(String[] args) {

        new Withdrawl("");
    }

}
