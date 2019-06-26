using System;
using CL.UCN.DISC.PDIS.SCE.Server.Controller;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Controller;
using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Data;
using Ice;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

/// <summary>
/// The Context
/// </summary>
namespace CL.UCN.DISC.PDIS.SCE.Server {

    /// <summary>
    /// The Dependency Injection
    /// </summary>
    public class Startup {

        public static ServiceProvider BuildServiceProvider() {

            var serviceProvider = new ServiceCollection()
                .AddLogging()
                .AddSingleton<ILoggerFactory>(s => BuildLoggerFactory())
                .AddSingleton<ZeroIceContext>(s => BuildZeroIceContext())
                .AddSingleton<IMainController, MainController>()
                .AddSingleton<BackendMain>()
                .AddSingleton<BackendWeb>()
                .AddSingleton<Communicator>(s => BuildCommunicator())
                .AddSingleton<ZeroIceCommunicator>()
                .AddSingleton<DataGeneratorService>()
                .BuildServiceProvider();

            return serviceProvider;
        }

        /// <summary>
        /// The Logger Factory
        /// </summary>
        private static ILoggerFactory BuildLoggerFactory() {

            // Console.WriteLine("[*] Building ILoggerFactory ..");

            // The LoggerFactory
            return LoggerFactory.Create(builder => {

                // Console.WriteLine("[*] Configuring Console ..");

                // 
                builder.AddConsole(options => {
                    // options.IncludeScopes = true;
                    // options.DisableColors = false;
                    options.TimestampFormat = "[yyyyMMdd.HHmmss.fff] ";
                });

                // builder.AddFilter(level => level >= LogLevel.Debug);
                builder.SetMinimumLevel(LogLevel.Trace);
            });

        }

        /// <summary>
        /// The Database
        /// </summary>
        private static ZeroIceContext BuildZeroIceContext() {

            // Console.WriteLine("[*] Building The Database ..");

            // The Database
            var dbBuilder = new DbContextOptionsBuilder<ZeroIceContext>()
                .UseSqlite("Data Source=zeroice.db");

            var db = new ZeroIceContext(dbBuilder.Options);
            db.Database.EnsureDeleted();
            db.Database.EnsureCreated();

            return db;
        }

        /// <summary>
        /// The Communicator
        /// </summary>
        private static Communicator BuildCommunicator() {

            // Console.WriteLine("[*] Building The Communicator ..");

            // ZeroC properties
            Properties properties = Util.createProperties();
            // https://doc.zeroc.com/ice/latest/property-reference/ice-trace
            properties.setProperty("Ice.Trace.Admin.Properties", "1");
            properties.setProperty("Ice.Trace.Locator", "2");
            properties.setProperty("Ice.Trace.Network", "3");
            properties.setProperty("Ice.Trace.Protocol", "1");
            properties.setProperty("Ice.Trace.Slicing", "1");
            properties.setProperty("Ice.Trace.ThreadPool", "1");
            properties.setProperty("Ice.Compression.Level", "9");

            // The ZeroC framework!
            InitializationData initializationData = new InitializationData();
            initializationData.properties = properties;

            var communicator = Ice.Util.initialize(initializationData);

            return communicator;

        }

    }

}