package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.CofigJournalAppEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<CofigJournalAppEntity,String> {
}
