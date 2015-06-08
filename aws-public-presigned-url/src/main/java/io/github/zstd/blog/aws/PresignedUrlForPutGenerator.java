package io.github.zstd.blog.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.util.Throwables;
import org.apache.http.HttpHeaders;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Generates presigned url for uploading file to Amazon S3.
 */
public class PresignedUrlForPutGenerator {

    public String generate(S3Options s3Options, GenerationParams params) {
        String bucket = s3Options.getBucket();
        long millis = Calendar.getInstance().getTimeInMillis();
        Date expires = new Date(millis + params.getExpirationTimeMillis());
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, params.getResourceKey());
        generatePresignedUrlRequest
                .withMethod(HttpMethod.PUT)
                .withExpiration(expires)
                .withContentType(params.getContentType())
                .addRequestParameter(HttpHeaders.CONTENT_TYPE, params.getContentType());
        // this parameter needed to make resource uploaded with presigned-url immediately public-available
        if(params.isPublicResource()) {
            generatePresignedUrlRequest.addRequestParameter("x-amz-acl","public-read");
        }

        URL result = createS3Client(s3Options).generatePresignedUrl(generatePresignedUrlRequest);
        try {
            return result.toURI().toString();
        } catch (URISyntaxException ex) {
            throw Throwables.failure(ex);
        }
    }

    private AmazonS3Client createS3Client(S3Options s3Options) {
        return new AmazonS3Client(
                new StaticCredentialsProvider(
                        new BasicAWSCredentials(s3Options.getAccessId(), s3Options.getSecretKey())));
    }

}
