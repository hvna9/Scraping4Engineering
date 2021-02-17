package com.claudiodimauro.Scrape4Engineering.api.resources;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.models.Pattern;
import com.claudiodimauro.Scrape4Engineering.api.services.EntityService;
import com.claudiodimauro.Scrape4Engineering.api.services.PatternService;
import java.io.IOException;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EntityScraprerWithoutPattern {
    private Pattern pattern;
    private EntityService entityService;
    private PatternService patternService;
    
    public EntityScraprerWithoutPattern(Pattern pattern, EntityService entityService, PatternService patternService) {
        this.pattern = pattern;
        this.entityService = entityService;
        this.patternService = patternService;
    }
    
    public void startScraping() throws Exception {
        scan();
    }

    private void scan() throws Exception {
        //qui serviva una else oltre alla if
        if (pattern.getUrl() == null) {
            throw new Exception("Pattern non valido");
        } 
        else {
            try {

                Document doc = Jsoup.connect(pattern.getUrl()).timeout(10000).get();

                Elements entityElements = doc.select(pattern.getTagForBody());
                for (Element entityElement : entityElements) {
                    Entity entity = new Entity();

                    Elements idElements = entityElement.select(pattern.getTagForEntityId());
                    if (!idElements.isEmpty()) {
                        entity.setEntityId(idElements.get(0).attr(pattern.getAttrForEntityId()));
                    }

                    Elements titleElements = entityElement.select(pattern.getTagForEntityTitle());
                    if (!titleElements.isEmpty()) {
                        if (pattern.getSelectorMethodForEntityTitle() == true) {//vere se eliminare ==
                            entity.setEntityTitle(titleElements.text());
                        } else {
                            entity.setEntityTitle(titleElements.get(0).attr(pattern.getAttrForEntityTitle()));
                        }
                    }

                    Elements pathElements = entityElement.select(pattern.getEntityPath());
                    if (!pathElements.isEmpty()) {
                        entity.setPath(pathElements.get(0).attr("href"));
                    }
                    // veder come fare a convertirlo in tipo date
                    Elements lastEntityUpdateElements = entityElement.select(pattern.getLastEntityUpdate());
                    if (!lastEntityUpdateElements.isEmpty()) {
                        entity.setLastUpdate(lastEntityUpdateElements.attr(pattern.getAttrLastEntityUpdate()));
                    }

                    Date date = new Date();
                    entity.setLastScraping(date);//da vedere se inserire nel controllo di inserimento

                    entity.setBasePath(pattern.getUrl());

                    entityService.create(entity);
                }
                patternService.create(pattern);
            } catch (IOException ex) {
                System.out.println("Catturata un'eccezione: \n" + ex.toString());
            }
        }
    }
}
