document.addEventListener("DOMContentLoaded", function () {
    const fileInput = document.getElementById("document");
    const selectedFile = document.getElementById("selectedFile");
    const dropZone = document.getElementById("dropZone");

    const signForm = document.getElementById("signForm");
    const verifyForm = document.getElementById("verifyForm");

    if (fileInput) {
        fileInput.addEventListener("change", function () {
            updateSelectedFile(this.files);
        });
    }

    if (dropZone) {
        ["dragenter", "dragover"].forEach(function (eventName) {
            dropZone.addEventListener(eventName, function (event) {
                event.preventDefault();
                dropZone.classList.add("dragover");
            });
        });

        ["dragleave", "drop"].forEach(function (eventName) {
            dropZone.addEventListener(eventName, function (event) {
                event.preventDefault();
                dropZone.classList.remove("dragover");
            });
        });

        dropZone.addEventListener("drop", function (event) {
            const files = event.dataTransfer.files;

            if (files.length > 0 && fileInput) {
                fileInput.files = files;
                updateSelectedFile(files);
            }
        });
    }

    if (signForm) {
        signForm.addEventListener("submit", function (event) {
            event.preventDefault();
            submitDocumentForm(
                signForm,
                "signResult",
                "Signing..."
            );
        });
    }

    if (verifyForm) {
        verifyForm.addEventListener("submit", function (event) {
            event.preventDefault();
            submitDocumentForm(
                verifyForm,
                "verificationResult",
                "Verifying..."
            );
        });
    }

    function updateSelectedFile(files) {
        if (!selectedFile) {
            return;
        }

        if (!files || files.length === 0) {
            selectedFile.textContent = "No file selected";
            selectedFile.classList.remove("has-file");
            return;
        }

        const file = files[0];

        if (!file.name.toLowerCase().endsWith(".pdf")) {
            selectedFile.textContent = "Only PDF files are allowed.";
            selectedFile.classList.remove("has-file");
            return;
        }

        selectedFile.textContent = "Selected file: " + file.name;
        selectedFile.classList.add("has-file");
    }
    async function submitDocumentForm(
        form,
        resultId,
        loadingText
    ) {
        const resultBox = document.getElementById(resultId);
        const submitButton = form.querySelector(
            "button[type='submit']"
        );

        if (!fileInput || fileInput.files.length === 0) {
            showError(resultBox, "Please select a PDF document.");
            return;
        }

        const selectedDocument = fileInput.files[0];

        if (!selectedDocument.name.toLowerCase().endsWith(".pdf")) {
            showError(resultBox, "Only PDF files are allowed.");
            return;
        }

        const originalButtonContent = submitButton.innerHTML;

        submitButton.disabled = true;
        submitButton.innerHTML =
            '<i class="fa-solid fa-spinner fa-spin"></i> '
            + loadingText;

        try {
            const formData = new FormData(form);

            const response = await fetch(form.action, {
                method: "POST",
                body: formData
            });

            const result = await response.json();

            if (form.id === "signForm") {
                displaySigningResult(resultBox, result);
            } else {
                displayVerificationResult(resultBox, result);
            }

        } catch (error) {
            showError(
                resultBox,
                "Unable to connect with server. Please check Tomcat and try again."
            );
        } finally {
            submitButton.disabled = false;
            submitButton.innerHTML = originalButtonContent;
        }
    }

    function displaySigningResult(resultBox, result) {
        if (!result.success) {
            showError(
                resultBox,
                result.message || "Document signing failed."
            );
            return;
        }

        resultBox.className = "result-card success-result";

        resultBox.innerHTML = `
            <div class="result-status success-status">
                <i class="fa-solid fa-circle-check"></i>
            </div>

            <div class="result-content">
                <h3>Document signed successfully</h3>

                <p>
                    <strong>Document:</strong>
                    ${escapeHtml(result.fileName)}
                </p>

                <p>
                    <strong>SHA-256 hash:</strong>
                </p>

                <div class="code-box">
                    ${escapeHtml(result.hash)}
                </div>

                <p>
                    <strong>RSA signature generated and stored in Oracle.</strong>
                </p>
            </div>

            <span class="verified-label">SIGNED</span>
        `;

        scrollToResult(resultBox);
    }

    function displayVerificationResult(resultBox, result) {
        if (!result.success) {
            showError(
                resultBox,
                result.message || "Document verification failed."
            );
            return;
        }

        if (!result.verified) {
            resultBox.className = "result-card failed-result";

            resultBox.innerHTML = `
                <div class="result-status error-status">
                    <i class="fa-solid fa-circle-xmark"></i>
                </div>

                <div class="result-content">
                    <h3>Document verification failed</h3>

                    <p>
                        <strong>Document:</strong>
                        ${escapeHtml(result.fileName || "Unknown")}
                    </p>

                    <p>
                        ${escapeHtml(result.message)}
                    </p>

                    <p class="verification-line">
                        Hash match:
                        <span class="status-false">
                            ${result.hashMatches ? "Yes" : "No"}
                        </span>
                    </p>

                    <p class="verification-line">
                        RSA signature valid:
                        <span class="status-false">
                            ${result.signatureValid ? "Yes" : "No"}
                        </span>
                    </p>
                </div>

                <span class="failed-label">NOT VERIFIED</span>
            `;

            scrollToResult(resultBox);
            return;
        }

        resultBox.className = "result-card success-result";

        resultBox.innerHTML = `
            <div class="result-status success-status">
                <i class="fa-solid fa-circle-check"></i>
            </div>

            <div class="result-content">
                <h3>Document verified successfully</h3>

                <p>
                    <strong>Document:</strong>
                    ${escapeHtml(result.fileName)}
                </p>

                <p>
                    The file hash matches and the RSA digital signature is valid.
                </p>

                <p class="verification-line">
                    Hash match:
                    <span class="status-true">Yes</span>
                </p>

                <p class="verification-line">
                    RSA signature valid:
                    <span class="status-true">Yes</span>
                </p>

                <p class="verification-line">
                    Signed at:
                    <span>${escapeHtml(result.signedAt || "Not available")}</span>
                </p>
            </div>

            <span class="verified-label">VERIFIED</span>
        `;

        scrollToResult(resultBox);
    }

    function showError(resultBox, message) {
        if (!resultBox) {
            return;
        }

        resultBox.className = "result-card failed-result";

        resultBox.innerHTML = `
            <div class="result-status error-status">
                <i class="fa-solid fa-circle-exclamation"></i>
            </div>

            <div class="result-content">
                <h3>Request failed</h3>
                <p>${escapeHtml(message)}</p>
            </div>

            <span class="failed-label">ERROR</span>
        `;

        scrollToResult(resultBox);
    }

    function scrollToResult(resultBox) {
        resultBox.scrollIntoView({
            behavior: "smooth",
            block: "center"
        });
    }

    function escapeHtml(value) {
        if (value === null || value === undefined) {
            return "";
        }

        const element = document.createElement("div");
        element.textContent = value;
        return element.innerHTML;
    }
});