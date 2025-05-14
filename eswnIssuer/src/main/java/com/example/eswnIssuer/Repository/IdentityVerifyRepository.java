package com.example.eswnIssuer.Repository;

import com.example.eswnIssuer.Entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentityVerifyRepository extends JpaRepository<Identity,Integer> {
    Optional<Identity> findByStudentIdAndAgeAndStudentNameAndUniversity(int studentId, int age, String studentName, String university);
}
