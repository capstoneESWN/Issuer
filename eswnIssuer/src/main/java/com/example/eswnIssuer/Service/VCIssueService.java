package com.example.eswnIssuer.Service;

import com.example.eswnIssuer.DTO.IdentityVerifyDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class VCIssueService {

    private final String publicKeyBase64 = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANg0lLGt/dSEyinKFHa1EkGHt6pBxmGd+m5nV+MnLl/M+F368zDYAxZt4MmMoV/8FBGgLOKiXpI+gddD5WTmXvECAwEAAQ==";
    private final String privateKeyBase64 = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEA2DSUsa391ITKKcoUdrUSQYe3qkHGYZ36bmdX4ycuX8z4XfrzMNgDFm3gyYyhX/wUEaAs4qJekj6B10PlZOZe8QIDAQABAkADx2t/7YwdvlJwR41zA7g1eANQUQUAKMw7SMgi+sjXOMw0727y5TXHZ3MYq/5jwZcG3oN+U6edtAuhcHLCvWwpAiEA9kCzmMRuCyTC3uwDT56TzJ6RMqtMAvqsQ/FgrPNyztUCIQDgw2g7LJLwUfAs29cT6BMRmWB3vNXeI1Lr4hIbdcS1rQIhANcLE4tR5kNG/AIOGqoZ8jnbMzMLUdq8K1k93c3K3zRtAiBBqZSnxOvgfW+XC1qYHDKF77L5CBfK37L36oGzuAIRuQIhAILrIgOlMGYUZahiDiH+sRhE127rmM9Aa4sDAgaiPJjH";

    public String issueVC(IdentityVerifyDTO request) throws Exception {
        String issuedAt = Instant.now().toString();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", "VerifiableCredential");
        data.put("credentialSubject", Map.of("age", request.getAge()));
        data.put("issuedAt", issuedAt);


        String jsonData = new ObjectMapper().writeValueAsString(data);

        PrivateKey privateKey = importPrivateKey(privateKeyBase64);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(jsonData.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();

        String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);

        Map<String, Object> vc = new LinkedHashMap<>(data);
        vc.put("proof", Map.of(
                "type", "RSASignature",
                "created", issuedAt,
                "proofPurpose", "assertionMethod",
                "verificationMethod", "ExampleVerificationMethod",
                "signature", signatureBase64
        ));

        return new ObjectMapper().writeValueAsString(vc);
    }

    private PrivateKey importPrivateKey(String base64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey importPublicKey(String base64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

}