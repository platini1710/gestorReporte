package cl.fonasa.certificados;


import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrador
 */
public class CertificadoCotizacionesDatos{


        public String sinResultado = "";
        private String nombreCotizante = "";
        public String rutCotizanteNumero = "";
        public String rutCotizanteDV = "";
        public String rutCotizante = "";
        public String periodoDesde = "";
        public String periodoHasta = "";
        public String codVerificacion = "";
        public String pathReporte = "";
       private String direccion = "";
        public CertificadoCotizacionesDatos(String[] args) {

            rutCotizanteNumero = args[0].trim();
            rutCotizanteDV = args[1].trim();
            periodoDesde = args[2].trim();
            periodoHasta = args[3].trim();
            rutCotizante = rutCotizanteNumero + "-" + rutCotizanteDV;
        }
        
        public CertificadoCotizacionesDatos(HttpServletRequest request) {

            rutCotizanteNumero = request.getParameter("RUT");
            rutCotizanteDV = request.getParameter("RUT_DV");
            periodoDesde = request.getParameter("PERIODO_INICIO");
            periodoHasta = request.getParameter("PERIODO_FIN");
            rutCotizante = rutCotizanteNumero + "-" + rutCotizanteDV;
        }

 
      
    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
        
        
    /**
     * @return the nombreCotizante
     */
    public String getNombreCotizante() {
        return nombreCotizante;
    }

    /**
     * @param nombreCotizante the nombreCotizante to set
     */
    public void setNombreCotizante(String nombreCotizante) {
        this.nombreCotizante = nombreCotizante;
    }
    }
