package com.soundify.server.shared.test;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@RequestMapping("/api/v1/test")
@RestController
public class TestController {
    private final StringRedisTemplate redisTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final ElasticsearchClient elasticsearchClient;
    private final JavaMailSender mailSender;
    private final MinioClient minioClient;


    @GetMapping("/redis")
    public String testRedis() {
        try {
            redisTemplate.opsForValue().set("testKey", "Hello Redis!");
            return "Redis is connected. Value: " + redisTemplate.opsForValue().get("testKey");
        } catch (Exception e) {
            return "Redis connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/mysql")
    public String testMySQL() {
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
            return conn.isValid(1) ? "MySQL is connected!" : "MySQL connection failed!";
        } catch (SQLException e) {
            return "MySQL connection error: " + e.getMessage();
        }
    }

    @GetMapping("/elasticsearch")
    public String testElasticsearch() {
        try {
            return elasticsearchClient.ping().value() ? "Elasticsearch is connected!" : "Elasticsearch connection failed!";
        } catch (Exception e) {
            return "Elasticsearch connection error: " + e.getMessage();
        }
    }

    @GetMapping("/minio")
    public String testMinio() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("test-bucket").build());
            return exists ? "MinIO is connected!" : "MinIO bucket does not exist!";
        } catch (RuntimeException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 ServerException | XmlParserException e) {
            return "MinIO connection error: " + e.getMessage();
        }
    }

    @GetMapping("/mail")
    public String testMail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo("test@example.com"); // Use a valid email
            helper.setSubject("Test Email");
            helper.setText("Mail server connection test.", true);
            return "Mail server is connected!";
        } catch (Exception e) {
            return "Mail connection error: " + e.getMessage();
        }
    }

    @GetMapping("/connection")
    public Map<String, String> testAllConnections() {
        Map<String, String> result = new HashMap<>();

        // Redis
        try {
            redisTemplate.opsForValue().set("testKey", "Hello Redis!");
            result.put("Redis", "Connected");
        } catch (Exception e) {
            result.put("Redis", "Failed: " + e.getMessage());
        }

        // MySQL
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            result.put("MySQL", conn.isValid(1) ? "Connected" : "Failed");
        } catch (SQLException e) {
            result.put("MySQL", "Failed: " + e.getMessage());
        }

        // Elasticsearch
        try {
            result.put("Elasticsearch", elasticsearchClient.ping().value() ? "Connected" : "Failed");
        } catch (Exception e) {
            result.put("Elasticsearch", "Failed: " + e.getMessage());
        }

        // MinIO
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("test-bucket").build());
            result.put("MinIO", exists ? "Connected" : "Bucket Not Found");
        } catch (RuntimeException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 ServerException | XmlParserException e) {
                result.put("MinIO", "Failed: " + e.getMessage());
            ;

        }

        // Mail
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo("test@example.com"); // Use a valid email
            helper.setSubject("Test Email");
            helper.setText("Mail server connection test.", true);
            result.put("Mail", "Configured");
        } catch (MessagingException e) {
            result.put("Mail", "Failed: " + e.getMessage());
        }


        return result;
    }
}


