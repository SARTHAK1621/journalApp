package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;
    private static final Logger logger= LoggerFactory.getLogger(JournalEntryService.class);
    @Transactional
    public void saveJournalentry(JournalEntry journalEntry, String userName){
        try {
            User user=userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved=journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            //logger.info();
            userService.saveUser(user);
        }catch(Exception e){
            log.error("Exception",e);
            logger.error("Exception",e);
            throw new RuntimeException("Error saving journal entry", e);
        }
    }
    public void saveJournalentry(JournalEntry journalEntry){
        try {
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved=journalEntryRepository.save(journalEntry);
        }catch(Exception e){
            log.error("Exception",e);
        }
    }
    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }
    @Transactional
    public boolean deleteById(ObjectId id, String userName){
        boolean removed=false;
        try {
            User user=userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().toString().equals(id.toString()));
            if(removed){
                journalEntryRepository.deleteById(id);
                userService.saveUser(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return removed;
    }
//    public List<JournalEntry> findByUserName(String userName){
//
//    }
}
