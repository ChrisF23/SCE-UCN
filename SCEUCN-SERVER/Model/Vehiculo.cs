
namespace SCEUCN_SERVER.Model
{
    public enum TipoVehiculo { Auto, Camioneta, Moto }
    public class Vehiculo
    {
        public string Id { get; set; }
        public string Patente { get; set; }

        public Persona Persona { get; set; }
                
        public string Marca { get; set; }
        
        public TipoVehiculo Tipo { get; set; }
    }
}