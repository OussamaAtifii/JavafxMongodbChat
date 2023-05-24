package org.iesalandalus.programacion.chat.modelo.dominio;

import java.time.LocalTime;

public class Mensaje {
    private Usuario origen;
    private Usuario destino;
    private String mensaje;
    private LocalTime hora;

    public Mensaje(String mensaje, Usuario origen, Usuario destino, LocalTime hora) {
        setMensaje(mensaje);
        setOrigen(origen);
        setDestino(destino);
        setHora(hora);
    }

    public Usuario getOrigen() {
        return origen;
    }

    private void setOrigen(Usuario origen) {
        if (origen == null) {
            throw new NullPointerException("ERROR: El usuario de origen no puede ser nulo.");
        }
        this.origen = origen;
    }

    public Usuario getDestino() {
        return destino;
    }

    private void setDestino(Usuario destino) {
        if (destino == null) {
            throw new NullPointerException("ERROR: El usuario de destino no puede ser nulo.");
        }
        this.destino = destino;
    }

    public String getMensaje() {
        return mensaje;
    }

    private void setMensaje(String mensaje) {
        if (mensaje.isBlank()) {
            throw new IllegalArgumentException("ERROR: El mensaje no puede estar vació.");
        }
        this.mensaje = mensaje;
    }

    public LocalTime getHora() {
        return hora;
    }

    private void setHora(LocalTime hora) {
        this.hora = hora;
    }
}
