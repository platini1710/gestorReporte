
package cl.fonasa.soa.proxy.gestioncertificado_ps;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoRequest;
import cl.fonasa.soa.gestioncertificado.GestionCertificadoResponse;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "gestionCertificadoPortType", targetNamespace = "http://soa.fonasa.cl/proxy/gestionCertificado_PS")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    cl.fonasa.soa.gestioncertificado.ObjectFactory.class,
    cl.fonasa.soa.protocolo.ObjectFactory.class
})
public interface GestionCertificadoPortType {


    /**
     * 
     * @param request
     * @return
     *     returns cl.fonasa.soa.gestioncertificado.GestionCertificadoResponse
     * @throws FaultMessage
     */
    @WebMethod(action = "gestionCertificado")
    @WebResult(name = "gestionCertificadoResponse", targetNamespace = "http://soa.fonasa.cl/gestionCertificado", partName = "response")
    public GestionCertificadoResponse gestionCertificado(
        @WebParam(name = "gestionCertificadoRequest", targetNamespace = "http://soa.fonasa.cl/gestionCertificado", partName = "request")
        GestionCertificadoRequest request)
        throws FaultMessage
    ;

}