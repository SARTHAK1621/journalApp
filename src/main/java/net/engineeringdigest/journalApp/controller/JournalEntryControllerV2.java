package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    @Autowired
     private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;
    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user=userService.findByUserName(userName);
        if(user==null){
            return new ResponseEntity<>("No user is there",HttpStatus.NO_CONTENT);
        }
        List<JournalEntry> journalEntries=user.getJournalEntries();
        if(journalEntries!=null && !journalEntries.isEmpty()){
            return new ResponseEntity<>(journalEntries,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String userName= authentication.getName();
            journalEntryService.saveJournalentry(journalEntry,userName);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable("myId") ObjectId id) {
        // Retrieve authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        // Fetch user from the database
        User user = userService.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // User not authenticated or found
        }

        // Check if the journal entry belongs to the authenticated user
        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
        if (journalEntry.isPresent() &&
                user.getJournalEntries() != null &&
                user.getJournalEntries().contains(journalEntry.get())) {

            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }

        // Return 403 if journal entry exists but doesn't belong to the user
        if (journalEntry.isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Return 404 if no journal entry found
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable("myId") ObjectId id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        boolean removed=journalEntryService.deleteById(id,userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("id/{id}")
    public ResponseEntity<?> updateById(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry newEntry){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user=userService.findByUserName(userName);
        List<JournalEntry> collect=user.getJournalEntries().stream().filter(x->x.getId().toString().equals(id.toString())).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> oldEntry=journalEntryService.findById(id);
            if(oldEntry.isPresent()) {
                JournalEntry old=oldEntry.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getTitle().equals("") ? newEntry.getContent() : old.getContent());
                journalEntryService.saveJournalentry(old);
            }
            return new ResponseEntity<>(oldEntry,HttpStatus.OK);
        }


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
