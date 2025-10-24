package com.inventory.app;

import com.inventory.dao.UsuarioDAO;
import com.inventory.view.LoginView;
import com.inventory.view.RegisterView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                
                if (!usuarioDAO.existeAdmin()) {
                    RegisterView registerView = new RegisterView(true);
                    registerView.setVisible(true);
                } else {
                    LoginView loginView = new LoginView();
                    loginView.setVisible(true);
                }
            }
        });
    }
}