package cl.fonasa.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JsonMimeInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JsonMimeInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpServletRequest hsr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        // Seteo Content-Type Que viene desde peticion request real
        request.getHeaders().set(
                HttpHeaders.CONTENT_TYPE,
                hsr.getHeader(HttpHeaders.CONTENT_TYPE) == null
                        ? MediaType.APPLICATION_JSON_VALUE : hsr.getHeader(HttpHeaders.CONTENT_TYPE));

        log.info("EL QUE VIENE CONTENT TYPE :" + request.getHeaders().getContentType());
        //cabeceras.setContentType(request.getHeaders().getContentType());


        //Seteo Accept Que viene desde peticion request real
        request.getHeaders().set(HttpHeaders.ACCEPT,
                hsr.getHeader(HttpHeaders.ACCEPT) == null
                        ? MediaType.APPLICATION_JSON_VALUE : hsr.getHeader(HttpHeaders.ACCEPT));

        log.info(" ACCEPT :" + request.getHeaders().getAccept());

        log.info("VER HEADER SERVICES -->" + request.getHeaders());

        return execution.execute(request, body);
    }
}