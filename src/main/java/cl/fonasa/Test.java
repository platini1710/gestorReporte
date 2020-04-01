package cl.fonasa;


import cl.fonasa.soa.gestioncertificado.GestionCertificadoRequest;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoRequest.BodyResquest;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoResponse;
import cl.fonasa.soa.protocolo.HeaderRequest;
import cl.fonasa.soa.proxy.gestioncertificado_ps.FaultMessage;
import cl.fonasa.soa.proxy.gestioncertificado_ps.GestionCertificadoBindingQSService;
import cl.fonasa.soa.proxy.gestioncertificado_ps.GestionCertificadoPortType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
public class Test {

    public static void main(String[] args) {
        try {
            URL wsdlLocation = new URL("http://bus.fonasa.cl:80/GestionCertificadoProject/ProxyServices/gestionCertificado_PS?wsdl");
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

            BodyResquest bd = new BodyResquest();
            bd.setRunCertificado("15099142");
            bd.setTipoCertificado("1");
            bd.setTramoCertificado("A");

            request.setHeaderRequest(header);
            request.setBodyResquest(bd);

            GestionCertificadoResponse response = port.gestionCertificado(request);

            System.out.println(response.getBodyResponse().getCodCertificado());
//            String pala = cifrarPalabra("15099142-0");
//            String lala = descrifrarPalabra(pala);
//            System.out.println(lala + " palabra " + pala);

        } catch (MalformedURLException | DatatypeConfigurationException | FaultMessage ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String cifrarPalabra(String mensaje) {
        char[] caracteres = mensaje.toCharArray();
        StringBuffer hexadecimal = new StringBuffer();
        for (int i = 0; i < caracteres.length; i++) {
            if (i > 0) {
                if ((i % 3) == 0) {
                    hexadecimal.append("-");
                }
            }
            hexadecimal.append(Integer.toHexString((int) caracteres[i]));
        }
        return hexadecimal.toString();
    }

    public static String descrifrarPalabra(String mensajeCifrado) {
        mensajeCifrado = mensajeCifrado.replace("-", "");
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < mensajeCifrado.length() - 1; i += 2) {
            String output = mensajeCifrado.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }
}