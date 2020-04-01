package cl.fonasa.certificados;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeadFootCertificadoLicMed extends PdfPageEventHelper {
  private static final Logger log = LoggerFactory.getLogger(HeadFootCertificadoLicMed.class);
    private PdfTemplate t;
    private Image total;
    private String nombre;
    private String run;
    private String periodoDesde, periodoHasta;

    public HeadFootCertificadoLicMed(String nombre, String run, String periodoDesde, String periodoHasta) {
        this.nombre = nombre;
        this.run = run;
        this.periodoDesde = periodoDesde;
        this.periodoHasta = periodoHasta;
    }
    public void onOpenDocument(PdfWriter writer, Document document) {

        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        addHeader(writer);
        addFooter(writer);
    }

    private void addHeader(PdfWriter writer) {
        PdfPTable table = new PdfPTable(3);
        try {
            // set defaults

            Font font1 = FontFactory.getFont("SansSerif", 12);
            table.setWidths(new float[]{1, 4, 1});
            table.setTotalWidth(527);
            Font fontHead = FontFactory.getFont("SansSerif", 8);
            Font fontHeadBold = FontFactory.getFont("SansSerif", 8);
            table.setLockedWidth(true);
            // add image
            Image logo = Image.getInstance("imagen/LOG-FON.jpg");
            //   logo.scaleToFit(110,110);
            logo.setAbsolutePosition(0, 0);
            logo.setAlignment(Element.ALIGN_LEFT);
            PdfPCell logoLeftCell = new PdfPCell();
            logoLeftCell.setImage(logo);
            logoLeftCell.setFixedHeight(68f);
            logoLeftCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(logoLeftCell);
            // add text
            PdfPCell text = new PdfPCell();
            font1.setSize(14);
            font1.setStyle(font1.BOLD);
            Chunk chunk1 = new Chunk("          CERTIFICADO DE LICENCIAS MÉDICAS");
            //chunk1.setUnderline(1.5f, -1);
            chunk1.setFont(font1);
            text.addElement(chunk1);
            font1.setSize(12);
            text.setFixedHeight(68f);
            text.setBorder(Rectangle.NO_BORDER);
            table.addCell(text);
            text = new PdfPCell();
            logoLeftCell = new PdfPCell();
            logo = Image.getInstance("imagen/logoFonasa.png");
            logo.setAlignment(Element.ALIGN_RIGHT);
            logoLeftCell.setImage(logo);
            logoLeftCell.setFixedHeight(68f);
            logoLeftCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoLeftCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(logoLeftCell);
            // write content
            text = new PdfPCell();
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            text.setColspan(3);
            fontHead.setStyle(Font.NORMAL);
            Chunk chunk0 = new Chunk("Fecha Emisión: " + dateFormat.format(date) , fontHeadBold);
            Paragraph p = new Paragraph();
            p.setAlignment(Paragraph.ALIGN_RIGHT);
            p.add(chunk0);
            text.addElement(p);

            Chunk chunk11 = new Chunk("El ", fontHead);
            font1.setStyle(font1.BOLD);
            fontHeadBold.setStyle(Font.BOLD);
            Chunk chunk21 = new Chunk("FONDO NACIONAL DE SALUD ", fontHeadBold);
            fontHead.setStyle(Font.NORMAL);
            Chunk chunk31 = new Chunk("dispone del siguiente registro de movimientos de cotizaciones de salud para el(la) afiliado(a) Sr(a) ", fontHead);
            Chunk chunk4 = new Chunk(nombre, fontHeadBold);
            Chunk chunk5 = new Chunk(", RUN ", fontHead);
            Chunk chunk6 = new Chunk(run, fontHeadBold);
            Chunk chunk7 = new Chunk(", correspondientes al período de " + periodoDesde + " a " + periodoHasta, fontHead);
             p = new Paragraph();

            p.add(chunk11);
            p.add(chunk21);
            p.add(chunk31);
            p.add(chunk4);
            p.add(chunk5);
            p.add(chunk6);
            p.add(chunk7);
            p.setLeading(fontHead.getSize() * 1.3f);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            text.addElement(p);
            text.setBorder(Rectangle.NO_BORDER);
            table.addCell(text);                      
            table.writeSelectedRows(0, 2, 34, 810, writer.getDirectContent());
        } catch (DocumentException de) {
            log.error(de.getMessage(), de);
            throw new ExceptionConverter(de);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ExceptionConverter(e);
        }
    }

     private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3);
        try {
            // set defaults
            footer.setWidths(new int[]{23, 3, 1});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            footer.addCell(new Phrase("             ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase(String.format("Página %d de", (writer.getPageNumber())), new Font(Font.FontFamily.HELVETICA, 8)));

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(total);
            totalPageCount.setBorder(Rectangle.TOP);
            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
             log.error(de.getMessage(), de);

        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber() - 1), new Font(Font.FontFamily.HELVETICA, 8)),
                totalWidth, 6, 0);
    }
}
