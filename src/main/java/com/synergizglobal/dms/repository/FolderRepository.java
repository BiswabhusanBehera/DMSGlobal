package com.synergizglobal.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synergizglobal.dms.entity.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

}
