package com.example.easybill;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

class HeaderFooterPageEvent extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {
        // Create the header text
        Paragraph header = new Paragraph();

        // Shop name (in bold and larger font size)
        Chunk shopNameChunk = new Chunk((char) R.string.name, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        header.add(shopNameChunk);

        // Address
        Chunk addressChunk = new Chunk((char) R.string.address, new Font(Font.FontFamily.HELVETICA, 12));
        header.add(addressChunk);

        // Phone number
        Chunk phoneChunk = new Chunk((char) R.string.phoneNumber, new Font(Font.FontFamily.HELVETICA, 12));
        header.add(phoneChunk);

        // Set the alignment and position of the header
        header.setAlignment(Element.ALIGN_CENTER);

        // Add the header to the document
        try {
            document.add(header);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        // Create the footer text
        Paragraph footer = new Paragraph(String.valueOf(R.string.ownerName), new Font(Font.FontFamily.HELVETICA, 10));

        // Set the alignment and position of the footer
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(10);

        // Add the footer to the document
        try {
            document.add(footer);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
