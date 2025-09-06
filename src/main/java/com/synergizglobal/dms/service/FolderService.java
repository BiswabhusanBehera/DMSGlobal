package com.synergizglobal.dms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.synergizglobal.dms.dto.FolderDTO;
import com.synergizglobal.dms.dto.SubFolderDTO;
import com.synergizglobal.dms.entity.Folder;
import com.synergizglobal.dms.entity.SubFolder;
import com.synergizglobal.dms.repository.FolderRepository;

@Service
public class FolderService {

    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public List<FolderDTO> getAllFolders() {
        return folderRepository.findAll().stream().map(folder -> new FolderDTO(
                folder.getId(),
                folder.getName(),
                folder.getSubFolders().stream()
                        .map(sf -> new SubFolderDTO(sf.getId(), sf.getName()))
                        .collect(Collectors.toList())
        )).collect(Collectors.toList());
    }

    public FolderDTO getFolderById(Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        return new FolderDTO(
                folder.getId(),
                folder.getName(),
                folder.getSubFolders().stream()
                        .map(sf -> new SubFolderDTO(sf.getId(), sf.getName()))
                        .collect(Collectors.toList())
        );
    }

    public FolderDTO createFolder(FolderDTO folderDTO) {
        Folder folder = new Folder(folderDTO.getName());
        if (folderDTO.getSubFolders() != null) {
            folderDTO.getSubFolders().forEach(sf ->
                    folder.getSubFolders().add(new SubFolder(sf.getName(), folder)));
        }
        Folder saved = folderRepository.save(folder);
        return getFolderById(saved.getId());
    }

    public FolderDTO updateFolder(Long id, FolderDTO folderDTO) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        // Update folder name
        folder.setName(folderDTO.getName());
        // Clear existing sub-folders and add new ones
        folder.getSubFolders().clear();
        if (folderDTO.getSubFolders() != null) {
            folderDTO.getSubFolders().forEach(sf ->
                    folder.getSubFolders().add(new SubFolder(sf.getName(), folder)));
        }
        Folder updated = folderRepository.save(folder);
        return getFolderById(updated.getId());
    }

    public void deleteFolder(Long id) {
        folderRepository.deleteById(id);
    }
}
