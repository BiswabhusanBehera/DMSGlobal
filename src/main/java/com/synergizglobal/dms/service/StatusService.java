package com.synergizglobal.dms.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.synergizglobal.dms.dto.StatusDTO;
import com.synergizglobal.dms.entity.Status;
import com.synergizglobal.dms.repository.StatusRepository;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<StatusDTO> getAllStatuses() {
        return statusRepository.findAll().stream()
                .map(status -> new StatusDTO(status.getId(), status.getName()))
                .collect(Collectors.toList());
    }

    public StatusDTO getStatusById(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));
        return new StatusDTO(status.getId(), status.getName());
    }

    public StatusDTO createStatus(StatusDTO statusDTO) {
        if (statusRepository.existsByName(statusDTO.getName())) {
            throw new RuntimeException("Status already exists");
        }
        Status status = new Status(statusDTO.getName());
        Status savedStatus = statusRepository.save(status);
        return new StatusDTO(savedStatus.getId(), savedStatus.getName());
    }

    public StatusDTO updateStatus(Long id, StatusDTO statusDTO) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));
        status.setName(statusDTO.getName());
        Status updatedStatus = statusRepository.save(status);
        return new StatusDTO(updatedStatus.getId(), updatedStatus.getName());
    }

    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }
}

