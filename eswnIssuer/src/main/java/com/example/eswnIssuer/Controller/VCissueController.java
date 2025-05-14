package com.example.eswnIssuer.Controller;

import com.example.eswnIssuer.DTO.IdentityVerifyDTO;
import com.example.eswnIssuer.Service.VCIssueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class VCissueController {

    private final VCIssueService vcIssueService;

    public VCissueController(VCIssueService vcIssueService) {
        this.vcIssueService = vcIssueService;
    }

    @PostMapping("/issueVC")
    public ResponseEntity VCIssue(@RequestBody IdentityVerifyDTO identityVerifyDTO) {
        System.out.println("VC 발급 요청됨");
        System.out.println(identityVerifyDTO.getAge());
        System.out.println(identityVerifyDTO.getStudentId());
        System.out.println(identityVerifyDTO.getStudentName());
        System.out.println(identityVerifyDTO.getUniversity());
        try {
            String vcJson = vcIssueService.issueVC(identityVerifyDTO);
            return ResponseEntity.ok(vcJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("VC 발급 실패: " + e.getMessage());
        }
    }
}
