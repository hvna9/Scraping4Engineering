package com.claudiodimauro.Scrape4Engineering.api.resources;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.models.Pattern;
import com.claudiodimauro.Scrape4Engineering.api.services.EntityService;
import com.claudiodimauro.Scrape4Engineering.api.services.PatternService;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EntityScraperByPattern {

    private String patternId;
    private EntityService entityService;
    private PatternService patternService;

    public EntityScraperByPattern(String patternId, EntityService entityService, PatternService patternService) {
        this.patternId = patternId;
        this.entityService = entityService;
        this.patternService = patternService;
    }

    public String startScraping() throws Exception {
        return scan();
    }

    private String scan() {
        List<Pattern> patterns = patternService.getList();
        Pattern pattern = new Pattern();
        for (Pattern p : patterns) {
            String url = p.getUrl();
            if (url.equals(patternId)) {
                pattern = p;
            }
        }

        if (pattern.getUrl() == null) {
            return "Pattern con id: " + patternId + " non valido";
        } else {
            try {
                Document doc = Jsoup.connect(pattern.getUrl()).timeout(10000).get();
                if (pattern.getHasPreScraping()) {
                    Elements preScraping = doc.select(pattern.getTagForPreScraping()); //vede 
                    int numPag = 1;

                    for (Element pagination : preScraping) {
                        if (numPag == 1) {
                            for (Element entityElement : doc.select(pattern.getTagForBody())) {
                                Entity entity = new Entity();

                                Elements idElements = entityElement.select(pattern.getTagForEntityId());
                                if (!idElements.isEmpty()) {
                                    entity.setEntityId(idElements.get(0).attr(pattern.getAttrForEntityId()));
                                }

                                Elements titleElements = entityElement.select(pattern.getTagForEntityTitle());
                                if (!titleElements.isEmpty()) {
                                    if (pattern.getSelectorMethodForEntityTitle() == true) {//vedere se togliere ==
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
                                    try {
                                        if (pattern.getSelectorMethodForLastEntityUpdate() == true) {//vedere se togliere ==

                                            entity.setLastUpdate(lastEntityUpdateElements.text());
                                        } else {
                                            entity.setLastUpdate(lastEntityUpdateElements.get(0).attr(pattern.getAttrLastEntityUpdate()));
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(ex.toString());
                                    }
                                }

                                Date date = new Date();
                                entity.setLastScraping(date);//da vedere se inserire nel controllo di inserimento

                                entity.setBasePath(pattern.getUrl());

                                if (pattern.getHaveToExplore()) {
                                    // scrivere codice - SETTARE CONTENT
                                } else {
                                    Elements contentElements = entityElement.select(pattern.getTagForContent());
                                    if (!contentElements.isEmpty()) {
                                        entity.setContent(contentElements.text());
                                    }
                                }

                                //entity.setAttachmentId(entityService.findAttachments(param, param));
                                entityService.create(entity);
                            }
                        } else {
                            try {
                                Document doc2 = Jsoup.connect(pattern.getUrl() + pagination.attr("href")).timeout(10000).get();

                                for (Element entityElement : doc2.select(pattern.getTagForBody())) {
                                    Entity entity = new Entity();

                                    Elements idElements = entityElement.select(pattern.getTagForEntityId());
                                    if (!idElements.isEmpty()) {
                                        entity.setEntityId(idElements.get(0).attr(pattern.getAttrForEntityId()));
                                    }

                                    Elements titleElements = entityElement.select(pattern.getTagForEntityTitle());
                                    if (!titleElements.isEmpty()) {
                                        if (pattern.getSelectorMethodForEntityTitle() == true) {//vedere se togliere ==
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

                                    //entity.setAttachmentId(entityService.findAttachments(param, param));
                                    entityService.create(entity);
                                }
                            } catch (IOException ex) {
                                System.out.println("Catturata un'eccezione: \n" + ex.toString());
                            }
                        }
                        numPag++;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Catturata un'eccezione: \n" + ex.toString());
            }
        }
        return "Scraping effettuato con successo.";
    }
}
