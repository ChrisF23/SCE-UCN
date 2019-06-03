/**
 * (c) 2019 Proyecto Desarrollo e Integracion de Soluciones.
 */
 #pragma once

/**
 * Modelo del Dominio del Problema
 */

//[["java:package:cl.ucn.disc.pdis.sceucn.ice"]]
["Ice.Package.model=cl.ucn.disc.pdis.sceucn.ice"]

module model {

    /**
     * Tipo de Logo/Contrato
     */
    enum Rol {
        Academico,
        Funcionario,
        Pregrado,
        Posgrado
    };

    /**
     * Logo: Autoadhesivo pegado en el parabrisas
     */
    ["clr:property"]
    class Logo {

        //  L1-1210, 19PS0182
        string identificador;

        // Academico
        Rol rol;

        // 2019
        string anio;
    };

    /**
     * Tipo de Contrato
     */
    enum Contrato {
        Oficial,
        Fijo,
        Temporal
    };

    /**
     * Persona en el campus con vehiculo
     */
    ["clr:property"]
    class Persona {
        
        // 12.123.132-3
        string rut;

        // Juan Andres
        string nombres;

        // Visalovic Terreas
        string apellidos;

        // jisalovic@ucn.cl
        string email;

        // +569 1234 5678
        string movil;

        // Departamento de Ingenieria de Sistemas y Computacion
        string unidad;

        // Pabellon Y1, Oficina 305
        string oficina;

        // +56 2 355 163
        string anexo;

        // Academico
        Rol rol;

        // Tipo de Contrato
        Contrato contrato;
    };

    /**
     * Tipo de Vehiculo
     */
    enum Tipo {
        Auto,
        Camioneta,
        Moto
    };

    /**
     * Listado de los logos
     */
    ["java:type:java.util.ArrayList<Logo>:java.util.List<Logo>", "cs:generic:List"]
    sequence<Logo> Logos;

    /**
     * Vehiculo asociado a una persona
     */
    ["clr:property"]
    class Vehiculo {

        // 2019
        string anio;

        // Suzuki
        string marca;

        // FBXS22
        string placa;

        // Juan Visalovic Terreas
        Persona persona;

        // Auto
        Tipo tipo;

        // Logos
        Logos logos;
    };

    /**
     * Lugar de ingreso
     */
    enum Porteria {
        Principal,
        Sur,
        Norte
    };

    /**
     * Registro de acceso
     */
    ["clr:property"]
    class Registro {

        // Vehiculo
        Vehiculo vehiculo;

        // Fecha de ingreso
        string fecha;

        // Porteria
        Porteria porteria;
    }

    /**
     * Listado de vehiculos en el sistema
     */
    ["java:type:java.util.ArrayList<Vehiculo>:java.util.List<Vehiculo>", "cs:generic:List"]
    sequence<Vehiculo> Vehiculos;

    /**
     * Operaciones del Sistema
     */
    interface Controlador {

        /**
         * Registra el ingreso de un vehiculo al campus.
         */
        void registrarIngreso(string placa, Porteria porteria);

        /**
         * Registra el ingreso de un vehiculo al campus en caso de estar offline.
         */
        void registrarIngresoOffline(string placa, Porteria porteria, string fecha);

        /**
         * Obtiene un listado con todos los vehiculos registrados en la base de datos.
         */
        Vehiculos obtenerVehiculos();
 
    };

};