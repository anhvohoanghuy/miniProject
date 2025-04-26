package com.example.demo.controller;

import com.example.demo.dto.ClaimDto;
import com.example.demo.model.Claim;
import com.example.demo.repository.IClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final IClaimRepository claimRepository;

    @GetMapping
    public List<Claim> getAllClaims(){
        return claimRepository.findAll();
    }

    @PostMapping
    public void addClaim(@RequestBody ClaimDto model){
        Claim claim = new Claim();
        claim.setClaimType(model.claimType);
        claim.setClaimValue(model.claimValue);
        claimRepository.save(claim);
    }
}
