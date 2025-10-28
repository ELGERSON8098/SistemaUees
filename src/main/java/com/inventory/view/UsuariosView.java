package com.inventory.view;

import com.inventory.dao.UsuarioDAO;
import com.inventory.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UsuariosView extends JPanel {
    private UsuarioDAO usuarioDAO;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField nombreField;
    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JComboBox<String> rolComboBox;
    private JButton btnAgregar, btnActualizar, btnEliminar;
    private Usuario usuarioSeleccionado;

    public UsuariosView() {
        usuarioDAO = new UsuarioDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        
        cargarUsuarios();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Agregar/Actualizar Usuario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        nombreField = new JTextField(15);
        panel.add(nombreField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 3;
        usuarioField = new JTextField(15);
        panel.add(usuarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        contrasenaField = new JPasswordField(15);
        panel.add(contrasenaField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Rol:"), gbc);

        gbc.gridx = 3;
        rolComboBox = new JComboBox<>(new String[]{"admin", "empleado"});
        panel.add(rolComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarUsuario();
            }
        });
        panel.add(btnAgregar, gbc);

        gbc.gridx = 1;
        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarUsuario();
            }
        });
        panel.add(btnActualizar, gbc);

        gbc.gridx = 2;
        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
            }
        });
        panel.add(btnEliminar, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnas = {"ID", "Nombre", "Usuario", "Rol", "Fecha Registro"};
        modeloTabla = new ReadOnlyTableModel(columnas, 0);
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaUsuarios.getSelectedRow() != -1) {
                cargarUsuarioSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();
        for (Usuario u : usuarios) {
            Object[] fila = {
                u.getIdUsuario(),
                u.getNombre(),
                u.getUsuario(),
                u.getRol(),
                u.getFechaRegistro()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void cargarUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila != -1) {
            int idUsuario = (int) modeloTabla.getValueAt(fila, 0);
            usuarioSeleccionado = usuarioDAO.obtenerUsuarioPorId(idUsuario);
            
            if (usuarioSeleccionado != null) {
                nombreField.setText(usuarioSeleccionado.getNombre());
                usuarioField.setText(usuarioSeleccionado.getUsuario());
                contrasenaField.setText(usuarioSeleccionado.getContrasena());
                rolComboBox.setSelectedItem(usuarioSeleccionado.getRol());
            }
        }
    }

    private void agregarUsuario() {
        if (validarCampos()) {
            Usuario nuevoUsuario = new Usuario(
                nombreField.getText(),
                usuarioField.getText(),
                new String(contrasenaField.getPassword()),
                (String) rolComboBox.getSelectedItem()
            );

            if (usuarioDAO.registrarUsuario(nuevoUsuario)) {
                JOptionPane.showMessageDialog(this, 
                    "Usuario creado exitosamente.\n\n" +
                    "Usuario: " + usuarioField.getText() + "\n" +
                    "Rol: " + rolComboBox.getSelectedItem() + "\n\n" +
                    "El usuario ya puede ingresar al sistema.", 
                    "Usuario Creado", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validarCampos()) {
            usuarioSeleccionado.setNombre(nombreField.getText());
            usuarioSeleccionado.setUsuario(usuarioField.getText());
            usuarioSeleccionado.setContrasena(new String(contrasenaField.getPassword()));
            usuarioSeleccionado.setRol((String) rolComboBox.getSelectedItem());

            if (usuarioDAO.actualizarUsuario(usuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarUsuarios();
                usuarioSeleccionado = null;
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminarUsuario(usuarioSeleccionado.getIdUsuario())) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarUsuarios();
                usuarioSeleccionado = null;
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCampos() {
        if (nombreField.getText().isEmpty() || usuarioField.getText().isEmpty() || contrasenaField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        nombreField.setText("");
        usuarioField.setText("");
        contrasenaField.setText("");
        rolComboBox.setSelectedIndex(0);
    }

    class ReadOnlyTableModel extends DefaultTableModel {
        public ReadOnlyTableModel(Object[] columnas, int filas) {
            super(columnas, filas);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
