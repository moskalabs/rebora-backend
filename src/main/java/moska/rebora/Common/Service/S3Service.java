package moska.rebora.Common.Service;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.File;
import java.io.InputStream;

public interface S3Service {

    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);

    String getFileUrl(String fileName);

    void uploadThumbnail(File thumbnailFile, String filename);

    void deleteFile(String deletePath);
}
