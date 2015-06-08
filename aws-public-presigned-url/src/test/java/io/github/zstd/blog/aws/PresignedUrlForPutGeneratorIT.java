package io.github.zstd.blog.aws;


import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assume.assumeNotNull;

public class PresignedUrlForPutGeneratorIT {

    private static final Logger LOG = LoggerFactory.getLogger(PresignedUrlForPutGeneratorIT.class);

    private S3Options s3Options;


    @Before
    public void setUp() throws Exception {
        s3Options = parseS3Options();

    }

    private S3Options parseS3Options() {
        Properties properties = new Properties();
        S3Options result = null;
        try {
            properties.load(this.getClass().getResourceAsStream("/aws.properties"));
            result = new S3Options(properties.getProperty("bucket"),
                                        properties.getProperty("access-id"),
                                        properties.getProperty("client-secret"));
            LOG.info("Loaded options: {}", result);
        } catch (IOException e) {
            LOG.error("Failed to load aws.properties. Tests will be skipped.");
        }
        return result;
    }


    @Test
    public void testPutResourceAsPublic() throws Exception {
        assumeNotNull(s3Options);

    }

    @Test
    public void testPutResourceAsPrivate() throws Exception {
        assumeNotNull(s3Options);

    }

}