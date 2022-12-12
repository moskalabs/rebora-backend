package moska.rebora.Common.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import moska.rebora.Common.Dto.S3Dto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class S3ServiceImpl implements S3Service{

    private final AmazonS3Client amazonS3Client;

    private final S3Dto s3Dto;

    private final AmazonS3 amazonS3;

    @Override
    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(s3Dto.getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String getFileUrl(String fileName) {
        return String.valueOf(amazonS3Client.getUrl(s3Dto.getBucket(), fileName));
    }

    @Override
    public void uploadThumbnail(File thumbnailFile, String filename) {
        amazonS3Client.putObject(new PutObjectRequest(s3Dto.getBucket(), filename, thumbnailFile).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public void deleteFile(String deletePath) {
        //boolean isExistObject = amazonS3.doesObjectExist("rebora", deletePath);
        amazonS3.deleteObject(new DeleteObjectRequest("rebora", deletePath));
    }

    @Override
    public ResponseEntity<byte[]> getObject(String storedFileName) throws IOException {
        S3Object o = amazonS3.getObject(new GetObjectRequest(s3Dto.getBucket(), storedFileName));
        S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(storedFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }
}
