package cl.fonasa.certificados;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cl.fonasa.util.PdfPrintGeneradorUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author Agusto
 */

@WebServlet(urlPatterns = "/certificadoLicenciasMedicas/*", loadOnStartup = 1)
public class CertificadoLicenciasMedicasServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private  String certificadoWSDL;

	public  String consultarLicenciaMedica;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DocumentException {
        String rutCotizanteNumero = request.getParameter("RUT");
        String DV = request.getParameter("RUT_DV");
        String rut = rutCotizanteNumero + "-" + DV;
        String periodoDesde = request.getParameter("PERIODO_DESDE");
        String periodoHasta = request.getParameter("PERIODO_HASTA");
        Date parsedDate;
        String strDate = " ";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");;
        CertificadoCotizacionesDatos cotizacionesDatos = new CertificadoCotizacionesDatos(request);
        PdfPrintGeneradorUtil pdfPrintGeneradorUtil = new PdfPrintGeneradorUtil();
                
        pdfPrintGeneradorUtil.obtenerDireccion(cotizacionesDatos, rut);
        
        Calendar fecha = Calendar.getInstance();
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        response.setContentType("application/pdf");
        ServletOutputStream os = response.getOutputStream();
        Calendar c1 = GregorianCalendar.getInstance();
        Locale locale = Locale.getDefault();
        String fechaActual = dia + " de " + fecha.getDisplayName(Calendar.MONTH, Calendar.LONG, locale) + " del " + año + "                     ";
        Image image1 = Image.getInstance("imagen/LOG-FON.jpg");
        Document document = new Document(PageSize.A4, 36, 36, 160, 76);
        Font font1 = FontFactory.getFont("SansSerif", 8);
        PdfWriter writer = PdfWriter.getInstance(document, os);
        Font f2 = new Font();
        f2.setFamily(FontFamily.COURIER.name());
        font1.setStyle(Font.BOLD);
        f2.setSize(8);
        Font font3 = FontFactory.getFont("SansSerif", 5);
        Font fontNoRecord = FontFactory.getFont("SansSerif", 12);
        Font fontTituloGrilla = FontFactory.getFont("SansSerif", 6);
        fontTituloGrilla.setStyle(Font.BOLD);
        // add header and footer
        HeadFootCertificadoLicMed event = new HeadFootCertificadoLicMed(cotizacionesDatos.getNombreCotizante(), rut, periodoDesde ,periodoHasta);
        writer.setPageEvent(event);
        // write to document
        document.open();
        font1.setStyle(font1.NORMAL);
        int numColumn = 11;
        PdfPTable table = new PdfPTable(numColumn);
        PdfPCell titulos = new PdfPCell();
        float[] columnWidths2 = new float[]{4f, 8f, 6f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f};
        table.setWidths(columnWidths2);
        table.setWidthPercentage(100);
        titulos.setPaddingBottom(2);
        titulos.setPaddingLeft(0);
        titulos.setPaddingTop(0);

        titulos.addElement(new Phrase("Rut Empleador ", fontTituloGrilla));//11
        titulos.setBorder(Rectangle.NO_BORDER);
        titulos.setHorizontalAlignment(Element.ALIGN_LEFT);
        titulos.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(titulos);

        titulos = new PdfPCell();//10
        titulos.setBorder(Rectangle.BOTTOM);
        titulos.addElement(new Phrase("             Empleador", fontTituloGrilla));
        titulos.setPadding(0);
        titulos.setHorizontalAlignment(Element.ALIGN_CENTER);
        titulos.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titulos.setBorder(Rectangle.NO_BORDER);
        table.addCell(titulos);
        titulos = new PdfPCell();
        titulos.setBorder(Rectangle.BOTTOM);

        titulos = new PdfPCell();//9
        titulos.setBorder(Rectangle.BOTTOM);
        titulos.addElement(new Phrase(" Estado actual ", fontTituloGrilla));
        titulos.setPadding(0);
        titulos.setHorizontalAlignment(Element.ALIGN_CENTER);
        titulos.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titulos.setBorder(Rectangle.NO_BORDER);
        table.addCell(titulos);

        titulos = new PdfPCell();
        titulos.setBorder(Rectangle.BOTTOM); //8     
        titulos.addElement(new Phrase(" Fecha estado actual ", fontTituloGrilla));
        titulos.setBorder(Rectangle.NO_BORDER);
        table.addCell(titulos);

        titulos = new PdfPCell();
        titulos.setBorder(Rectangle.BOTTOM);
        titulos.addElement(new Phrase(" Causa Rechazo ", fontTituloGrilla));
        titulos.setBorder(Rectangle.NO_BORDER);
        table.addCell(titulos);

        titulos = new PdfPCell();
        titulos.setBorder(Rectangle.NO_BORDER);
        titulos.addElement(new Phrase(" Fecha Desde ", fontTituloGrilla));
        table.addCell(titulos);

        titulos = new PdfPCell();//5
        titulos.setBorder(Rectangle.NO_BORDER);
        titulos.addElement(new Phrase(" Fecha Hasta ", fontTituloGrilla));
        table.addCell(titulos);

        titulos = new PdfPCell();
        titulos.setBorder(Rectangle.NO_BORDER);
        titulos.addElement(new Phrase(" Días  Trabajados  ", fontTituloGrilla));
        table.addCell(titulos);

        titulos = new PdfPCell();//3
        titulos.setBorder(Rectangle.NO_BORDER);
        titulos.addElement(new Phrase(" Rut Prestador ", fontTituloGrilla));
        table.addCell(titulos);

        titulos = new PdfPCell();//1
        titulos.setBorder(Rectangle.NO_BORDER);
        titulos.addElement(new Phrase(" Nombre Prestador  ", fontTituloGrilla));
        table.addCell(titulos);

        titulos = new PdfPCell();
        titulos.setBorder(Rectangle.NO_BORDER);
        titulos.addElement(new Phrase(" Fecha  Pago  ", fontTituloGrilla));
        table.addCell(titulos);

        table.setHeaderRows(1);
        //document.add(table3);
        int i=0;
    //    PdfPTable table = new PdfPTable(numColumn);
        PdfPCell text = new PdfPCell();
        float[] columnWidths = new float[]{4f, 8f, 6f, 6f, 6f, 6f, 6f, 6f, 6f, 6f};
    //    table.setWidths(columnWidths2);
        table.setWidthPercentage(100);
        float h = 16f;
  
        Object[] o = pdfPrintGeneradorUtil.obtenerDatosCotizaciones(rut,periodoDesde,periodoHasta,consultarLicenciaMedica);

        if (o == null || o.length == 0 || "0".equals(String.valueOf((((Map) o[0]).get("RutEmpleador"))).trim())) {

            document.add(table);
            text = new PdfPCell();
            text.setColspan(numColumn);
            text.setBorder(Rectangle.TOP);
            text.setFixedHeight(30);
            text.addElement(new Phrase("                                      No se encontró información de cotizaciones ", fontNoRecord));
            text.setHorizontalAlignment(Element.ALIGN_CENTER);
            text.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(text);
                        table.setComplete(true);
            document.add(table);

        } else {
            for (Object m : o) {
                Map acreditacion2 = ((Map) m);

                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(0);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                titulos.setHorizontalAlignment(Element.ALIGN_LEFT);
                titulos.setVerticalAlignment(Element.ALIGN_CENTER);
                text.setFixedHeight(h);
                text.setBorderColor(BaseColor.BLACK);
                String rutEmpleador = String.valueOf(acreditacion2.get("RutEmpleador")) + "-" + acreditacion2.get("DigEmpleador");
                text.addElement(new Phrase(rutEmpleador, font3));
                text.setFixedHeight(h);
                table.addCell(text);
                /*-----------------------------------2-------------------------------------------------------------
                 */

                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(0);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                // text.setBorder(Rectangle.RIGHT);
                titulos.setHorizontalAlignment(Element.ALIGN_LEFT);
                titulos.setVerticalAlignment(Element.ALIGN_CENTER);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
                text.addElement(new Phrase((String) acreditacion2.get("RazonSocial"), font3));
                table.addCell(text);
                /*-------------------------------3-----------------------------------------------------------------
                 */

                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(0);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                titulos.setHorizontalAlignment(Element.ALIGN_LEFT);
                titulos.setVerticalAlignment(Element.ALIGN_CENTER);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
                Long codCausaRechazo = (Long) acreditacion2.get("CodCausaRechazo");
                String causaRechazo1 = "";
                if (codCausaRechazo == 0) {
                    causaRechazo1 = "Aceptada";

                } else {

                    causaRechazo1 = "Rechazada";
                }
                text.addElement(new Phrase(causaRechazo1, font3));
                table.addCell(text);

                /*-------------------------------4-----------------------------------------------------------------
                 */
                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(10);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);

                 try {
                    String fechaEmision = (String) acreditacion2.get("FechaEmision");
                  
        
                    parsedDate = dateFormat.parse(fechaEmision);
                    strDate = dateFormat.format(parsedDate); 
                    } catch (java.text.ParseException e) {
                        System.out.println("error parse");
 
                    }
                              text.addElement(new Phrase(strDate, font3));
             

                table.addCell(text);

                /*--------------------------5----------------------------------------------------------------------
                 */
                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(10);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                //text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
               
                text.addElement(new Phrase( (String) acreditacion2.get("GloCausaRechazo"), font3));
                table.addCell(text);
                /*----------------------------------6--------------------------------------------------------------
                 */
                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(10);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                //text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
               
                 try {
                    String fecAutDesde = (String) acreditacion2.get("FecAutDesde");                 
         
                    parsedDate = dateFormat.parse(fecAutDesde);
                    strDate = dateFormat.format(parsedDate); 
                    } catch (java.text.ParseException e) {
                        System.out.println("error parse");
 
                    }
                 text.addElement(new Phrase(strDate, font3));
                table.addCell(text);

                /*-----------------------------7-------------------------------------------------------------------
                 */
                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(10);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                //text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
                     try {
                    String fecAutHasta = (String) acreditacion2.get("FecAutHasta");                 

                    parsedDate = dateFormat.parse(fecAutHasta);
                    strDate = dateFormat.format(parsedDate); 
                    } catch (java.text.ParseException e) {
                        System.out.println("error parse");
 
                    }
                text.addElement(new Phrase(strDate, font3));
                table.addCell(text);

                /*------------------8------------------------------------------------------------------------------
                 */
                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(10);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                //text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
                String diasAut=acreditacion2.get("DiasAutorizados").toString();
                
                text.addElement(new Phrase(diasAut, font3));
                table.addCell(text);

                /*------------------------9------------------------------------------------------------------------
                 */
                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(10);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                //text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
                  String rutProfesional=acreditacion2.get("RutProfesional").toString();
                text.addElement(new Phrase(rutProfesional, font3));
                table.addCell(text);

                /*---------------------------------10---------------------------------------------------------------
                 */
                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(10);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                //text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
                      String nombreProfesional=acreditacion2.get("NombreProfesional").toString();
                
                text.addElement(new Phrase(nombreProfesional, font3));
                table.addCell(text);

                /*---------------------------------11---------------------------------------------------------------
                 */
                text = new PdfPCell();
                text.setPaddingBottom(0);
                text.setPaddingLeft(10);
                text.setPaddingTop(0);
                text.setBorder(Rectangle.TOP);
                //text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.BLACK);
                text.setFixedHeight(h);
                text.addElement(new Phrase("01-12-201", font3));
                table.addCell(text);

            }

            table.setComplete(true);
            document.add(table);
        }

        /*------------------------------------------------------------------------------------------------
         */
        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(100);
        Image image2 = Image.getInstance("imagen/timbre.jpg");
        image2.scalePercent(30f);
        image2.setAlignment(Element.ALIGN_CENTER);
        Image image3 = Image.getInstance("imagen/firma.png");
        image3.scalePercent(30f);
        image3.setAlignment(Element.ALIGN_CENTER);
        PdfPCell texto0 = new PdfPCell();
        Chunk chunkfoot0 = new Chunk("Se extiende el presente certificado a petición del interesado, para los fines relacionados al área de salud.", font1);
        Paragraph p0 = new Paragraph();
        p0.add(chunkfoot0);
        texto0.addElement(p0);
        texto0.setBorder(Rectangle.NO_BORDER);
        texto0.setVerticalAlignment(Element.ALIGN_TOP);
        texto0.setColspan(2);
        table2.addCell(texto0);
        PdfPCell image1LeftCell = new PdfPCell();
        image1LeftCell.setImage(image2);
        image1LeftCell.setFixedHeight(128f);
        image1LeftCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        image1LeftCell.setBorder(Rectangle.NO_BORDER);
        table2.addCell(image1LeftCell);
        /*------------------------------------------------------------------------------------------------
         */
        PdfPCell image2LeftCell = new PdfPCell();
        image2LeftCell.setImage(image3);
        image2LeftCell.setFixedHeight(128f);
        image2LeftCell.setBorder(Rectangle.NO_BORDER);
        image2LeftCell.setBorderColor(BaseColor.BLUE);
        image2LeftCell.setBorder(Rectangle.NO_BORDER);
        table2.addCell(image2LeftCell);
        PdfPCell texto = new PdfPCell();
        Chunk chunkfoot1 = new Chunk("\nEste certificado tiene una validez de 60 días, después de su emisión.", font1);
        Chunk chunkfoot2 = new Chunk("\nCódigo Verificación: 559298963  ", font1);
        Chunk chunkfoot3 = new Chunk("\nNotas:", font1);
        Chunk chunkfoot4 = new Chunk("\n1. Verifique que los montos aquí señalados sean iguales a los descuentos por concepto de cotización por salud FONASA efectuados por su empleador en su liquidación de remuneraciones o pensión. Cualquier consulta o reclamo se debe hacer llegar a FONASA a través de la página web (http://www.fonasa.cl).", font1);
        Chunk chunkfoot5 = new Chunk("\n2. Esta cartola de cotizaciones no es válida para efectos de lo establecido en la Ley N°19.844, relativa a acreditación de pago de cotizaciones previsionales por parte del empleador.", font1);
        Chunk chunkfoot6 = new Chunk("\n3. CT: Cotizaciones Temporales, serán validadas por FONASA en forma posterior.", font1);
        Chunk chunkfoot7 = new Chunk("\n4. PBS: Pensión Básica Solidaria.", font1);
        Paragraph p = new Paragraph();
        p.add(chunkfoot1);
        p.add(chunkfoot2);
        p.add(chunkfoot3);
        p.add(chunkfoot4);
        p.add(chunkfoot5);
        p.add(chunkfoot6);
        p.add(chunkfoot7);
        texto.setColspan(2);
        texto.addElement(p);
        texto.setPaddingTop(0);

        texto.setBorder(Rectangle.NO_BORDER);
        texto.setVerticalAlignment(Element.ALIGN_TOP);
        table2.addCell(texto);

        document.add(table2);
        document.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DocumentException ex) {
            Logger.getLogger(CertificadoLicenciasMedicasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DocumentException ex) {
            Logger.getLogger(CertificadoLicenciasMedicasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

	public String getCertificadoWSDL() {
		return certificadoWSDL;
	}

	public void setCertificadoWSDL(String certificadoWSDL) {
		this.certificadoWSDL = certificadoWSDL;
	}

	public String getConsultarLicenciaMedica() {
		return consultarLicenciaMedica;
	}

	public void setConsultarLicenciaMedica(String consultarLicenciaMedica) {
		this.consultarLicenciaMedica = consultarLicenciaMedica;
	}

}
