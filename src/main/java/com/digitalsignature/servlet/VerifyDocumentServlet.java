package com.digitalsignature.servlet;

import com.digitalsignature.util.DBConnection;
import com.digitalsignature.util.RSAUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/VerifyDocumentServlet")
@MultipartConfig(
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 12 * 1024 * 1024
)
public class VerifyDocumentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            Part filePart = request.getPart("document");

            if (filePart == null || filePart.getSize() == 0) {
                sendError(out, "Please select a PDF file.");
                return;
            }

            String originalFileName =
                    filePart.getSubmittedFileName();

            String fileName =
                    Paths.get(originalFileName)
                            .getFileName()
                            .toString();

            if (!fileName.toLowerCase().endsWith(".pdf")) {
                sendError(out, "Only PDF files are allowed.");
                return;
            }

            byte[] documentBytes =
                    filePart.getInputStream()
                            .readAllBytes();

            String currentHash =
                    calculateSHA256(documentBytes);

            String sql =
                    "SELECT DOCUMENT_HASH, SIGNATURE, CREATED_AT "
                    + "FROM DIGITAL_SIGNATURES "
                    + "WHERE FILE_NAME = ? "
                    + "ORDER BY CREATED_AT DESC "
                    + "FETCH FIRST 1 ROW ONLY";

            String storedHash = null;
            String storedSignature = null;
            Timestamp signedAt = null;

            try (
                    Connection con =
                            DBConnection.getConnection();

                    PreparedStatement ps =
                            con.prepareStatement(sql)
            ) {
                ps.setString(1, fileName);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        storedHash =
                                rs.getString("DOCUMENT_HASH");

                        storedSignature =
                                rs.getString("SIGNATURE");

                        signedAt =
                                rs.getTimestamp("CREATED_AT");
                    }
                }
            }

            if (storedHash == null || storedSignature == null) {
                out.print(
                        "{"
                        + "\"success\":true,"
                        + "\"verified\":false,"
                        + "\"fileName\":\""
                        + escape(fileName)
                        + "\","
                        + "\"message\":"
                        + "\"No signature record was found for this document.\""
                        + "}"
                );
                return;
            }

            boolean hashMatches =
                    currentHash.equalsIgnoreCase(
                            storedHash
                    );

            boolean signatureValid = false;

            if (hashMatches) {
                signatureValid =
                        RSAUtil.verify(
                                documentBytes,
                                storedSignature
                        );
            }

            boolean verified =
                    hashMatches && signatureValid;

            String signedAtText =
                    signedAt == null
                            ? ""
                            : signedAt.toString();

            String message =
                    verified
                            ? "Document is authentic and signature is valid."
                            : "Document verification failed. The file may have been modified.";

            out.print(
                    "{"
                    + "\"success\":true,"
                    + "\"verified\":" + verified + ","
                    + "\"fileName\":\""
                    + escape(fileName)
                    + "\","
                    + "\"hashMatches\":" + hashMatches + ","
                    + "\"signatureValid\":" + signatureValid + ","
                    + "\"signedAt\":\""
                    + escape(signedAtText)
                    + "\","
                    + "\"message\":\""
                    + escape(message)
                    + "\""
                    + "}"
            );

        } catch (Exception e) {
            e.printStackTrace();

            sendError(
                    out,
                    "Verification failed: " + getSafeMessage(e)
            );
        }
    }

    private String calculateSHA256(byte[] bytes)
            throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

        byte[] hashBytes =
                digest.digest(bytes);

        StringBuilder hash =
                new StringBuilder();

        for (byte b : hashBytes) {
            hash.append(
                    String.format("%02x", b)
            );
        }

        return hash.toString();
    }

    private void sendError(
            PrintWriter out,
            String message) {

        out.print(
                "{"
                + "\"success\":false,"
                + "\"verified\":false,"
                + "\"message\":\""
                + escape(message)
                + "\""
                + "}"
        );
    }

    private String getSafeMessage(Exception e) {
        if (e.getMessage() == null) {
            return "Unexpected server error.";
        }

        return e.getMessage();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}