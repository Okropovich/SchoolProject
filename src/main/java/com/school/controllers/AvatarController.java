package com.school.controllers;

import com.school.models.Avatar;
import com.school.services.AvatarService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/{studentId}/upload")
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile file) {
        try {
            avatarService.uploadAvatar(studentId, file);
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload avatar: " + e.getMessage());
        }
    }

    @GetMapping
    public Page<Avatar> getAllAvatars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return avatarService.getAllAvatars(page, size);
    }
}