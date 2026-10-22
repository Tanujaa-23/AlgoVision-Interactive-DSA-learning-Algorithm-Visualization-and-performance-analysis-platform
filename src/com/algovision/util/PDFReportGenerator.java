package com.algovision.util;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.OutputStream;
import java.util.Date;

/**
 * Utility for generating professional downloadable PDF execution reports using OpenPDF.
 */
public class PDFReportGenerator {

    public static void generateExecutionReport(
            OutputStream out,
            String userName,
            String algorithmName,
            String inputData,
            String outputData,
            int stepCount,
            int comparisonCount,
            int swapCount,
            double executionTimeMs,
            String bestCase,
            String avgCase,
            String worstCase,
            String spaceComplexity
    ) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Colors
        Color primaryColor = new Color(79, 70, 229); // Indigo
        Color darkColor = new Color(30, 41, 59);    // Slate Dark

        // Fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, primaryColor);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, darkColor);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);

        // Header Title
        Paragraph title = new Paragraph("AlgoVision Pro - Execution Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph sub = new Paragraph("Algorithm Performance & Complexity Analysis", subtitleFont);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(15);
        document.add(sub);

        document.add(new Paragraph("Generated for: " + (userName != null ? userName : "Student"), bodyFont));
        document.add(new Paragraph("Date: " + new Date().toString(), bodyFont));
        document.add(new Paragraph("--------------------------------------------------------------------------------------------------"));

        // Section 1: Execution Overview
        Paragraph sec1 = new Paragraph("1. Execution Metrics", subtitleFont);
        sec1.setSpacingBefore(10);
        sec1.setSpacingAfter(10);
        document.add(sec1);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        addTableCell(table, "Parameter", headerFont, primaryColor);
        addTableCell(table, "Value", headerFont, primaryColor);

        addTableRow(table, "Algorithm Name", algorithmName);
        addTableRow(table, "Input Data", inputData);
        addTableRow(table, "Output / Result", outputData);
        addTableRow(table, "Total Steps", String.valueOf(stepCount));
        addTableRow(table, "Comparisons", String.valueOf(comparisonCount));
        addTableRow(table, "Swaps / Operations", String.valueOf(swapCount));
        addTableRow(table, "Execution Time", String.format("%.3f ms", executionTimeMs));

        document.add(table);

        // Section 2: Complexity Analysis
        Paragraph sec2 = new Paragraph("2. Theoretical Complexity Analysis", subtitleFont);
        sec2.setSpacingBefore(15);
        sec2.setSpacingAfter(10);
        document.add(sec2);

        PdfPTable compTable = new PdfPTable(2);
        compTable.setWidthPercentage(100);

        addTableCell(compTable, "Complexity Metric", headerFont, primaryColor);
        addTableCell(compTable, "Big-O Notation", headerFont, primaryColor);

        addTableRow(compTable, "Best-Case Time Complexity", bestCase);
        addTableRow(compTable, "Average-Case Time Complexity", avgCase);
        addTableRow(compTable, "Worst-Case Time Complexity", worstCase);
        addTableRow(compTable, "Auxiliary Space Complexity", spaceComplexity);

        document.add(compTable);

        Paragraph footer = new Paragraph("\n\nThank you for using AlgoVision Pro platform to master Data Structures & Algorithms!", bodyFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }

    private static void addTableCell(PdfPTable table, String text, Font font, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private static void addTableRow(PdfPTable table, String key, String val) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        table.addCell(new Phrase(key, font));
        table.addCell(new Phrase(val != null ? val : "N/A", font));
    }
}
