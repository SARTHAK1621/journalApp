package net.engineeringdigest.journalApp.cache;
import net.engineeringdigest.journalApp.entity.CofigJournalAppEntity;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {
    public Map<String,String> appCache;
    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    @PostConstruct
    public void init(){
        appCache=new HashMap<>();
        List<CofigJournalAppEntity> all = configJournalAppRepository.findAll();
        for(CofigJournalAppEntity curr:all){
            appCache.put(curr.getKey(),curr.getValue());
        }
        System.out.println(appCache);
    }
}
