package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;
    // this method continously serching if any data came or not
    @KafkaListener(topics="weekly-sentiments",groupId="weekly-sentiment-group")
    public void consume(SentimentData sentimentData){
        System.out.println("Received Kafka message: " + sentimentData);
        sendEmail(sentimentData);}

    public void sendEmail(SentimentData sentimentData){
        emailService.sendEmail(sentimentData.getEmail(), "Sentiment For previous week", sentimentData.getSentiment());
    }


}
