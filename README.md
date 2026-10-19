# 🔐 Digital Signature System (JAVA)

A secure, enterprise-grade Java web application designed to digitally sign and verify documents using **RSA Asymmetric Cryptography** and **SHA-256 Hashing**, backed by an **Oracle Database** and deployed on **Apache Tomcat**.

---

## 🌟 Key Features

* **Asymmetric Key Cryptography (RSA):** Generates secure public/private key pairs to sign documents, ensuring non-repudiation and high security.
* **Cryptographic Hashing (SHA-256):** Converts document data into a fixed-size unique hash value to verify integrity.
* **Document Verification Engine:** Instantly validates whether a signed document is authentic or has been tampered with.
* **Database Persistence:** Securely stores signature logs, metadata, and user records using **Oracle Database (`ojdbc11`)**.
* **MVC Architecture:** Built cleanly using standard Java Servlets, JSP, HTML5, CSS3, and JavaScript.

---

## 🏗️ System Architecture & Workflow Diagram

```text
       +-------------------------------------------------------------+
       |                        Client Browser                       |
       |             (HTML5 / JSP / CSS / JavaScript)                |
       +------------------------------+------------------------------+
                                      |
                     HTTP Requests (Sign / Verify)
                                      |
                                      v
       +-------------------------------------------------------------+
       |                  Apache Tomcat Web Server                   |
       +------------------------------+------------------------------+
                                      |
            +-------------------------+-------------------------+
            | (Handling Servlet Routing & Business Logic)       |
            v                                                   v
  +--------------------+                               +--------------------+
  | SignDocumentServlet|                               |VerifyDocumentServlet|
  +---------+----------+                               +---------+----------+
            |                                                    |
            v                                                    v
  +--------------------+                               +--------------------+
  |     RSAUtil        |                               |     RSAUtil        |
  | (SHA-256 + RSA Key)|                               | (Signature Match)  |
  +---------+----------+                               +---------+----------+
            |                                                    |
            +-------------------------+-------------------------+
                                      |
                                      v
       +-------------------------------------------------------------+
       |                       Oracle Database                       |
       |                  (Signature Records & Logs)                 |
       +-------------------------------------------------------------+##
👨‍💻 Author

* **Ayush Kumar**  
  * [GitHub Profile](https://github.com/ayushkr0114-web)
