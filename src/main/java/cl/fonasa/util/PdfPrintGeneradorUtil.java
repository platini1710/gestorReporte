package cl.fonasa.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import cl.fonasa.WebConfig;
import cl.fonasa.certificados.CertificadoCotizacionesDatos;
/**
 *
 * @author Agusto
 */
@Configuration
@PropertySource("classpath:application.properties")
public class PdfPrintGeneradorUtil {

    @Value("${ws.genera.codigo.certificadoWSDL}")
	private  String certificadoWSDL;

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final Logger log = LoggerFactory.getLogger(PdfPrintGeneradorUtil.class);
    public void obtenerDireccion(CertificadoCotizacionesDatos cotizacionesDatos,String rut) {
        String direccion = " ";
        String nombreUsuario= " ";
        
        try {

            RestTemplate restTemplate = new RestTemplate();
    		String result = restTemplate.getForObject("http://fosqaotdgen.fonasa.local:10010/FrontInt_OSB_ServiciosExternos/RS_ConsultarLicenciaMedicaV2?CodigoUsuario=portalweb&ClaveUsuario=portalweb201&RutBeneficiario=13748592-3&FecConsulta=03-2015",  String.class);
            System.out.println("result:::::"+result);
            Object obj = new JSONParser().parse(result);
            JSONObject jo = (JSONObject) obj;
     
	    Map InfoBeneficiario = ((Map)jo.get("InfoBeneficiario"));
	    nombreUsuario=(String)InfoBeneficiario.get("nombres") + " " + (String)InfoBeneficiario.get("apellidoPaterno")+ " " + (String)InfoBeneficiario.get("apellidoMaterno") ;
            Map acreditacion = ((Map) jo.get("acreditacion"));
            Map ubicabilidad = ((Map) acreditacion.get("ubicabilidad"));
            JSONArray ja = (JSONArray) ubicabilidad.get("direccion");
            HashMap<String, String> mapDirecciones = new HashMap<String, String>();
            Iterator itr2 = ja.iterator();
            Iterator itr1;;
            while (itr2.hasNext()) {
                itr1 = ((Map) itr2.next()).entrySet().iterator();
                while (itr1.hasNext()) {
                    Map.Entry pair = (Entry) itr1.next();
                    if ((pair.getKey()!=null) && (pair.getValue()!=null)  &&  !mapDirecciones.containsKey(pair.getKey()))  {
                        mapDirecciones.put(pair.getKey().toString(), pair.getValue().toString());
                    }
                }
            }
        
            obj = new JSONParser().parse(mapDirecciones.get("comuna"));
            JSONObject comunaJson = (JSONObject) obj;
            Map re = ((Map) comunaJson.get("region"));
            String calle = (String)mapDirecciones.get("calle");
            String poblacionVilla = (String)mapDirecciones.get("poblacionVilla");
            String block = (String)mapDirecciones.get("block");
            String departamento =(String) mapDirecciones.get("numeroDepartamento");
            String comuna = (String) comunaJson.get("nombre");
            String region = (String) re.get("nombre");

            if (calle != null) {
                direccion = direccion + "calle " + calle;
            }
            if (poblacionVilla != null) {
                direccion = direccion + " villa o población " + poblacionVilla;
            }
            if (block != null) {
                direccion = direccion + " block " + block;
            }
            if (departamento != null) {
                direccion = direccion + " N° departamento " + departamento;
            }
            if (comuna != null) {
                direccion = direccion + " comuna " + comuna;
            }
            if (region != null) {
                direccion = direccion + " región " + region;
            }

        } catch (ParseException e) {
            System.out.println("error parse");
          log.error(e.getMessage(), e);  
        } catch (Exception e) {
                   System.out.println("error Exception");
          log.error(e.getMessage(), e);

        }
        cotizacionesDatos.setNombreCotizante(nombreUsuario);
        cotizacionesDatos.setDireccion(direccion);

    }


    public Object[] obtenerDatosCotizaciones (String rut, String periodoDesde, String periodoHasta , String consultarLicenciaMedica)  {
           Object[] o = null;
        try {
            System.out.println("ebConfig.consultarLicenciaMedica " + consultarLicenciaMedica );
            System.out.println("certificadoWSDL ::: " + certificadoWSDL );
            
            URL url = new URL("http://fosqaotdgen.fonasa.local:10010/FrontInt_OSB_ServiciosExternos/RS_ConsultarLicenciaMedicaV2" + "?CodigoUsuario=portalweb&ClaveUsuario=portalweb201&RutBeneficiario=" + rut +"&FecConsulta=03-2015");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output, output2 = "";

            while ((output = br.readLine()) != null) {
                output2 = output2 + output;

            }

            Object obj = new JSONParser().parse(output2);
            JSONObject jo = (JSONObject) obj;
            conn.disconnect();

            Map acreditacion = ((Map) jo.get("VerLccTrabResult"));
            Map ja2 = (Map) acreditacion.get("ListaLmeTrab");
            JSONArray ja = (JSONArray) ja2.get("ListaLicencias");
            o = ja.toArray();
            

            for (int i = 0; i < o.length - 1; i++) {
                Map o1 = (Map) o[i];

                String fechaAutDesde = (String) o1.get("FecAutDesde");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = dateFormat.parse(fechaAutDesde);

                    for (int j = i + 1; j < o.length; j++) {
                        Map o2 = (Map) o[j];
                        String fechaAutDesde2 = (String) o2.get("FecAutDesde");

                        Date date2 = dateFormat.parse(fechaAutDesde2);

                        if (date.compareTo(date2) < 0) {
                            Object aux = o[i];
                            o[i] = o[j];
                            o[j] = aux;
                            fechaAutDesde = (String) ((Map) o[i]).get("FecAutDesde");
                            date = dateFormat.parse(fechaAutDesde);
                            
                        }
                    }

                } catch (java.text.ParseException e1) {
                    // TODO Auto-generated catch block
                    log.error(e1.getMessage(), e1);
                }

            }

        } catch (MalformedURLException e) {

            log.error(e.getMessage(), e);

        } catch (IOException e) {

            log.error(e.getMessage(), e);

        } catch (ParseException ex) {
            log.error(ex.getMessage(), ex);
        }
        return o;
    }
    
    
}
