package com.example.demo.service;

import com.example.demo.model.Claim;
import com.example.demo.repository.IClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClaimSeed implements CommandLineRunner {
    private final IClaimRepository claimRepository;

    @Override
    public void run(String... args) throws Exception {
        seedClaim("ROLE_admin");
        seedClaim("ROLE_customer");
    }
    private void seedClaim(String value){
        boolean isExists = claimRepository.findByClaimTypeAndClaimValue("role", value).isPresent();
        if(!isExists){
            Claim claim = new Claim();
            claim.setClaimType("role");
            claim.setClaimValue(value);
            claimRepository.save(claim);
            System.out.println("Seeded claim: " + "role" + " - " + value);
        }
    }
}
