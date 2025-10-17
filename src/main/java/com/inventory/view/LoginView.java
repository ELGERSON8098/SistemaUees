package com.inventory.view;

import com.inventory.dao.UsuarioDAO;
import com.inventory.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;
    private UsuarioDAO usuarioDAO;

    public LoginView() {
        usuarioDAO = new UsuarioDAO();
        
        setTitle("Sistema de Inventario - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Iniciar Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel usuarioLabel = new JLabel("Usuario:");
        panel.add(usuarioLabel, gbc);

        gbc.gridx = 1;
        usuarioField = new JTextField(20);
        panel.add(usuarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        panel.add(contrasenaLabel, gbc);

        gbc.gridx = 1;
        contrasenaField = new JPasswordField(20);
        panel.add(contrasenaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel, gbc);

        gbc.gridy = 4;
        JPanel buttonPanel = new JPanel();
        
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(150, 35));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });
        
        registerButton = new JButton("Registrarse");
        registerButton.setPreferredSize(new Dimension(150, 35));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterView();
            }
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void loginAction() {
        String usuario = usuarioField.getText().trim();
        String contrasena = new String(contrasenaField.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            messageLabel.setText("Por favor complete todos los campos");
            messageLabel.setForeground(Color.RED);
            return;
        }

        Usuario usuarioAutenticado = usuarioDAO.login(usuario, contrasena);

        if (usuarioAutenticado != null) {
            messageLabel.setText("Login exitoso!");
            messageLabel.setForeground(new Color(0, 150, 0));
            JOptionPane.showMessageDialog(this, "¡Bienvenido " + usuarioAutenticado.getNombre() + "!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
            DashboardView dashboardView = new DashboardView(usuarioAutenticado);
            dashboardView.setVisible(true);
            this.dispose();
        } else {
            messageLabel.setText("Usuario o contraseña incorrectos");
            messageLabel.setForeground(Color.RED);
            contrasenaField.setText("");
        }
    }

    private void openRegisterView() {
        RegisterView registerView = new RegisterView();
        registerView.setVisible(true);
        this.dispose();
    }

    private void limpiarCampos() {
        usuarioField.setText("");
        contrasenaField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            }
        });
    }
}
