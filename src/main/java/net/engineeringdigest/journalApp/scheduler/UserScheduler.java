package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;
    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron="0 0 9 * * SUN")
   //@Scheduled(cron="0 * * ? * *")-> for every second
    public void fetchUsersAndSendSaMail(){
        List<User> users = userRepository.getuserForSA();
        System.out.println(users.size());
        for(User user:users){
            List<JournalEntry> journalEntries=user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().
                    isAfter(LocalDateTime.now().minus(14, ChronoUnit.DAYS))).
                    map(x-> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCounts=new HashMap<>();
            for(Sentiment sentiment:sentiments){
                if(sentiment!=null){
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment mostFrequentSentiment=null;

            int maxCount=0;
            for(Map.Entry<Sentiment,Integer> entry:sentimentCounts.entrySet()){
                if(entry.getValue()>maxCount){
                    maxCount= entry.getValue();
                    mostFrequentSentiment=entry.getKey();
                }
            }
            System.out.println(mostFrequentSentiment);
            if(mostFrequentSentiment!=null){
                SentimentData sentimentData=SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days "+ mostFrequentSentiment).build();
                System.out.println("KafkaTemplate instance: " + kafkaTemplate);
                try {
                    //kafkaTemplate.send("weekly-sentiments",sentimentData.getEmail(),sentimentData);
                    throw new RuntimeException("Testing for kafka fallback");
                } catch (Exception e) {
                    emailService.sendEmail(sentimentData.getEmail(),"Sentiment For Last 7 Days",mostFrequentSentiment.toString());
                }
                //emailService.sendEmail(user.getEmail(),"Sentiment For Last 7 Days",mostFrequentSentiment.toString());
            }
        }
    }
    @Scheduled(cron = "0 0/50 * ? * *")
    public void clearAppCache(){
       appCache.init();
    }
}
