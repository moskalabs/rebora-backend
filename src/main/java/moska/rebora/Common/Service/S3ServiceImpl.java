package moska.rebora.Common.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import moska.rebora.Common.Dto.S3Dto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

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
}
