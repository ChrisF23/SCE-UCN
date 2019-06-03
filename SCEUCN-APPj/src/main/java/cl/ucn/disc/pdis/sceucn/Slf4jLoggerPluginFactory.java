/*
 * Copyright (c) 2019 Diego Urrutia-Astorga. http://durrutia.cl.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package cl.ucn.disc.pdis.sceucn;

import com.zeroc.Ice.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;

/**
 * Implementacion del logger.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
public final class Slf4jLoggerPluginFactory implements PluginFactory {
    /**
     * Called by the Ice run time to create a new plug-in.
     *
     * @param communicator The communicator that is in the process of being
     *                     initialized.
     * @param name         The name of the plug-in.
     * @param args         The arguments that are specified in the plug-ins
     *                     configuration.
     * @return The plug-in that was created by this method.
     **/
    @Override
    public Plugin create(Communicator communicator, String name, String[] args) {

        return new LoggerPlugin(communicator, new Slf4jLogger()) {
            /**
             * Called by the Ice run time during communicator initialization. The derived
             * class can override this method to perform any initialization that might be
             * required by a custom logger.
             **/
            @Override
            public void initialize() {
                super.initialize();
                log.debug("Initialize!");
            }

            /**
             * Called by the Ice run time when the communicator is destroyed. The derived
             * class can override this method to perform any finalization that might be
             * required by a custom logger.
             **/
            @Override
            public void destroy() {
                log.debug("Destroy!");
                super.destroy();
            }
        };
    }

    /**
     * Implementacion del server de articulos.
     *
     * @author Diego Urrutia-Astorga.
     */
    @Slf4j
    public static final class Slf4jLogger implements Logger {

        /**
         * Print a message. The message is printed literally, without any decorations
         * such as executable name or time stamp.
         *
         * @param message The message to log.
         **/
        @Override
        public void print(String message) {
            log.debug(message);
        }

        /**
         * Log a trace message.
         *
         * @param category The trace category.
         * @param message  The trace message to log.
         **/
        @Override
        public void trace(String category, String message) {
            log.debug(message);
            // log.trace(MarkerFactory.getMarker(category), message);
        }

        /**
         * Log a warning message.
         *
         * @param message The warning message to log.
         * @see #error
         **/
        @Override
        public void warning(String message) {
            log.warn(message);
        }

        /**
         * Log an error message.
         *
         * @param message The error message to log.
         * @see #warning
         **/
        @Override
        public void error(String message) {
            log.error(message);
        }

        /**
         * Returns this logger's prefix.
         *
         * @return The prefix.
         **/
        @Override
        public String getPrefix() {
            return null;
        }

        /**
         * Returns a clone of the logger with a new prefix.
         *
         * @param prefix The new prefix for the logger.
         * @return A logger instance.
         **/
        @Override
        public Logger cloneWithPrefix(String prefix) {
            return null;
        }
    }
}
