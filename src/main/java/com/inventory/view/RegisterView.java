package com.inventory.view;

import com.inventory.dao.UsuarioDAO;
import com.inventory.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField nombreField;
    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JPasswordField confirmarContrasenaField;
    private JComboBox<String> rolComboBox;
    private JButton registerButton;
    private JButton backButton;
    private JLabel messageLabel;
    private UsuarioDAO usuarioDAO;

    public RegisterView() {
        usuarioDAO = new UsuarioDAO();
        
        setTitle("Sistema de Inventario - Registrarse");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Crear Cuenta");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel nombreLabel = new JLabel("Nombre:");
        panel.add(nombreLabel, gbc);

        gbc.gridx = 1;
        nombreField = new JTextField(20);
        panel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel usuarioLabel = new JLabel("Usuario:");
        panel.add(usuarioLabel, gbc);

        gbc.gridx = 1;
        usuarioField = new JTextField(20);
        panel.add(usuarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        panel.add(contrasenaLabel, gbc);

        gbc.gridx = 1;
        contrasenaField = new JPasswordField(20);
        panel.add(contrasenaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel confirmarLabel = new JLabel("Confirmar Contraseña:");
        panel.add(confirmarLabel, gbc);

        gbc.gridx = 1;
        confirmarContrasenaField = new JPasswordField(20);
        panel.add(confirmarContrasenaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel rolLabel = new JLabel("Rol:");
        panel.add(rolLabel, gbc);

        gbc.gridx = 1;
        rolComboBox = new JComboBox<>(new String[]{"empleado", "admin"});
        panel.add(rolComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel, gbc);

        gbc.gridy = 7;
        JPanel buttonPanel = new JPanel();
        
        registerButton = new JButton("Registrarse");
        registerButton.setPreferredSize(new Dimension(150, 35));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAction();
            }
        });
        
        backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(150, 35));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void registerAction() {
        String nombre = nombreField.getText().trim();
        String usuario = usuarioField.getText().trim();
        String contrasena = new String(contrasenaField.getPassword());
        String confirmarContrasena = new String(confirmarContrasenaField.getPassword());
        String rol = (String) rolComboBox.getSelectedItem();

        if (nombre.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            messageLabel.setText("Por favor complete todos los campos");
            messageLabel.setForeground(Color.RED);
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            messageLabel.setText("Las contraseñas no coinciden");
            messageLabel.setForeground(Color.RED);
            confirmarContrasenaField.setText("");
            return;
        }

        if (usuario.length() < 3) {
            messageLabel.setText("El usuario debe tener al menos 3 caracteres");
            messageLabel.setForeground(Color.RED);
            return;
        }

        if (contrasena.length() < 4) {
            messageLabel.setText("La contraseña debe tener al menos 4 caracteres");
            messageLabel.setForeground(Color.RED);
            return;
        }

        if (usuarioDAO.usuarioExiste(usuario)) {
            messageLabel.setText("El usuario ya existe");
            messageLabel.setForeground(Color.RED);
            usuarioField.setText("");
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, usuario, contrasena, rol);

        if (usuarioDAO.registrarUsuario(nuevoUsuario)) {
            messageLabel.setText("Registro exitoso! Redirigiendo...");
            messageLabel.setForeground(new Color(0, 150, 0));
            JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ahora puede iniciar sesión.", "Registro Completado", JOptionPane.INFORMATION_MESSAGE);
            goBackToLogin();
        } else {
            messageLabel.setText("Error al registrar. Intente de nuevo");
            messageLabel.setForeground(Color.RED);
        }
    }

    private void goBackToLogin() {
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
        this.dispose();
    }
}
