package com.inventory.view;

import com.inventory.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardView extends JFrame {
    private Usuario usuarioActual;
    private JPanel panelContenido;
    private CardLayout cardLayout;

    public DashboardView(Usuario usuario) {
        this.usuarioActual = usuario;
        
        setTitle("Sistema de Inventario - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        JPanel panelMenu = crearPanelMenu();
        panelPrincipal.add(panelMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        
        panelContenido.add(crearPanelBienvenida(), "inicio");
        panelContenido.add(new ProductosView(), "productos");
        panelContenido.add(new CategoriasView(), "categorias");
        
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }

    private JPanel crearPanelMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50));
        panel.setPreferredSize(new Dimension(200, 600));

        JLabel usuarioLabel = new JLabel(usuarioActual.getNombre());
        usuarioLabel.setForeground(Color.WHITE);
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usuarioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(usuarioLabel);
        panel.add(Box.createVerticalStrut(20));

        JButton btnInicio = crearBotonMenu("Inicio");
        btnInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "inicio");
            }
        });
        panel.add(btnInicio);

        JButton btnProductos = crearBotonMenu("Productos");
        btnProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "productos");
            }
        });
        panel.add(btnProductos);

        JButton btnCategorias = crearBotonMenu("Categorías");
        btnCategorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, "categorias");
            }
        });
        panel.add(btnCategorias);

        panel.add(Box.createVerticalGlue());

        JButton btnLogout = crearBotonMenu("Cerrar Sesión");
        btnLogout.setBackground(new Color(200, 50, 50));
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        panel.add(btnLogout);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setMaximumSize(new Dimension(180, 50));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setBackground(new Color(70, 130, 180));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.PLAIN, 12));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setMargin(new Insets(10, 10, 10, 10));
        return boton;
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Bienvenido al Sistema de Inventario");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Usuario: " + usuarioActual.getNombre() + " | Rol: " + usuarioActual.getRol());
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        panel.add(subtitulo, gbc);

        JLabel instrucciones = new JLabel("<html><center>Selecciona una opción del menú para comenzar</center></html>");
        instrucciones.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 2;
        gbc.insets = new Insets(40, 20, 20, 20);
        panel.add(instrucciones, gbc);

        return panel;
    }

    private void logout() {
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
        this.dispose();
    }
}
