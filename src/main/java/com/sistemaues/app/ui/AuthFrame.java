package com.sistemaues.app.ui;

import com.sistemaues.app.model.User;
import com.sistemaues.app.service.UserService;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Optional;

public class AuthFrame extends JFrame {
    private final UserService userService;

    public AuthFrame(UserService userService) {
        super("Sistema de Inventario - Acceso");
        this.userService = userService;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(buildTabs(), BorderLayout.CENTER);
        setSize(420, 320);
        setLocationRelativeTo(null);
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Iniciar Sesión", buildLoginPanel());
        tabbedPane.addTab("Registrar", buildRegisterPanel());
        return tabbedPane;
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.anchor = GridBagConstraints.WEST;

        JTextField usuarioField = new JTextField(20);
        JPasswordField contrasenaField = new JPasswordField(20);
        JButton loginButton = new JButton("Ingresar");

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Usuario"), constraints);
        constraints.gridx = 1;
        panel.add(usuarioField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Contraseña"), constraints);
        constraints.gridx = 1;
        panel.add(contrasenaField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(loginButton, constraints);

        loginButton.addActionListener(event -> onLogin(usuarioField.getText(), new String(contrasenaField.getPassword())));
        return panel;
    }

    private JPanel buildRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.anchor = GridBagConstraints.WEST;

        JTextField nombreField = new JTextField(20);
        JTextField usuarioField = new JTextField(20);
        JPasswordField contrasenaField = new JPasswordField(20);
        JComboBox<String> rolBox = new JComboBox<>(new String[]{"empleado", "admin"});
        JButton registerButton = new JButton("Registrar");

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Nombre"), constraints);
        constraints.gridx = 1;
        panel.add(nombreField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Usuario"), constraints);
        constraints.gridx = 1;
        panel.add(usuarioField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Contraseña"), constraints);
        constraints.gridx = 1;
        panel.add(contrasenaField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(new JLabel("Rol"), constraints);
        constraints.gridx = 1;
        panel.add(rolBox, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(registerButton, constraints);

        registerButton.addActionListener(event -> onRegister(nombreField.getText(), usuarioField.getText(), new String(contrasenaField.getPassword()), (String) rolBox.getSelectedItem()));
        return panel;
    }

    private void onLogin(String usuario, String contrasena) {
        try {
            Optional<User> user = userService.authenticate(usuario, contrasena);
            if (user.isPresent()) {
                JOptionPane.showMessageDialog(this, "Bienvenido " + user.get().getNombre(), "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void onRegister(String nombre, String usuario, String contrasena, String rol) {
        try {
            userService.register(nombre, usuario, contrasena, rol);
            JOptionPane.showMessageDialog(this, "Registro exitoso. Ahora puede iniciar sesión.", "Registro", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, "Ocurrió un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void launch(UserService userService) {
        SwingUtilities.invokeLater(() -> {
            AuthFrame frame = new AuthFrame(userService);
            frame.setVisible(true);
        });
    }
}
