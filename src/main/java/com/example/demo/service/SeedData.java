package com.example.demo.service;

import com.example.demo.model.Claim;
import com.example.demo.model.User;
import com.example.demo.model.UserClaim;
import com.example.demo.repository.IClaimRepository;
import com.example.demo.repository.IUserClaimRepository;
import com.example.demo.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class SeedData implements CommandLineRunner {
    private final IClaimRepository claimRepository;
    private final IUserRepository userRepository;
    private final IUserClaimRepository userClaimRepository;

    @Override
    public void run(String... args) throws Exception {
        seedClaim("ROLE_admin");
        seedClaim("ROLE_customer");
        seedAdmin();
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
    @Transactional
    private void seedAdmin() throws Exception {
        CompletableFuture<Optional<User>> result = userRepository.findByUsername("admin");
        boolean isExists = result.join().isPresent();
        if(!isExists){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin1");
            admin.setEmail("admin@gmail.com");
            userRepository.save(admin);
            Optional<Claim> adminClaim= claimRepository.findByClaimTypeAndClaimValue("role","ROLE_admin");
            if (adminClaim.isPresent()){
                UserClaim userClaim = new UserClaim();
                userClaim.setUserId(admin.getId());
                userClaim.setClaimId(adminClaim.get().getId());
                userClaimRepository.save(userClaim);
            }
            else{
                throw new Exception();
            }
        }
    }
}
