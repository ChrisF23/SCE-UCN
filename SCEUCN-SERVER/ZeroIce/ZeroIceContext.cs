using CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model;
using Microsoft.EntityFrameworkCore;

/// <summary>
/// Fix the Model
/// </summary>
namespace CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Model {

    public partial class Persona {

        public int id { get; set; }

    }

    public partial class Vehiculo {

        public int id { get; set; }

    }

    public partial class Logo {

        public int id { get; set; }

    }

    public partial class Registro {

        public int id { get; set; }

    }

}

/// <summary>
/// The Context
/// </summary>
namespace CL.UCN.DISC.PDIS.SCE.Server.ZeroIce.Data {

    /// <summary>
    /// The DbContext
    /// </summary>
    public class ZeroIceContext : DbContext {

        public ZeroIceContext(DbContextOptions<ZeroIceContext> options) : base(options) { }

        public DbSet<Persona> Personas { get; set; }
        public DbSet<Vehiculo> Vehiculos { get; set; }
        public DbSet<Logo> Logos { get; set; }
        public DbSet<Registro> Registros { get; set; }

    }

}