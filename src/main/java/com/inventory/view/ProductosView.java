package com.inventory.view;

import com.inventory.dao.*;
import com.inventory.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class ProductosView extends JPanel {
    private JTabbedPane tabbedPane;

    public ProductosView() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Productos", new ProductosPanel());
        tabbedPane.addTab("Proveedores", new ProveedoresPanel());
        tabbedPane.addTab("Entradas", new EntradasPanel());
        tabbedPane.addTab("Salidas", new SalidasPanel());
        tabbedPane.addTab("Inventario", new InventarioPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    class ProductosPanel extends JPanel {
        private ProductoDAO productoDAO;
        private CategoriaDAO categoriaDAO;
        private JTable tablaProductos;
        private DefaultTableModel modeloTabla;
        private JTextField nombreField;
        private JTextArea descripcionArea;
        private JComboBox<Categoria> categoriaComboBox;
        private JTextField precioField;
        private JButton btnAgregar, btnActualizar, btnEliminar;
        private Producto productoSeleccionado;

        public ProductosPanel() {
            productoDAO = new ProductoDAO();
            categoriaDAO = new CategoriaDAO();
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(crearPanelFormulario(), BorderLayout.NORTH);
            add(crearPanelTabla(), BorderLayout.CENTER);
            
            cargarProductos();
        }

        private JPanel crearPanelFormulario() {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createTitledBorder("Agregar/Actualizar Producto"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Nombre:"), gbc);

            gbc.gridx = 1;
            nombreField = new JTextField(15);
            panel.add(nombreField, gbc);

            gbc.gridx = 2;
            panel.add(new JLabel("Categoría:"), gbc);

            gbc.gridx = 3;
            categoriaComboBox = new JComboBox<>();
            cargarCategorias();
            panel.add(categoriaComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Descripción:"), gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 3;
            descripcionArea = new JTextArea(3, 15);
            panel.add(new JScrollPane(descripcionArea), gbc);

            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Precio:"), gbc);

            gbc.gridx = 1;
            precioField = new JTextField(15);
            panel.add(precioField, gbc);

            gbc.gridx = 2;
            gbc.gridy = 2;
            btnAgregar = new JButton("Agregar");
            btnAgregar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    agregarProducto();
                }
            });
            panel.add(btnAgregar, gbc);

            gbc.gridx = 3;
            btnActualizar = new JButton("Actualizar");
            btnActualizar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actualizarProducto();
                }
            });
            panel.add(btnActualizar, gbc);

            return panel;
        }

        private JPanel crearPanelTabla() {
            JPanel panel = new JPanel(new BorderLayout());
            
            String[] columnas = {"ID", "Nombre", "Descripción", "Categoría", "Precio"};
            modeloTabla = new DefaultTableModel(columnas, 0);
            tablaProductos = new JTable(modeloTabla);
            tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaProductos.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && tablaProductos.getSelectedRow() != -1) {
                    cargarProductoSeleccionado();
                }
            });

            JScrollPane scrollPane = new JScrollPane(tablaProductos);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel panelBotones = new JPanel();
            btnEliminar = new JButton("Eliminar Seleccionado");
            btnEliminar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eliminarProducto();
                }
            });
            panelBotones.add(btnEliminar);

            panel.add(panelBotones, BorderLayout.SOUTH);

            return panel;
        }

        private void cargarCategorias() {
            categoriaComboBox.removeAllItems();
            List<Categoria> categorias = categoriaDAO.obtenerTodasCategorias();
            for (Categoria cat : categorias) {
                categoriaComboBox.addItem(cat);
            }
        }

        private void cargarProductos() {
            modeloTabla.setRowCount(0);
            List<Producto> productos = productoDAO.obtenerTodosProductos();
            for (Producto p : productos) {
                Object[] fila = {
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getNombreCategoria(),
                    p.getPrecio()
                };
                modeloTabla.addRow(fila);
            }
        }

        private void cargarProductoSeleccionado() {
            int fila = tablaProductos.getSelectedRow();
            if (fila != -1) {
                int idProducto = (int) modeloTabla.getValueAt(fila, 0);
                productoSeleccionado = productoDAO.obtenerProductoPorId(idProducto);
                
                if (productoSeleccionado != null) {
                    nombreField.setText(productoSeleccionado.getNombre());
                    descripcionArea.setText(productoSeleccionado.getDescripcion());
                    precioField.setText(productoSeleccionado.getPrecio().toString());
                    
                    for (int i = 0; i < categoriaComboBox.getItemCount(); i++) {
                        if (categoriaComboBox.getItemAt(i).getIdCategoria() == productoSeleccionado.getIdCategoria()) {
                            categoriaComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        }

        private void agregarProducto() {
            if (validarCampos()) {
                Categoria categoriaSeleccionada = (Categoria) categoriaComboBox.getSelectedItem();
                Producto nuevoProducto = new Producto(
                    nombreField.getText(),
                    descripcionArea.getText(),
                    categoriaSeleccionada.getIdCategoria(),
                    new BigDecimal(precioField.getText())
                );

                if (productoDAO.agregarProducto(nuevoProducto)) {
                    JOptionPane.showMessageDialog(this, "Producto agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarProductos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void actualizarProducto() {
            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (validarCampos()) {
                Categoria categoriaSeleccionada = (Categoria) categoriaComboBox.getSelectedItem();
                productoSeleccionado.setNombre(nombreField.getText());
                productoSeleccionado.setDescripcion(descripcionArea.getText());
                productoSeleccionado.setIdCategoria(categoriaSeleccionada.getIdCategoria());
                productoSeleccionado.setPrecio(new BigDecimal(precioField.getText()));

                if (productoDAO.actualizarProducto(productoSeleccionado)) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarProductos();
                    productoSeleccionado = null;
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void eliminarProducto() {
            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este producto?", "Confirmación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (productoDAO.eliminarProducto(productoSeleccionado.getIdProducto())) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarProductos();
                    productoSeleccionado = null;
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private boolean validarCampos() {
            if (nombreField.getText().isEmpty() || descripcionArea.getText().isEmpty() || precioField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                new BigDecimal(precioField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número válido", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        private void limpiarFormulario() {
            nombreField.setText("");
            descripcionArea.setText("");
            precioField.setText("");
            if (categoriaComboBox.getItemCount() > 0) {
                categoriaComboBox.setSelectedIndex(0);
            }
        }
    }

    class ProveedoresPanel extends JPanel {
        private ProveedorDAO proveedorDAO;
        private JTable tablaProveedores;
        private DefaultTableModel modeloTabla;
        private JTextField nombreField, telefonoField, direccionField;
        private JButton btnAgregar, btnActualizar, btnEliminar;
        private Proveedor proveedorSeleccionado;

        public ProveedoresPanel() {
            proveedorDAO = new ProveedorDAO();
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(crearPanelFormulario(), BorderLayout.NORTH);
            add(crearPanelTabla(), BorderLayout.CENTER);
            
            cargarProveedores();
        }

        private JPanel crearPanelFormulario() {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createTitledBorder("Agregar/Actualizar Proveedor"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Nombre:"), gbc);

            gbc.gridx = 1;
            nombreField = new JTextField(15);
            panel.add(nombreField, gbc);

            gbc.gridx = 2;
            panel.add(new JLabel("Teléfono:"), gbc);

            gbc.gridx = 3;
            telefonoField = new JTextField(15);
            panel.add(telefonoField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Dirección:"), gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 3;
            direccionField = new JTextField(15);
            panel.add(direccionField, gbc);

            gbc.gridwidth = 1;
            gbc.gridx = 1;
            gbc.gridy = 2;
            btnAgregar = new JButton("Agregar");
            btnAgregar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    agregarProveedor();
                }
            });
            panel.add(btnAgregar, gbc);

            gbc.gridx = 2;
            btnActualizar = new JButton("Actualizar");
            btnActualizar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actualizarProveedor();
                }
            });
            panel.add(btnActualizar, gbc);

            return panel;
        }

        private JPanel crearPanelTabla() {
            JPanel panel = new JPanel(new BorderLayout());
            
            String[] columnas = {"ID", "Nombre", "Teléfono", "Dirección"};
            modeloTabla = new DefaultTableModel(columnas, 0);
            tablaProveedores = new JTable(modeloTabla);
            tablaProveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaProveedores.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && tablaProveedores.getSelectedRow() != -1) {
                    cargarProveedorSeleccionado();
                }
            });

            JScrollPane scrollPane = new JScrollPane(tablaProveedores);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel panelBotones = new JPanel();
            btnEliminar = new JButton("Eliminar Seleccionado");
            btnEliminar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eliminarProveedor();
                }
            });
            panelBotones.add(btnEliminar);

            panel.add(panelBotones, BorderLayout.SOUTH);

            return panel;
        }

        private void cargarProveedores() {
            modeloTabla.setRowCount(0);
            List<Proveedor> proveedores = proveedorDAO.obtenerTodosProveedores();
            for (Proveedor p : proveedores) {
                Object[] fila = {
                    p.getIdProveedor(),
                    p.getNombre(),
                    p.getTelefono(),
                    p.getDireccion()
                };
                modeloTabla.addRow(fila);
            }
        }

        private void cargarProveedorSeleccionado() {
            int fila = tablaProveedores.getSelectedRow();
            if (fila != -1) {
                int idProveedor = (int) modeloTabla.getValueAt(fila, 0);
                proveedorSeleccionado = proveedorDAO.obtenerProveedorPorId(idProveedor);
                
                if (proveedorSeleccionado != null) {
                    nombreField.setText(proveedorSeleccionado.getNombre());
                    telefonoField.setText(proveedorSeleccionado.getTelefono());
                    direccionField.setText(proveedorSeleccionado.getDireccion());
                }
            }
        }

        private void agregarProveedor() {
            if (validarCampos()) {
                Proveedor nuevoProveedor = new Proveedor(
                    nombreField.getText(),
                    telefonoField.getText(),
                    direccionField.getText()
                );

                if (proveedorDAO.agregarProveedor(nuevoProveedor)) {
                    JOptionPane.showMessageDialog(this, "Proveedor agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarProveedores();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar proveedor", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void actualizarProveedor() {
            if (proveedorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor para actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (validarCampos()) {
                proveedorSeleccionado.setNombre(nombreField.getText());
                proveedorSeleccionado.setTelefono(telefonoField.getText());
                proveedorSeleccionado.setDireccion(direccionField.getText());

                if (proveedorDAO.actualizarProveedor(proveedorSeleccionado)) {
                    JOptionPane.showMessageDialog(this, "Proveedor actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarProveedores();
                    proveedorSeleccionado = null;
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar proveedor", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void eliminarProveedor() {
            if (proveedorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este proveedor?", "Confirmación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (proveedorDAO.eliminarProveedor(proveedorSeleccionado.getIdProveedor())) {
                    JOptionPane.showMessageDialog(this, "Proveedor eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarProveedores();
                    proveedorSeleccionado = null;
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar proveedor", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private boolean validarCampos() {
            if (nombreField.getText().isEmpty() || telefonoField.getText().isEmpty() || direccionField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        private void limpiarFormulario() {
            nombreField.setText("");
            telefonoField.setText("");
            direccionField.setText("");
        }
    }

    class EntradasPanel extends JPanel {
        private EntradaDAO entradaDAO;
        private ProductoDAO productoDAO;
        private ProveedorDAO proveedorDAO;
        private InventarioDAO inventarioDAO;
        private JTable tablaEntradas;
        private DefaultTableModel modeloTabla;
        private JComboBox<Producto> productoComboBox;
        private JComboBox<Proveedor> proveedorComboBox;
        private JTextField cantidadField, costoField;
        private JButton btnAgregar, btnEliminar;

        public EntradasPanel() {
            entradaDAO = new EntradaDAO();
            productoDAO = new ProductoDAO();
            proveedorDAO = new ProveedorDAO();
            inventarioDAO = new InventarioDAO();
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(crearPanelFormulario(), BorderLayout.NORTH);
            add(crearPanelTabla(), BorderLayout.CENTER);
            
            cargarEntradas();
        }

        private JPanel crearPanelFormulario() {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createTitledBorder("Registrar Entrada de Producto"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Producto:"), gbc);

            gbc.gridx = 1;
            productoComboBox = new JComboBox<>();
            cargarProductos();
            panel.add(productoComboBox, gbc);

            gbc.gridx = 2;
            panel.add(new JLabel("Proveedor:"), gbc);

            gbc.gridx = 3;
            proveedorComboBox = new JComboBox<>();
            cargarProveedores();
            panel.add(proveedorComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Cantidad:"), gbc);

            gbc.gridx = 1;
            cantidadField = new JTextField(15);
            panel.add(cantidadField, gbc);

            gbc.gridx = 2;
            panel.add(new JLabel("Costo Total:"), gbc);

            gbc.gridx = 3;
            costoField = new JTextField(15);
            panel.add(costoField, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            btnAgregar = new JButton("Registrar Entrada");
            btnAgregar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registrarEntrada();
                }
            });
            panel.add(btnAgregar, gbc);

            return panel;
        }

        private JPanel crearPanelTabla() {
            JPanel panel = new JPanel(new BorderLayout());
            
            String[] columnas = {"ID", "Producto", "Proveedor", "Fecha", "Cantidad", "Costo"};
            modeloTabla = new DefaultTableModel(columnas, 0);
            tablaEntradas = new JTable(modeloTabla);
            tablaEntradas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(tablaEntradas);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel panelBotones = new JPanel();
            btnEliminar = new JButton("Eliminar Seleccionada");
            btnEliminar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eliminarEntrada();
                }
            });
            panelBotones.add(btnEliminar);

            panel.add(panelBotones, BorderLayout.SOUTH);

            return panel;
        }

        private void cargarProductos() {
            productoComboBox.removeAllItems();
            List<Producto> productos = productoDAO.obtenerTodosProductos();
            for (Producto p : productos) {
                productoComboBox.addItem(p);
            }
        }

        private void cargarProveedores() {
            proveedorComboBox.removeAllItems();
            List<Proveedor> proveedores = proveedorDAO.obtenerTodosProveedores();
            for (Proveedor p : proveedores) {
                proveedorComboBox.addItem(p);
            }
        }

        private void cargarEntradas() {
            modeloTabla.setRowCount(0);
            List<Entrada> entradas = entradaDAO.obtenerTodasEntradas();
            for (Entrada e : entradas) {
                Object[] fila = {
                    e.getIdEntrada(),
                    e.getNombreProducto(),
                    e.getNombreProveedor(),
                    e.getFecha(),
                    e.getCantidad(),
                    e.getCosto()
                };
                modeloTabla.addRow(fila);
            }
        }

        private void registrarEntrada() {
            if (validarCampos()) {
                Producto productSeleccionado = (Producto) productoComboBox.getSelectedItem();
                Proveedor proveedorSeleccionado = (Proveedor) proveedorComboBox.getSelectedItem();
                
                Entrada nuevaEntrada = new Entrada(
                    productSeleccionado.getIdProducto(),
                    proveedorSeleccionado.getIdProveedor(),
                    new Date(System.currentTimeMillis()),
                    Integer.parseInt(cantidadField.getText()),
                    new BigDecimal(costoField.getText())
                );

                if (entradaDAO.agregarEntrada(nuevaEntrada)) {
                    actualizarInventario(productSeleccionado.getIdProducto(), Integer.parseInt(cantidadField.getText()));
                    JOptionPane.showMessageDialog(this, "Entrada registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarEntradas();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar entrada", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void actualizarInventario(int idProducto, int cantidad) {
            Inventario inventario = inventarioDAO.obtenerInventarioPorProducto(idProducto);
            if (inventario != null) {
                inventarioDAO.actualizarStock(idProducto, inventario.getStockActual() + cantidad);
            } else {
                inventarioDAO.agregarInventario(new Inventario(idProducto, cantidad));
            }
        }

        private void eliminarEntrada() {
            int filaSeleccionada = tablaEntradas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una entrada para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro?", "Confirmación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (entradaDAO.eliminarEntrada(id)) {
                    JOptionPane.showMessageDialog(this, "Entrada eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarEntradas();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar entrada", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private boolean validarCampos() {
            if (cantidadField.getText().isEmpty() || costoField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                Integer.parseInt(cantidadField.getText());
                new BigDecimal(costoField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        private void limpiarFormulario() {
            cantidadField.setText("");
            costoField.setText("");
        }
    }

    class SalidasPanel extends JPanel {
        private SalidaDAO salidaDAO;
        private ProductoDAO productoDAO;
        private InventarioDAO inventarioDAO;
        private JTable tablaSalidas;
        private DefaultTableModel modeloTabla;
        private JComboBox<Producto> productoComboBox;
        private JTextField cantidadField, destinoField;
        private JButton btnAgregar, btnEliminar;

        public SalidasPanel() {
            salidaDAO = new SalidaDAO();
            productoDAO = new ProductoDAO();
            inventarioDAO = new InventarioDAO();
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(crearPanelFormulario(), BorderLayout.NORTH);
            add(crearPanelTabla(), BorderLayout.CENTER);
            
            cargarSalidas();
        }

        private JPanel crearPanelFormulario() {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createTitledBorder("Registrar Salida de Producto"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Producto:"), gbc);

            gbc.gridx = 1;
            productoComboBox = new JComboBox<>();
            cargarProductos();
            panel.add(productoComboBox, gbc);

            gbc.gridx = 2;
            panel.add(new JLabel("Cantidad:"), gbc);

            gbc.gridx = 3;
            cantidadField = new JTextField(15);
            panel.add(cantidadField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Destino:"), gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 3;
            destinoField = new JTextField(15);
            panel.add(destinoField, gbc);

            gbc.gridwidth = 1;
            gbc.gridx = 1;
            gbc.gridy = 2;
            btnAgregar = new JButton("Registrar Salida");
            btnAgregar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registrarSalida();
                }
            });
            panel.add(btnAgregar, gbc);

            return panel;
        }

        private JPanel crearPanelTabla() {
            JPanel panel = new JPanel(new BorderLayout());
            
            String[] columnas = {"ID", "Producto", "Fecha", "Cantidad", "Destino"};
            modeloTabla = new DefaultTableModel(columnas, 0);
            tablaSalidas = new JTable(modeloTabla);
            tablaSalidas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(tablaSalidas);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel panelBotones = new JPanel();
            btnEliminar = new JButton("Eliminar Seleccionada");
            btnEliminar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eliminarSalida();
                }
            });
            panelBotones.add(btnEliminar);

            panel.add(panelBotones, BorderLayout.SOUTH);

            return panel;
        }

        private void cargarProductos() {
            productoComboBox.removeAllItems();
            List<Producto> productos = productoDAO.obtenerTodosProductos();
            for (Producto p : productos) {
                productoComboBox.addItem(p);
            }
        }

        private void cargarSalidas() {
            modeloTabla.setRowCount(0);
            List<Salida> salidas = salidaDAO.obtenerTodasSalidas();
            for (Salida s : salidas) {
                Object[] fila = {
                    s.getIdSalida(),
                    s.getNombreProducto(),
                    s.getFecha(),
                    s.getCantidad(),
                    s.getDestino()
                };
                modeloTabla.addRow(fila);
            }
        }

        private void registrarSalida() {
            if (validarCampos()) {
                Producto productSeleccionado = (Producto) productoComboBox.getSelectedItem();
                int cantidad = Integer.parseInt(cantidadField.getText());

                Inventario inventario = inventarioDAO.obtenerInventarioPorProducto(productSeleccionado.getIdProducto());
                if (inventario != null && inventario.getStockActual() >= cantidad) {
                    Salida nuevaSalida = new Salida(
                        productSeleccionado.getIdProducto(),
                        new Date(System.currentTimeMillis()),
                        cantidad,
                        destinoField.getText()
                    );

                    if (salidaDAO.agregarSalida(nuevaSalida)) {
                        inventarioDAO.actualizarStock(productSeleccionado.getIdProducto(), inventario.getStockActual() - cantidad);
                        JOptionPane.showMessageDialog(this, "Salida registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        limpiarFormulario();
                        cargarSalidas();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al registrar salida", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Stock insuficiente para esta salida", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        private void eliminarSalida() {
            int filaSeleccionada = tablaSalidas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una salida para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro?", "Confirmación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (salidaDAO.eliminarSalida(id)) {
                    JOptionPane.showMessageDialog(this, "Salida eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarSalidas();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar salida", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private boolean validarCampos() {
            if (cantidadField.getText().isEmpty() || destinoField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                Integer.parseInt(cantidadField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        private void limpiarFormulario() {
            cantidadField.setText("");
            destinoField.setText("");
        }
    }

    class InventarioPanel extends JPanel {
        private InventarioDAO inventarioDAO;
        private JTable tablaInventario;
        private DefaultTableModel modeloTabla;

        public InventarioPanel() {
            inventarioDAO = new InventarioDAO();
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(crearPanelTabla(), BorderLayout.CENTER);
            
            cargarInventario();
        }

        private JPanel crearPanelTabla() {
            JPanel panel = new JPanel(new BorderLayout());
            
            String[] columnas = {"ID", "Producto", "Stock Actual"};
            modeloTabla = new DefaultTableModel(columnas, 0);
            tablaInventario = new JTable(modeloTabla);

            JScrollPane scrollPane = new JScrollPane(tablaInventario);
            panel.add(scrollPane, BorderLayout.CENTER);

            JButton btnRefrescar = new JButton("Refrescar");
            btnRefrescar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cargarInventario();
                }
            });
            
            JPanel panelBotones = new JPanel();
            panelBotones.add(btnRefrescar);
            panel.add(panelBotones, BorderLayout.SOUTH);

            return panel;
        }

        private void cargarInventario() {
            modeloTabla.setRowCount(0);
            List<Inventario> inventarios = inventarioDAO.obtenerTodoInventario();
            for (Inventario i : inventarios) {
                Object[] fila = {
                    i.getIdInventario(),
                    i.getNombreProducto(),
                    i.getStockActual()
                };
                modeloTabla.addRow(fila);
            }
        }
    }
}
