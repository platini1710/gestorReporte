package cl.fonasa.controller;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloWorldController {     
	
    @RequestMapping("/world")
    public String helloMsg() {

        RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://fosqaotdgen.fonasa.local:10010/FrontInt_OSB_ServiciosExternos/RS_ConsultarLicenciaMedicaV2?CodigoUsuario=portalweb&ClaveUsuario=portalweb201&RutBeneficiario=13748592-3&FecConsulta=03-2015",  String.class);
        System.out.println("result:::::"+result);

    	return result;
    }
} 