package org.group02.guitarshop.others;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.group02.guitarshop.entity.Invoice;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class PDFExporter {
    private List<Invoice> invoiceList;
    private float sum;
    private  Date date;
    private Date startDate;
    private Date endDate;

    public PDFExporter(List<Invoice> invoiceList, float sum, Date date) {
        this.invoiceList = invoiceList;
        this.sum = sum;
        this.date = date;
    }

    public PDFExporter(List<Invoice> invoiceList, float sum, Date startDate, Date endDate) {
        this.invoiceList = invoiceList;
        this.sum = sum;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("ID", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell.setPhrase(new Phrase("Order Date", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell.setPhrase(new Phrase("Payment Method", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        DecimalFormat decimalFormat = new DecimalFormat("#,###,##0");
        for (Invoice invoice : invoiceList){
            PdfPCell cell = new PdfPCell();
            cell.setPhrase(new Phrase(String.valueOf(invoice.getId())));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell.setPhrase(new Phrase(invoice.getCustomerEmail()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell.setPhrase(new Phrase(dateFormat.format(invoice.getCreatedTime())));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell.setPhrase(new Phrase(invoice.getPaymentMethod()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell.setPhrase(new Phrase(decimalFormat.format(invoice.getTotal())));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        cell.setColspan(4);
        cell.setPhrase(new Phrase("Subtotal", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell.setPhrase(new Phrase(decimalFormat.format(sum), font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

    }

    public void exportByDate(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Image image = Image.getInstance("src/main/resources/static/img/reportHeader.png");
        float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        image.scaleToFit(documentWidth, 100);
        document.add(image);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(24);
        Paragraph p = new Paragraph("Income Report", font);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        font.setSize(12);
        font = FontFactory.getFont(FontFactory.HELVETICA);
        Paragraph p1 = new Paragraph("(Day: "+dateFormat.format(date)+")", font);
        p1.setAlignment(Element.ALIGN_CENTER);
        document.add(p1);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.0f});
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();
    }

    public void exportFromTo(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Image image = Image.getInstance("src/main/resources/static/img/reportHeader.png");
        float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        image.scaleToFit(documentWidth, 100);
        document.add(image);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(24);
        Paragraph p = new Paragraph("Income of GuitarShop", font);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        font.setSize(12);
        font = FontFactory.getFont(FontFactory.HELVETICA);
        Paragraph p1 = new Paragraph("(From: "+dateFormat.format(startDate)+ " To: "+dateFormat.format(endDate)+")", font);
        p1.setAlignment(Element.ALIGN_CENTER);
        document.add(p1);
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.0f});
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();
    }
}
