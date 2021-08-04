package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.api.RegisterApi;
import com.backbase.oss.boat.bay.model.RegisterProject;
import com.backbase.oss.boat.bay.repository.BoatPortalRepository;
import com.backbase.oss.boat.bay.repository.BoatSourceRepository;
import com.backbase.oss.boat.bay.source.SpecSourceResolver;
import com.backbase.oss.boat.bay.source.scanner.SpecSourceScanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;


@RestController
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RegisterController implements RegisterApi {


    private final BoatPortalRepository boatPortalRepository;
    private final BoatSourceRepository boatSourceRepository;
    private final SpecSourceResolver specSourceResolver;
    final Set<SpecSourceScanner> scanners = new HashSet<>();


    @Override
    public ResponseEntity<Void> registerProject(RegisterProject registerProject) {

        return null;
    }
}
