package com.p5Project.cookIt.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.p5Project.cookIt.exceptions.ExternalIntegrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private static final String SECURE_URL_KEY = "secure_url";

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String assetFolder) {
        if (isEmpty(file)) {
            return null;
        }

        try {
            return upload(file, assetFolder);
        } catch (IOException e) {
            throw new ExternalIntegrationException("Erro ao fazer upload da imagem no Cloudinary", e);
        }
    }

    private boolean isEmpty(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    private String upload(MultipartFile file, String assetFolder) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("asset_folder", assetFolder)
        );

        Object secureUrl = result.get(SECURE_URL_KEY);
        return secureUrl != null ? secureUrl.toString() : null;
    }
}
