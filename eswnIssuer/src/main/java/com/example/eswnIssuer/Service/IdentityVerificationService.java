package com.example.eswnIssuer.Service;

import com.example.eswnIssuer.DTO.IdentityVerifyDTO;
import com.example.eswnIssuer.Entity.Identity;
import com.example.eswnIssuer.Repository.IdentityVerifyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdentityVerificationService {
    private final IdentityVerifyRepository identityVerifyRepository;
    public IdentityVerificationService(IdentityVerifyRepository identityVerifyRepository) {
        this.identityVerifyRepository = identityVerifyRepository;
    }
    public boolean identityVerify(IdentityVerifyDTO dto) {
        // 입력값과 일치하는 레코드를 찾음
        Optional<Identity> optionalIdentity = identityVerifyRepository.findByStudentIdAndAgeAndStudentNameAndUniversity(
                dto.getStudentId(),
                dto.getAge(),
                dto.getStudentName(),
                dto.getUniversity()
        );

        if (optionalIdentity.isPresent()) {
            Identity identity = optionalIdentity.get();

            // is_used가 true라면 이미 인증된 사용자 → 인증 거부
            if (identity.isUsed()) {
                return false;
            }

            // 아직 사용되지 않은 사용자 → 인증 성공 + is_used = true로 업데이트
            // identity.setUsed(true); // 또는 setIsUsed(true); 필드명에 따라 다름
            // identityVerifyRepository.save(identity);

            return true;
        }

        // 해당하는 사용자 정보 없음 → 인증 실패
        return false;
    }
}
