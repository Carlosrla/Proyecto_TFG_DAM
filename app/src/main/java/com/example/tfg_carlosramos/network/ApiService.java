package com.example.tfg_carlosramos.network;

import com.example.tfg_carlosramos.Model.Cliente;
import com.example.tfg_carlosramos.Model.Nota;
import com.example.tfg_carlosramos.Model.Propiedad;
import com.example.tfg_carlosramos.Model.Transaccion;
import com.example.tfg_carlosramos.Model.Cita;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Métodos existentes

    /**
     * Obtiene la lista de clientes con notas.
     * @return Call<List<Cliente>> con la lista de clientes.
     */
    @GET("clientesConNotas")
    Call<List<Cliente>> getClientesConNotas();

    /**
     * Obtiene la lista de clientes sin notas.
     * @return Call<List<Cliente>> con la lista de clientes.
     */
    @GET("clientesSinNotas")
    Call<List<Cliente>> getClientesSinNotas();

    /**
     * Obtiene la lista de todos los clientes.
     * @return Call<List<Cliente>> con la lista de clientes.
     */
    @GET("clientes")
    Call<List<Cliente>> getClientes();

    /**
     * Obtiene un cliente por su ID.
     * @param id ID del cliente.
     * @return Call<Cliente> con los detalles del cliente.
     */
    @GET("clientes/{id}")
    Call<Cliente> getClienteById(@Path("id") int id);

    /**
     * Agrega un nuevo cliente.
     * @param cliente Cliente a agregar.
     * @return Call<Void>
     */
    @POST("clientes")
    Call<Void> agregarCliente(@Body Cliente cliente);

    /**
     * Actualiza un cliente existente.
     * @param id ID del cliente a actualizar.
     * @param cliente Cliente actualizado.
     * @return Call<Void>
     */
    @PUT("clientes/{id}")
    Call<Void> actualizarCliente(@Path("id") int id, @Body Cliente cliente);

    /**
     * Elimina un cliente por su ID.
     * @param id ID del cliente a eliminar.
     * @return Call<Void>
     */
    @DELETE("clientes/{id}")
    Call<Void> eliminarCliente(@Path("id") int id);

    // Métodos para Propiedades

    /**
     * Obtiene la lista de todas las propiedades.
     * @return Call<List<Propiedad>> con la lista de propiedades.
     */
    @GET("propiedades")
    Call<List<Propiedad>> getPropiedades();

    /**
     * Agrega una nueva propiedad.
     * @param propiedad Propiedad a agregar.
     * @return Call<Void>
     */
    @POST("propiedades")
    Call<Void> agregarPropiedad(@Body Propiedad propiedad);

    /**
     * Actualiza una propiedad existente.
     * @param id ID de la propiedad a actualizar.
     * @param propiedad Propiedad actualizada.
     * @return Call<Void>
     */
    @PUT("propiedades/{id}")
    Call<Void> actualizarPropiedad(@Path("id") int id, @Body Propiedad propiedad);

    /**
     * Elimina una propiedad por su ID.
     * @param id ID de la propiedad a eliminar.
     * @return Call<Void>
     */
    @DELETE("propiedades/{id}")
    Call<Void> eliminarPropiedad(@Path("id") int id);

    /**
     * Obtiene la lista de propiedades disponibles.
     * @return Call<List<Propiedad>> con la lista de propiedades disponibles.
     */
    @GET("/propiedadesDisponibles")
    Call<List<Propiedad>> getPropiedadesDisponibles();

    /**
     * Obtiene la lista de propiedades por ubicación.
     * @param ubicacion Ubicación de las propiedades.
     * @return Call<List<Propiedad>> con la lista de propiedades.
     */
    @GET("/propiedadesPorUbicacion")
    Call<List<Propiedad>> getPropiedadesPorUbicacion(@Query("ubicacion") String ubicacion);

    // Métodos para Transacciones

    /**
     * Obtiene la lista de todas las transacciones.
     * @return Call<List<Transaccion>> con la lista de transacciones.
     */
    @GET("transacciones")
    Call<List<Transaccion>> getTransacciones();

    /**
     * Agrega una nueva transacción.
     * @param transaccion Transacción a agregar.
     * @return Call<Void>
     */
    @POST("transacciones")
    Call<Void> agregarTransaccion(@Body Transaccion transaccion);

    /**
     * Actualiza una transacción existente.
     * @param id ID de la transacción a actualizar.
     * @param transaccion Transacción actualizada.
     * @return Call<Void>
     */
    @PUT("transacciones/{id}")
    Call<Void> actualizarTransaccion(@Path("id") int id, @Body Transaccion transaccion);

    /**
     * Elimina una transacción por su ID.
     * @param id ID de la transacción a eliminar.
     * @return Call<Void>
     */
    @DELETE("transacciones/{id}")
    Call<Void> eliminarTransaccion(@Path("id") int id);

    // Métodos para Notas

    /**
     * Obtiene la lista de todas las notas.
     * @return Call<List<Nota>> con la lista de notas.
     */
    @GET("notas")
    Call<List<Nota>> getNotas();

    /**
     * Obtiene la lista de notas de un cliente por su ID.
     * @param id ID del cliente.
     * @return Call<List<Nota>> con la lista de notas del cliente.
     */
    @GET("clientes/{id}/notas")
    Call<List<Nota>> getNotasDelCliente(@Path("id") int id);

    /**
     * Agrega una nueva nota.
     * @param nota Nota a agregar.
     * @return Call<Void>
     */
    @POST("notas")
    Call<Void> agregarNota(@Body Nota nota);

    /**
     * Actualiza una nota existente.
     * @param id ID de la nota a actualizar.
     * @param nota Nota actualizada.
     * @return Call<Void>
     */
    @PUT("notas/{id}")
    Call<Void> actualizarNota(@Path("id") int id, @Body Nota nota);

    /**
     * Elimina una nota por su ID.
     * @param id ID de la nota a eliminar.
     * @return Call<Void>
     */
    @DELETE("notas/{id}")
    Call<Void> eliminarNota(@Path("id") int id);

    // Métodos para Citas

    /**
     * Obtiene la lista de todas las citas.
     * @return Call<List<Cita>> con la lista de citas.
     */
    @GET("citas")
    Call<List<Cita>> getCitas();

    /**
     * Obtiene la lista de citas de un cliente por su ID.
     * @param id ID del cliente.
     * @return Call<List<Cita>> con la lista de citas del cliente.
     */
    @GET("clientes/{id}/citas")
    Call<List<Cita>> getCitasDelCliente(@Path("id") int id);

    /**
     * Agrega una nueva cita.
     * @param cita Cita a agregar.
     * @return Call<Void>
     */
    @POST("citas")
    Call<Void> agregarCita(@Body Cita cita);

    /**
     * Actualiza una cita existente.
     * @param id ID de la cita a actualizar.
     * @param cita Cita actualizada.
     * @return Call<Void>
     */
    @PUT("citas/{id}")
    Call<Void> actualizarCita(@Path("id") int id, @Body Cita cita);

    /**
     * Elimina una cita por su ID.
     * @param id ID de la cita a eliminar.
     * @return Call<Void>
     */
    @DELETE("citas/{id}")
    Call<Void> eliminarCita(@Path("id") int id);
}
