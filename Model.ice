/**
 * (c) 2019 Proyecto Desarrollo e Integracion de Soluciones.
 */
 #pragma once

/**
 * Modelo del Dominio del Problema
 */
["java:package:cl.ucn.disc.pdis.sceucn.ice"]
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
    class Logo {

        //  L1-1210, 19PS0182
        ["java:getset", "protected"]
        string identificador;

        // Academico
        ["java:getset", "protected"]
        Rol rol;

        // 2019
        ["java:getset", "protected"]
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
    class Persona {
        
        // 12.123.132-3
        ["java:getset", "protected"]
        string rut;

        // Juan Andres
        ["java:getset", "protected"]
        string nombres;

        // Visalovic Terreas
        ["java:getset", "protected"]
        string apellidos;

        // jisalovic@ucn.cl
        ["java:getset", "protected"]
        string email;

        // +569 1234 5678
        ["java:getset", "protected"]
        string movil;

        // Departamento de Ingenieria de Sistemas y Computacion
        ["java:getset", "protected"]
        string unidad;

        // Pabellon Y1, Oficina 305
        ["java:getset", "protected"]
        string oficina;

        // +56 2 355 163
        ["java:getset", "protected"]
        string anexo;

        // Academico
        ["java:getset", "protected"]
        Rol rol;

        // Tipo de Contrato
        ["java:getset", "protected"]
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
    ["java:type:java.util.ArrayList<Logo>:java.util.List<Logo>"]
    sequence<Logo> Logos;

    /**
     * Vehiculo asociado a una persona
     */
    class Vehiculo {

        // 2019
        ["java:getset", "protected"]
        string anio;

        // Suzuki
        ["java:getset", "protected"]
        string marca;

        // FBXS22
        ["java:getset", "protected"]
        string placa;

        // Juan Visalovic Terreas
        ["java:getset", "protected"]
        Persona persona;

        // Auto
        ["java:getset", "protected"]
        Tipo tipo;

        // Logos
        ["java:getset", "protected"]
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
    class Registro {

        // Vehiculo
        ["java:getset", "protected"]
        Vehiculo vehiculo;

        // Fecha de ingreso
        ["java:getset", "protected"]
        string fecha;

        // Porteria
        ["java:getset", "protected"]
        Porteria porteria;
    }

    /**
     * Listado de vehiculos en el sistema
     */
    ["java:type:java.util.ArrayList<Vehiculo>:java.util.List<Vehiculo>"]
    sequence<Vehiculo> Vehiculos;

    /**
     * Operaciones del Sistema
     */
    interface Controlador {

        /**
         * Registra el ingreso de un vehiculo al campus.
         */
        void registrarIngreso(Registro registro);

        /**
         * Obtiene un listado con todos los vehiculos registrados en la base de datos.
         */
        idempotent Vehiculos obtenerVehiculos();
 
    };

};