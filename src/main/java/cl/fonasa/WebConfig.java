package cl.fonasa;

import java.nio.charset.Charset;
import java.util.Collections;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cl.fonasa.certificados.CertificadoDireccionesServlet;
import cl.fonasa.certificados.CertificadoLicenciasMedicasServlet;
import cl.fonasa.util.JsonMimeInterceptor;



@Configuration
public class WebConfig {
    @Value("${ws.genera.codigo.certificadoWSDL}")
	private  String certificadoWSDL;
    @Value("${ruta.rest.servicio.ConsultarLicenciaMedica}")
	private   String consultarLicenciaMedica;

   @Bean	
   public ServletRegistrationBean<HttpServlet> certificadoDireccionesServlet() {
	   ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
	   servRegBean.setServlet(new CertificadoDireccionesServlet());
	   servRegBean.addUrlMappings("/certificadoDirecciones/*");
	   servRegBean.setLoadOnStartup(1);
	   return servRegBean;
   }
   @Bean	
   public ServletRegistrationBean<HttpServlet> certificadoLicenciasMedicasServlet() {
	   System.out.println("ServletRegistrationBean ::" + certificadoWSDL);
	   System.out.println("consultarLicenciaMedica ::" + consultarLicenciaMedica);
	   ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
	   CertificadoLicenciasMedicasServlet  certificadoLicenciasMedicasServlet=new CertificadoLicenciasMedicasServlet();
	   certificadoLicenciasMedicasServlet.setCertificadoWSDL(certificadoWSDL);
	   certificadoLicenciasMedicasServlet.setConsultarLicenciaMedica(consultarLicenciaMedica);
	   servRegBean.setServlet(new CertificadoLicenciasMedicasServlet());
	   servRegBean.addUrlMappings("/certificadoLicenciasMedicas/*");
	   servRegBean.setLoadOnStartup(1);
	   return servRegBean;
   }
   
   @Bean
   public RestTemplate restTemplate() {
       RestTemplate restTemplate = new RestTemplate();

       // se debe incorporar este interceptor para que el proxy por POST funcione

       restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

       return restTemplate;
   }
} 