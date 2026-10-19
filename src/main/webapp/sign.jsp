<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Sign Document | Digital Sign</title>

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
            <a class="active"
               href="<%= request.getContextPath() %>/sign.jsp">
                <i class="fa-solid fa-pen-nib"></i>
                Sign document
            </a>

            <a href="<%= request.getContextPath() %>/verify.jsp">
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
                <h2>Sign a document</h2>
                <p>
                    Upload a PDF to create a SHA-256 hash and RSA digital
                    signature.
                </p>
            </div>

            <div class="user-avatar">
                <i class="fa-solid fa-pen-nib"></i>
            </div>
        </div>

        <section class="verification-grid">

            <div class="upload-card">

                <div class="card-icon">
                    <i class="fa-solid fa-file-signature"></i>
                </div>

                <h3>Upload PDF document</h3>

                <p>
                    Select the PDF document that you want to digitally sign.
                    Maximum allowed file size: 10 MB.
                </p>

                <form id="signForm"
                      action="<%= request.getContextPath() %>/SignDocumentServlet"
                      method="post"
                      enctype="multipart/form-data">

                    <label for="document"
                           id="dropZone"
                           class="drop-zone">

                        <i class="fa-solid fa-cloud-arrow-up"></i>

                        <span class="drop-title">
                            Drag and drop your PDF here
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
                            id="signButton"
                            class="primary-btn verify-btn">

                        <i class="fa-solid fa-pen-nib"></i>
                        Sign document
                    </button>

                </form>

            </div>

            <div class="verification-info">

                <div class="info-card">
                    <h3>
                        <i class="fa-solid fa-shield-halved green-icon"></i>
                        Signing process
                    </h3>

                    <ul class="check-list">
                        <li>PDF bytes are read securely</li>
                        <li>SHA-256 document hash is created</li>
                        <li>RSA-2048 digital signature is generated</li>
                        <li>Signature record is stored in Oracle</li>
                    </ul>
                </div>

                <div class="info-card security-note">
                    <h3>
                        <i class="fa-solid fa-lock"></i>
                        Security notice
                    </h3>

                    <p>
                        Signing creates a secure RSA signature from the exact
                        uploaded PDF bytes. If the document changes, its hash
                        and signature verification will fail.
                    </p>
                </div>

            </div>

        </section>

        <section id="signResult"
                 class="result-card hidden">
        </section>

    </main>

</div>

<script src="<%= request.getContextPath() %>/js/signature.js"></script>

</body>
</html>