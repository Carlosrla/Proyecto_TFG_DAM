package com.example.tfg_carlosramos.fragments;

import com.example.tfg_carlosramos.Model.Cliente;

/**
 * Interfaz que define los métodos para modificar o eliminar un cliente.
 * Esta interfaz debe ser implementada por cualquier clase que desee
 * realizar acciones de modificación o eliminación de clientes.
 */
public interface ModificarClienteListener {

    /**
     * Método que se llama cuando se desea modificar un cliente.
     *
     * @param cliente El cliente que se desea modificar.
     */
    void onModificarCliente(Cliente cliente);

    /**
     * Método que se llama cuando se desea eliminar un cliente.
     *
     * @param cliente El cliente que se desea eliminar.
     */
    void onEliminarCliente(Cliente cliente);
}
