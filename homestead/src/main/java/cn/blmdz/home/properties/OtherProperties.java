package cn.blmdz.home.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="other")
public class OtherProperties {
    private Boolean devMode = Boolean.FALSE.booleanValue();
    private ThirdProperties third;

    private OOS oos;
    
    @Data
    public static class OOS {
        private String endpoint;
        private String accessKey;
        private String accessSecret;
        private String bucketName;
        private String imgDomain;
    }
}