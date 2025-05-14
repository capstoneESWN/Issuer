package com.example.eswnIssuer.Controller;

import com.example.eswnIssuer.DTO.IdentityVerifyDTO;
import com.example.eswnIssuer.Service.IdentityVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdentityVerificationController {
    private final IdentityVerificationService identityVerificationService;

    IdentityVerificationController(IdentityVerificationService identityVerificationService) {
        this.identityVerificationService = identityVerificationService;
    }

    @PostMapping("/identityverify")
    public ResponseEntity identityverify(@RequestBody IdentityVerifyDTO identityVerifyDTO) {

        boolean result = identityVerificationService.identityVerify(identityVerifyDTO);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
