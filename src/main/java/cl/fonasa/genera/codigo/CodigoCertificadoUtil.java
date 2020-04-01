/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.fonasa.genera.codigo;



import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import cl.fonasa.WebConfig;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoRequest;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoResponse;
import cl.fonasa.soa.protocolo.HeaderRequest;
import cl.fonasa.soa.proxy.gestioncertificado_ps.FaultMessage;
import cl.fonasa.soa.proxy.gestioncertificado_ps.GestionCertificadoBindingQSService;
import cl.fonasa.soa.proxy.gestioncertificado_ps.GestionCertificadoPortType;

/**
 *
 * @author Administrador
 */
@Configuration
public class CodigoCertificadoUtil implements Serializable {

    @Value("${ws.genera.codigo.certificadoWSDL}")
    private  String certificadoWSDL;
    @Value("${ruta.rest.servicio.ConsultarLicenciaMedica}")
	public  String consultarLicenciaMedica;

    public  String generaCodigoCertificado(String rut, String tramo, String codigo) {
        String valor = "";


System.out.println("entro ::" + certificadoWSDL);
        try {
            URL wsdlLocation = new URL(certificadoWSDL);
            GestionCertificadoBindingQSService service = new GestionCertificadoBindingQSService(wsdlLocation);
            GestionCertificadoPortType port = service.getGestionCertificadoBindingQSPort();
            HeaderRequest header = new HeaderRequest();
            header.setUserID("11");
            header.setRolID("1");
            header.setSucursalID("1");
            GregorianCalendar gfechaActual = new GregorianCalendar();
            Calendar fechaActual = GregorianCalendar.getInstance();
            gfechaActual.setTime(fechaActual.getTime());
            header.setFechaHora(DatatypeFactory.newInstance().newXMLGregorianCalendar(gfechaActual));

            GestionCertificadoRequest request = new GestionCertificadoRequest();

            GestionCertificadoRequest.BodyResquest bd = new GestionCertificadoRequest.BodyResquest();
            bd.setRunCertificado(rut);
            bd.setTipoCertificado(codigo);
            bd.setTramoCertificado(tramo);

            request.setHeaderRequest(header);
            request.setBodyResquest(bd);

            GestionCertificadoResponse response = port.gestionCertificado(request);
            //valor = response.getBodyResponse().getIdCertificado().toString();
            System.out.println("response" + response.getBodyResponse());
            valor = response.getBodyResponse().getCodCertificado();

        } catch (MalformedURLException | DatatypeConfigurationException | FaultMessage ex) {
        	ex.printStackTrace();
            Logger.getLogger(CodigoCertificadoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valor;

    }
}
