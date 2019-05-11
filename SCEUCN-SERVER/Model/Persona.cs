

namespace SCEUCN_SERVER.Model
{
    public enum Planta { Oficial, Fija, Temporal }

    public class Persona
    {
        
        public int Id { get; set; }
        
        public string Rut { get; set; }

        public string Nombres { get; set; }

        public string Apellidos { get; set; }
    }
}