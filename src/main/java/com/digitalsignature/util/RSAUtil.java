package com.digitalsignature.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    private static final String ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private static final Path KEY_DIRECTORY =
            Paths.get(
                    System.getProperty("user.home"),
                    "digital-signature-keys"
            );

    private static final Path PRIVATE_KEY_FILE =
            KEY_DIRECTORY.resolve("private.key");

    private static final Path PUBLIC_KEY_FILE =
            KEY_DIRECTORY.resolve("public.key");

    private static KeyPair keyPair;

    static {
        try {
            keyPair = loadOrCreateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(
                    "RSA keys could not be initialized.",
                    e
            );
        }
    }

    private RSAUtil() {
    }

    private static KeyPair loadOrCreateKeyPair()
            throws Exception {

        if (Files.exists(PRIVATE_KEY_FILE)
                && Files.exists(PUBLIC_KEY_FILE)) {

            byte[] privateKeyBytes =
                    Files.readAllBytes(PRIVATE_KEY_FILE);

            byte[] publicKeyBytes =
                    Files.readAllBytes(PUBLIC_KEY_FILE);

            KeyFactory keyFactory =
                    KeyFactory.getInstance(ALGORITHM);

            PrivateKey privateKey =
                    keyFactory.generatePrivate(
                            new PKCS8EncodedKeySpec(
                                    privateKeyBytes
                            )
                    );

            PublicKey publicKey =
                    keyFactory.generatePublic(
                            new X509EncodedKeySpec(
                                    publicKeyBytes
                            )
                    );

            return new KeyPair(publicKey, privateKey);
        }

        KeyPairGenerator generator =
                KeyPairGenerator.getInstance(ALGORITHM);

        generator.initialize(2048);

        KeyPair generatedKeyPair =
                generator.generateKeyPair();

        saveKeyPair(generatedKeyPair);

        return generatedKeyPair;
    }

    private static void saveKeyPair(KeyPair pair)
            throws IOException {

        Files.createDirectories(KEY_DIRECTORY);

        Files.write(
                PRIVATE_KEY_FILE,
                pair.getPrivate().getEncoded()
        );

        Files.write(
                PUBLIC_KEY_FILE,
                pair.getPublic().getEncoded()
        );
    }

    public static String sign(byte[] documentBytes)
            throws Exception {

        PrivateKey privateKey =
                keyPair.getPrivate();

        Signature signature =
                Signature.getInstance(
                        SIGNATURE_ALGORITHM
                );

        signature.initSign(privateKey);
        signature.update(documentBytes);

        byte[] signedData = signature.sign();

        return Base64.getEncoder()
                .encodeToString(signedData);
    }

    public static boolean verify(
            byte[] documentBytes,
            String signatureText)
            throws Exception {

        if (signatureText == null
                || signatureText.trim().isEmpty()) {

            return false;
        }

        PublicKey publicKey =
                keyPair.getPublic();

        Signature signature =
                Signature.getInstance(
                        SIGNATURE_ALGORITHM
                );

        signature.initVerify(publicKey);
        signature.update(documentBytes);

        byte[] signatureBytes =
                Base64.getDecoder()
                        .decode(signatureText);

        return signature.verify(signatureBytes);
    }
}