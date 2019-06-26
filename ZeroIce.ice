/**
 * (c) 2019 Proyecto Desarrollo e Integracion de Soluciones.
 */
#pragma once

/**
 * Modelo del Dominio del Problema
 */

["java:package:cl.ucn.disc.pdis.sceucn.ice", "cs:namespace:CL.UCN.DISC.PDIS.SCE.Server"]
// ["Ice.Package.model=cl.ucn.disc.pdis.sceucn.ice"]
module ZeroIce {

    /**
     * Model
     */
    module Model {

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
        ["cs:property"]
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
        ["cs:property"]
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
        ["cs:property"]
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
        ["cs:property"]
        class Registro {

            // Vehiculo
            Vehiculo vehiculo;

            // Fecha de ingreso
            string fecha;

            // Porteria
            Porteria porteria;
        }

        /**
         * Listado de Vehiculos en el sistema
         */
        ["java:type:java.util.ArrayList<Vehiculo>:java.util.List<Vehiculo>", "cs:generic:List"]
        sequence<Vehiculo> Vehiculos;

        /**
         * Listado de Personas en el sistema
         */
        ["java:type:java.util.ArrayList<Persona>:java.util.List<Persona>", "cs:generic:List"]
        sequence<Persona> Personas;

        /**
         * Listado de Registros en el sistema
         */
        ["java:type:java.util.ArrayList<Registro>:java.util.List<Registro>", "cs:generic:List"]
        sequence<Registro> Registros;

    };

    /**
     * Controllers
     */
    module Controller {

        /**
         * Operaciones del Sistema
         */
        interface IBackendMain {

            /**
             * Registra el ingreso de un vehiculo al campus.
             */
            void registrarIngreso(string placa, Model::Porteria porteria);

            /**
             * Registra el ingreso de un vehiculo al campus en caso de estar offline.
             */
            void registrarIngresoOffline(string placa, Model::Porteria porteria, string fecha);

            /**
             * Obtiene un listado con todos los vehiculos registrados en la base de datos.
             */
            Model::Vehiculos obtenerVehiculos();
    
        };

        /**
        * Operaciones de la pagina web.
        */
        interface IBackendWeb {

            /**
             * Obtiene un listado con todos los registros ingresados en la base de datos.
             */
            Model::Registros obtenerRegistros();

            /**
             * Obtiene un listado con todos los vehiculos registrados en la base de datos.
             */
            Model::Vehiculos obtenerVehiculos();

            /**
             * Dada una placa, obtiene el vehiculo correspondiente.
             */
            Model::Vehiculo obtenerVehiculo(string placa);

            /**
             * Obtiene un listado con todas las personas registradas en la base de datos.
             */
            Model::Personas obtenerPersonas();

            /**
             * Agrega un vehiculo a la base de datos.
             */
            void agregarVehiculo(string rutPersona, string placa, string marca, Model::Tipo tipo);
    
        };

    };

};