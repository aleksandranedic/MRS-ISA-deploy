package com.project.team9.service;

import com.project.team9.model.Image;
import com.project.team9.repo.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class ImageService {
    private final ImageRepository imageRepository;

    final String STATIC_PATH = "src/main/resources/static/";
    final String STATIC_PATH_TARGET = "target/classes/static/";

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getImages() {
        return imageRepository.findAll();
    }

    public Image getById(String id) {
        return imageRepository.getById(Long.parseLong(id));
    }

    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public Optional<Image> getImageByPath(String path) {
        return imageRepository.findByPath(path);
    }

    public List<Image> getImages(List<String> paths) {
        List<Image> images = new ArrayList<Image>();
        for (String path : paths) {
            Optional<Image> optImg = this.getImageByPath(path);
            Image img;
            img = optImg.orElseGet(() -> new Image(path));
            this.save(img);
            images.add(img);
        }
        return images;
    }

    public void savePicturesOnPath(Long id,
                                   MultipartFile[] multipartFiles,
                                   List<String> paths,
                                   Path path,
                                   String imagesPath) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        for (MultipartFile mpf : multipartFiles) {
            String fileName = mpf.getOriginalFilename();
            try (InputStream inputStream = mpf.getInputStream()) {
                assert fileName != null;
                Path filePath = path.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                paths.add(imagesPath + id + "/" + fileName);
            } catch (DirectoryNotEmptyException ignored) {
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        }
    }

    public List<String> saveImages(Long id,
                                    MultipartFile[] multipartFiles,
                                    String imagesPath,
                                    List<Image> images) throws IOException {
        List<String> paths = new ArrayList<>();
        if (multipartFiles == null) {
            return paths;
        }
        Path path = Paths.get(STATIC_PATH + imagesPath + id);
        Path path_target = Paths.get(STATIC_PATH_TARGET + imagesPath + id);
        savePicturesOnPath(id, multipartFiles, paths, path, imagesPath);
        savePicturesOnPath(id, multipartFiles, paths, path_target, imagesPath);
        if (images != null && images.size() > 0){
            for (Image image : images) {
                paths.add(image.getPath());
            }
        }
        return paths.stream().distinct().collect(Collectors.toList());
    }
}

