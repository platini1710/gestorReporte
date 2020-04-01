package cl.fonasa.certificados;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class HeadFootCertificadoDirecc extends PdfPageEventHelper {

    private PdfTemplate t;
    private Image total;

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

    private void addHeader(PdfWriter writer){
        PdfPTable table = new PdfPTable(3);
        try {
            // set defaults
         	 Font font1 = FontFactory.getFont("SansSerif", 12);
        	table.setWidths(new float[] { 2,4, 1 });
        	table.setTotalWidth(527);
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
            font1.setSize(16);
            font1.setStyle(font1.BOLD);
            Chunk chunk1 = new Chunk("CERTIFICADO DE DIRECCIONES");
            //chunk1.setUnderline(1.5f, -1);
            chunk1.setFont(font1);
            text.addElement(chunk1);
            font1.setSize(12);
            text.setFixedHeight(68f);
            text.setBorder(Rectangle.NO_BORDER);
            table.addCell(text);
            logoLeftCell = new PdfPCell();
             logo =Image.getInstance("imagen/logoFonasa.png");
            logo.setAlignment(Element.ALIGN_RIGHT);
            logoLeftCell.setImage(logo);
            logoLeftCell.setFixedHeight(68f);
            logoLeftCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoLeftCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(logoLeftCell);
            // write content
            table.writeSelectedRows(0, -1, 34, 820, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (MalformedURLException e) {
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3);
        try {
            // set defaults
            footer.setWidths(new int[]{24, 2, 1});
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
            throw new ExceptionConverter(de);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber()-1), new Font(Font.FontFamily.HELVETICA, 8)),
                totalWidth, 6, 0);
    }
}