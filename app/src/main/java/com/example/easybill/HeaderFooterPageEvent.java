package com.example.easybill;


import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.Date;

class HeaderFooterPageEvent extends PdfPageEventHelper {
    public void onStartPage(PdfWriter writer, Document document, Context context) {
        PdfContentByte cb = writer.getDirectContent();


        // Add header content (company name, address, phone number, and date)
        cb.saveState();
        cb.beginText();

        // Set font and size for company name
        try {
            cb.setFontAndSize(BaseFont.createFont(), 16);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cb.setTextMatrix(document.left(), document.top() - 30); // Adjust vertical position as needed
        cb.showText(String.valueOf(R.string.name)); // Replace "Your Company Name" with the actual company name

        // Set font and size for address
        try {
            cb.setFontAndSize(BaseFont.createFont(), 12);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cb.setTextMatrix(document.left(), document.top() - 50); // Adjust vertical position as needed
        cb.showText(String.valueOf(R.string.address)); // Replace "Your Company Address" with the actual address

        // Set font and size for phone number and date
        try {
            cb.setFontAndSize(BaseFont.createFont(), 10);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cb.setTextMatrix(document.left(), document.top() - 70); // Adjust vertical position as needed
        cb.showText(String.valueOf(R.string.phoneNumber)); // Replace with the actual phone number
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cb.showTextAligned(Element.ALIGN_RIGHT, "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()), document.right(), document.top() - 70, 0);
        }

        cb.endText();
        cb.restoreState();
    }


    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();

        // Add footer content (e.g., page numbers and additional text)
        cb.saveState();
        cb.beginText();

        // Set font and size for page numbers
        try {
            cb.setFontAndSize(BaseFont.createFont(), 10);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cb.setTextMatrix(document.left(), document.bottom() - 30); // Adjust vertical position for page numbers
        cb.showText("Page " + writer.getPageNumber());

        // Set font and size for additional text
        try {
            cb.setFontAndSize(BaseFont.createFont(), 8);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cb.setTextMatrix(document.right(), document.bottom() - 50); // Adjust vertical position for additional text
        cb.showText("sign: "); // Replace with your desired additional text

        cb.endText();
        cb.restoreState();
    }


}
