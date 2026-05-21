package com.school.services;

import com.school.models.Avatar;
import com.school.models.Student;
import com.school.repositories.AvatarRepository;
import com.school.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar for student id: {}", studentId);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            logger.error("Student with id {} not found for avatar upload", studentId);
            return;
        }
        logger.debug("Uploading file: {}, size: {} bytes", file.getOriginalFilename(), file.getSize());
        Avatar avatar = new Avatar();
        avatar.setFilePath("avatars/" + studentId + "/" + file.getOriginalFilename());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);
        avatarRepository.save(avatar);
        logger.info("Avatar saved for student id: {}", studentId);
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars with pagination: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Avatar> avatars = avatarRepository.findAll(pageable);
        logger.debug("Found {} avatars total", avatars.getTotalElements());
        return avatars;
    }

    public Avatar findById(Long id) {
        logger.info("Was invoked method for find avatar by id: {}", id);
        Avatar avatar = avatarRepository.findById(id).orElse(null);
        if (avatar == null) {
            logger.error("Avatar with id {} not found", id);
        }
        return avatar;
    }
}