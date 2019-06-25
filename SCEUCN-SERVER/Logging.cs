using Microsoft.Extensions.Logging;

namespace CL.UCN.DISC.PDIS.SCE.Server
{
    public class Logging
    {
        // The Logger Factory
        private static ILoggerFactory TheLoggerFactory { get; } = LoggerFactory.Create(builder =>
        {
            // 
            builder.AddConsole(options =>
            {
                // options.IncludeScopes = true;
                // options.DisableColors = false;
                options.TimestampFormat = "[yyyyMMdd.HHmmss.fff] ";
            });

            // builder.AddFilter(level => level >= LogLevel.Debug);
            builder.SetMinimumLevel(LogLevel.Trace);
        });

        public static ILogger<T> CreateLogger<T>() => TheLoggerFactory.CreateLogger<T>();
    }

    public class LE
    {
        public const int Main = 1000;
        public const int Generate = 2000;
        public const int Ice = 3000;
        public const int Find = 4000;
        public const int Save = 4100;
        public const int Delete = 4200;
        public const int Converter = 5000;


    }

}
