package cl.fonasa.certificados;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.itextpdf.text.BaseColor;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import cl.fonasa.genera.codigo.CodigoCertificadoUtil;
import cl.fonasa.util.PdfPrintGeneradorUtil;

import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;



/**
 *
 * @author Agusto@WebServlet("/RequestDetails")
 */


@WebServlet(urlPatterns = "/certificadoDirecciones/*", loadOnStartup = 1)
public class CertificadoDireccionesServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DocumentException {
        response.setContentType("application/pdf;charset=ISO-8859-1");

        // create document
        //Fecha actual desglosada:
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
            String rutCotizanteNumero = request.getParameter("RUT");
            String DV = request.getParameter("RUT_DV");
            String rut=rutCotizanteNumero + "-" + DV;         
            CertificadoCotizacionesDatos cotizacionesDatos = new CertificadoCotizacionesDatos(request);
            CodigoCertificadoUtil codigoCertificadoUtil=new CodigoCertificadoUtil();
            String codVerificacion = codigoCertificadoUtil.generaCodigoCertificado(rutCotizanteNumero+ DV, "X", "1");
            PdfPrintGeneradorUtil pdfPrintGeneradorUtil=new PdfPrintGeneradorUtil();
            pdfPrintGeneradorUtil.obtenerDireccion(cotizacionesDatos,rut);
            Calendar fecha = Calendar.getInstance();
            int año = fecha.get(Calendar.YEAR);
            int mes = fecha.get(Calendar.MONTH) + 1;
            int dia = fecha.get(Calendar.DAY_OF_MONTH);

            Calendar c1 = GregorianCalendar.getInstance();
            Locale locale = Locale.getDefault();
            String fechaActual = dia + " de " + fecha.getDisplayName(Calendar.MONTH, Calendar.LONG, locale) + " del " + año ;
            Image image1 = Image.getInstance("imagen/LOG-FON.jpg");

            ServletOutputStream os = response.getOutputStream();
            //set the response content type to PDF
            response.setContentType("application/pdf");
            response.setContentType("application/pdf;charset=ISO-8859-1");
            response.setCharacterEncoding("ISO-8859-1");
            //create a new document
            Document document = new Document(PageSize.A4, 36, 36, 120, 136);
            Font font1 = FontFactory.getFont("SansSerif", 10);
            Font fontBody = FontFactory.getFont("SansSerif", 10);
             Font fontBodyBold = FontFactory.getFont("SansSerif", 10);
            PdfWriter writer = PdfWriter.getInstance(document, os);
            Font f2 = new Font();
            f2.setFamily(FontFamily.COURIER.name());
            font1.setStyle(Font.BOLD);
            f2.setSize(10);
            // add header and footer
            HeadFootCertificadoDirecc event = new HeadFootCertificadoDirecc();
            writer.setPageEvent(event);

            // write to document
            document.open();
            document.add(Chunk.NEWLINE);
            Paragraph para = new Paragraph();
            para.setFont(font1);

            font1.setStyle(font1.BOLD);
            para.add(" Fecha Emisión:  " + fechaActual);
            para.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(para);
            font1.setStyle(font1.NORMAL);
            fontBodyBold.setStyle(font1.BOLD);
            font1.setStyle(font1.NORMAL);
            Chunk chunkfoot1 = new Chunk("El ", fontBody);

            Chunk chunkfoot2 = new Chunk("FONDO NACIONAL DE SALUD ", fontBodyBold);

            Chunk chunkfoot3 = new Chunk(" certifica que de acuerdo a sus registros, Don(a) ", fontBody);

            Chunk chunkfoot4 = new Chunk(cotizacionesDatos.getNombreCotizante() , fontBodyBold);

            Chunk chunkfoot5 = new Chunk(", RUN,", fontBody);

            Chunk chunkfoot6 = new Chunk( rut + ", ", font1);

            Chunk chunkfoot7 = new Chunk("presenta domicilio en ", fontBody);

            Chunk chunkfoot8 = new Chunk(cotizacionesDatos.getDireccion() + " ", fontBodyBold);
            Paragraph p = new Paragraph();
                 p.add(chunkfoot1);
                 p.add(chunkfoot2);
                 p.add(chunkfoot3);
                 p.add(chunkfoot4);
                 p.add(chunkfoot5);
                 p.add(chunkfoot6);
                 p.add(chunkfoot7);
                 p.add(chunkfoot8);
                             p.setAlignment(Element.ALIGN_JUSTIFIED);
               document.add(  p); 

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            font1.setStyle(font1.NORMAL);
            document.add(new Phrase("Se extiende el presente certificado a petición del interesado, para los fines relacionados al área de salud.", font1));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            PdfPTable table2 = new PdfPTable(2);
            float[] widthsTable2 = new float[] { 40f, 60f};
          //  table2.setWidths(widths);
            Image image2 = Image.getInstance("imagen/timbre.jpg");
            image2.scalePercent(30f);
            image2.setAlignment(Element.ALIGN_CENTER);
            //  document.add(        image2 );
            Image image3 = Image.getInstance("imagen/firma.png");
            image3.scalePercent(100f);
            image3.setAlignment(Element.ALIGN_CENTER);
            PdfPCell image1LeftCell = new PdfPCell();
            image1LeftCell.setImage(image2);
            image1LeftCell.setFixedHeight(128f);
            image1LeftCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            image1LeftCell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(image1LeftCell);
            PdfPCell image2LeftCell = new PdfPCell();
            image2LeftCell.setImage(image3);
            image2LeftCell.setFixedHeight(128f);
            image2LeftCell.setBorder(Rectangle.NO_BORDER);
            image2LeftCell.setBorderColor(BaseColor.BLUE);
            table2.addCell(image2LeftCell);
            document.add(table2);            
            PdfPTable table3 = new PdfPTable(2);
            float[] widths = new float[] { 40f, 60f};
            table3.setWidths(widths);
            PdfPCell text = new PdfPCell();
            text.setBorder(Rectangle.NO_BORDER);
            table3.addCell(text);
            text = new PdfPCell();
            text.setHorizontalAlignment(Element.ALIGN_RIGHT);
            text.setBorder(Rectangle.NO_BORDER);
            font1.setStyle(font1.BOLD);
            font1.setSize(8);
            text.addElement(new Phrase("Queremos que esté más informado." , font1));
            text.addElement(new Phrase("llámenos al 600 360 3000 " , font1));
            text.addElement(new Phrase("Visítenos en www.fonasa.cl " , font1));
            table3.addCell(text);
 
            document.add(table3);
          
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
        
   
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
        
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);  

            para = new Paragraph();
            para.setFont(font1);

            font1.setSize(10);
            font1.setStyle(font1.NORMAL);
            para.add("Este certificado tiene una validez de 60 días, después de su emisión " );
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);
            para = new Paragraph();
            para.setFont(font1);
            para.add("Fecha emisión :    " + fechaActual);              
            document.add(para);           
            para = new Paragraph();
            para.setFont(font1);
            para.add("Código de Verifificacón :   " + codVerificacion);
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);
            document.close();

        } catch (DocumentException ex) {
            Logger.getLogger(CertificadoDireccionesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
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
        doGet(request, response);
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

}

