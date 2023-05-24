package org.iesalandalus.programacion.chat.modelo.dominio;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String password;

    public Usuario(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        /*if (userName.isBlank()) {
            throw new IllegalArgumentException("ERROR: El nombre del usuario no puede estar vació.");
        }*/
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        /*if (password.isBlank()) {
            throw new IllegalArgumentException("ERROR: La contraseña del usuario no puede estar vacia.");
        }*/
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(userName, usuario.userName);
    }

    public static Usuario obtenerUsuarioUsername(String userName) {
        return new Usuario(userName, "2222");
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public String toString() {
        return userName;
    }
}
