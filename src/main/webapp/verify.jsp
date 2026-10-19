<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Verify Document | Digital Sign</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<div class="app-page">

    <header class="topbar">

        <a class="topbar-brand"
           href="<%= request.getContextPath() %>/index.jsp">

            <div class="brand-logo">DS</div>

            <div>
                <h1>Digital Sign</h1>
                <p>Secure e-sign platform</p>
            </div>
        </a>

        <nav class="topbar-nav">
            <a href="<%= request.getContextPath() %>/sign.jsp">
                <i class="fa-solid fa-pen-nib"></i>
                Sign document
            </a>

            <a class="active"
               href="<%= request.getContextPath() %>/verify.jsp">
                <i class="fa-solid fa-shield-halved"></i>
                Verify
            </a>

            <a href="<%= request.getContextPath() %>/index.jsp">
                <i class="fa-solid fa-house"></i>
                Home
            </a>
        </nav>

    </header>

    <main class="dashboard-content">

        <div class="dashboard-heading">
            <div>
                <h2>Verify a document</h2>
                <p>
                    Check the integrity and RSA digital signature of a PDF.
                </p>
            </div>

            <div class="user-avatar">
                <i class="fa-solid fa-shield-halved"></i>
            </div>
        </div>

        <section class="verification-grid">

            <div class="upload-card">

                <div class="card-icon">
                    <i class="fa-solid fa-file-shield"></i>
                </div>

                <h3>Upload signed PDF</h3>

                <p>
                    Upload exactly the same PDF file that was signed earlier.
                    The document hash and RSA signature will be checked.
                </p>

                <form id="verifyForm"
                      action="<%= request.getContextPath() %>/VerifyDocumentServlet"
                      method="post"
                      enctype="multipart/form-data">

                    <label for="document"
                           id="dropZone"
                           class="drop-zone">

                        <i class="fa-solid fa-cloud-arrow-up"></i>

                        <span class="drop-title">
                            Drag and drop your signed PDF here
                        </span>

                        <span class="drop-subtitle">
                            Or click to browse from your computer
                        </span>

                        <input type="file"
                               id="document"
                               name="document"
                               accept="application/pdf"
                               hidden
                               required>
                    </label>

                    <div id="selectedFile" class="selected-file">
                        No file selected
                    </div>

                    <button type="submit"
                            id="verifyButton"
                            class="primary-btn verify-btn">

                        <i class="fa-solid fa-shield-check"></i>
                        Verify document
                    </button>

                </form>

            </div>

            <div class="verification-info">

                <div class="info-card">
                    <h3>
                        <i class="fa-solid fa-circle-check green-icon"></i>
                        What we verify
                    </h3>

                    <ul class="check-list">
                        <li>Signature record exists in Oracle database</li>
                        <li>Current PDF SHA-256 hash matches</li>
                        <li>RSA signature is cryptographically valid</li>
                        <li>PDF document has not been modified</li>
                    </ul>
                </div>

                <div class="info-card security-note">
                    <h3>
                        <i class="fa-solid fa-lock"></i>
                        Important
                    </h3>

                    <p>
                        For successful verification, upload the same file name
                        and the exact original PDF bytes used during signing.
                        Any content modification makes verification fail.
                    </p>
                </div>

            </div>

        </section>

        <section id="verificationResult"
                 class="result-card hidden">
        </section>

    </main>

</div>

<script src="<%= request.getContextPath() %>/js/signature.js"></script>

</body>
</html>