package com.inventory.view;

import com.inventory.dao.CategoriaDAO;
import com.inventory.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CategoriasView extends JPanel {
    private CategoriaDAO categoriaDAO;
    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;
    private JTextField nombreField;
    private JTextArea descripcionArea;
    private JButton btnAgregar, btnActualizar, btnEliminar;
    private Categoria categoriaSeleccionada;

    public CategoriasView() {
        categoriaDAO = new CategoriaDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        
        cargarCategorias();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Agregar/Actualizar Categoría"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        nombreField = new JTextField(20);
        panel.add(nombreField, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        descripcionArea = new JTextArea(3, 20);
        panel.add(new JScrollPane(descripcionArea), gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 2;
        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarCategoria();
            }
        });
        panel.add(btnAgregar, gbc);

        gbc.gridx = 2;
        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCategoria();
            }
        });
        panel.add(btnActualizar, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnas = {"ID", "Nombre", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCategorias.getSelectedRow() != -1) {
                cargarCategoriaSeleccionada();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaCategorias);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnEliminar = new JButton("Eliminar Seleccionada");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCategoria();
            }
        });
        panelBotones.add(btnEliminar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarCategorias() {
        modeloTabla.setRowCount(0);
        List<Categoria> categorias = categoriaDAO.obtenerTodasCategorias();
        for (Categoria c : categorias) {
            Object[] fila = {
                c.getIdCategoria(),
                c.getNombre(),
                c.getDescripcion()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void cargarCategoriaSeleccionada() {
        int fila = tablaCategorias.getSelectedRow();
        if (fila != -1) {
            int idCategoria = (int) modeloTabla.getValueAt(fila, 0);
            categoriaSeleccionada = categoriaDAO.obtenerCategoriaPorId(idCategoria);
            
            if (categoriaSeleccionada != null) {
                nombreField.setText(categoriaSeleccionada.getNombre());
                descripcionArea.setText(categoriaSeleccionada.getDescripcion());
            }
        }
    }

    private void agregarCategoria() {
        if (validarCampos()) {
            Categoria nuevaCategoria = new Categoria(
                nombreField.getText(),
                descripcionArea.getText()
            );

            if (categoriaDAO.agregarCategoria(nuevaCategoria)) {
                JOptionPane.showMessageDialog(this, "Categoría agregada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarCategorias();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar categoría", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarCategoria() {
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría para actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validarCampos()) {
            categoriaSeleccionada.setNombre(nombreField.getText());
            categoriaSeleccionada.setDescripcion(descripcionArea.getText());

            if (categoriaDAO.actualizarCategoria(categoriaSeleccionada)) {
                JOptionPane.showMessageDialog(this, "Categoría actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarCategorias();
                categoriaSeleccionada = null;
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar categoría", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarCategoria() {
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta categoría?", "Confirmación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (categoriaDAO.eliminarCategoria(categoriaSeleccionada.getIdCategoria())) {
                JOptionPane.showMessageDialog(this, "Categoría eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarCategorias();
                categoriaSeleccionada = null;
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar categoría", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCampos() {
        if (nombreField.getText().isEmpty() || descripcionArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        nombreField.setText("");
        descripcionArea.setText("");
    }
}
