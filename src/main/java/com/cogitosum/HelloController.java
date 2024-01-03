package com.cogitosum;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.apache.kafka.clients.Metadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/okta")
    public void jwt() {
        String clientSecret = "${clientSecret}"; // Or load from configuration
        String clientId="0oaggfxk2rJ0pdh6d4x7";
        //SecretKey  = Keys.hmacShaKeyFor(clientSecret.getBytes(StandardCharsets.UTF_8));
        SecretKey key= Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Instant now = Instant.now();

        String jwt = Jwts.builder()
                .setAudience("https://${yourOktaDomain}/oauth2/default/v1/token")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
                .setIssuer(clientId)
                .setSubject(clientId)
                .setId(UUID.randomUUID().toString())
                .signWith(key)
                .compact();
        System.out.println(jwt);

    }
    @RequestMapping("/pKafka")
    public void produceKafka(@RequestParam("topic") String topic) throws ExecutionException, InterruptedException {
        String bootStrapServers="127.0.0.1:29092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        KafkaProducer<String, String> producer= new KafkaProducer<String,String>(properties);
        //ProducerRecord<String,String> record = new ProducerRecord<String,String>("topic2", "key","value2");
        //ProducerRecord<String,String> record = new ProducerRecord<String,String>(topic, "key","value2");
        Random rand = new Random();
        int randomValue= (int)Math.round(Math.random());
        //System.out.println("RANDOM "+randomValue);
        ProducerRecord<String,String> record = new ProducerRecord<String,String>(topic,randomValue, "key","value2");
        Future<RecordMetadata> fMetaData=producer.send(record);
        RecordMetadata data= fMetaData.get();
        //System.out.println("HELLOCONTROLLER # "+data.topic()+" # "+data.partition()+" # "+data.offset());
        producer.flush();
        producer.close();
    }
}
