package com.digitalsignature.servlet;

import com.digitalsignature.util.DBConnection;
import com.digitalsignature.util.RSAUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/SignDocumentServlet")
@MultipartConfig(
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 12 * 1024 * 1024
)
public class SignDocumentServlet extends HttpServlet {

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

            String documentHash =
                    calculateSHA256(documentBytes);

            String digitalSignature =
                    RSAUtil.sign(documentBytes);

            String sql =
                    "INSERT INTO DIGITAL_SIGNATURES "
                    + "(FILE_NAME, DOCUMENT_HASH, SIGNATURE) "
                    + "VALUES (?, ?, ?)";

            try (
                    Connection con =
                            DBConnection.getConnection();

                    PreparedStatement ps =
                            con.prepareStatement(sql)
            ) {
                ps.setString(1, fileName);
                ps.setString(2, documentHash);
                ps.setString(3, digitalSignature);

                ps.executeUpdate();
            }

            out.print(
                    "{"
                    + "\"success\":true,"
                    + "\"message\":\"Document signed successfully.\","
                    + "\"fileName\":\"" + escape(fileName) + "\","
                    + "\"hash\":\"" + documentHash + "\","
                    + "\"signature\":\""
                    + escape(digitalSignature)
                    + "\""
                    + "}"
            );

        } catch (Exception e) {
            e.printStackTrace();

            sendError(
                    out,
                    "Signing failed: " + getSafeMessage(e)
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