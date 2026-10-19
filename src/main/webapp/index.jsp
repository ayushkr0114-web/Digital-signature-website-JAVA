<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Digital Sign | Secure e-sign platform</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<div class="page-wrapper">

    <div class="auth-layout">

        <section class="info-panel">

            <div class="secure-badge">
                <span class="status-dot"></span>
                Secure digital signature
            </div>

            <div class="info-card">
                <h2>Why teams trust Digital Sign</h2>

                <ul class="feature-list">
                    <li>
                        <span>•</span>
                        RSA 2048-bit encrypted digital signatures
                    </li>
                    <li>
                        <span>•</span>
                        SHA-256 tamper detection for every PDF
                    </li>
                    <li>
                        <span>•</span>
                        Oracle database audit records
                    </li>
                </ul>
            </div>

            <div class="info-card compliance-card">
                <h2>Security standards</h2>

                <div class="compliance-tags">
                    <span>RSA-2048</span>
                    <span>SHA-256</span>
                    <span>Oracle DB</span>
                    <span>PDF</span>
                </div>
            </div>

            <div class="info-card works-card">
                <h2>How it works</h2>
                <p>1) Upload your PDF document</p>
                <p>2) Create RSA digital signature</p>
                <p>3) Verify document authenticity anytime</p>
            </div>

            <div class="testimonial-card">
                <p>
                    “Fast, secure, and simple — signed documents can be
                    verified instantly.”
                </p>
                <small>— Digital Sign Platform</small>
            </div>

        </section>

        <section class="login-panel home-panel">

            <div class="brand-row">
                <div class="brand-logo">DS</div>

                <div>
                    <h1>Digital Sign</h1>
                    <p>Secure e-sign platform</p>
                </div>

                <div class="support-link">
                    Secure
                    <i class="fa-solid fa-shield-halved"></i>
                </div>
            </div>

            <div class="login-heading">
                <h2>Welcome</h2>

                <p>
                    Sign your PDF with RSA encryption or verify a previously
                    signed document.
                </p>
            </div>

            <div class="home-action-card">
                <div class="action-icon purple-icon">
                    <i class="fa-solid fa-pen-nib"></i>
                </div>

                <div>
                    <h3>Sign a document</h3>
                    <p>
                        Upload a PDF and generate its SHA-256 hash and RSA
                        digital signature.
                    </p>
                </div>

                <a class="action-arrow"
                   href="<%= request.getContextPath() %>/sign.jsp">
                    <i class="fa-solid fa-arrow-right"></i>
                </a>
            </div>

            <div class="home-action-card">
                <div class="action-icon green-bg-icon">
                    <i class="fa-solid fa-shield-halved"></i>
                </div>

                <div>
                    <h3>Verify a document</h3>
                    <p>
                        Confirm file integrity, document hash, and RSA
                        signature validity.
                    </p>
                </div>

                <a class="action-arrow"
                   href="<%= request.getContextPath() %>/verify.jsp">
                    <i class="fa-solid fa-arrow-right"></i>
                </a>
            </div>

            <div class="home-security-note">
                <i class="fa-solid fa-lock"></i>
                Files are processed securely and signature records are stored
                in Oracle Database.
            </div>

        </section>

    </div>

</div>

</body>
</html>