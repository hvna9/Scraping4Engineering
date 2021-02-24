package com.claudiodimauro.Scrape4Engineering.api.resources;

import com.claudiodimauro.Scrape4Engineering.api.models.AttachmentObject;
import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.models.Pattern;
import com.claudiodimauro.Scrape4Engineering.api.models.PatternObject;
import com.claudiodimauro.Scrape4Engineering.api.services.EntityService;
import com.claudiodimauro.Scrape4Engineering.api.services.PatternService;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EntityScraprerByNewPattern {
    private final int TIMER = 60000;//ms

    private Pattern pattern;
    private EntityService entityService;
    private PatternService patternService;

    public EntityScraprerByNewPattern(Pattern pattern, EntityService entityService, PatternService patternService) {
        this.pattern = pattern;
        this.entityService = entityService;
        this.patternService = patternService;
    }

    public String startScraping() throws Exception {
        /*Il pattern passato come JSON viene sempre creato e salvato nel DB. In caso di pattern con id già presente, si effettua l'aggiornamento.
        Anche in caso di pattern errato, la creazione avviene ma non sarà garantito il successo dello scraping.
         */
        patternService.create(pattern);
        return scan();
    }

    private String scan() throws Exception {
        //qui serviva una else oltre alla if
        if (pattern.getUrl() == null) {
            throw new Exception("Pattern non valido");
        } else {
            try {
                Document doc = Jsoup.connect(pattern.getUrl()).timeout(TIMER).get();
                System.out.println("URL DI CONNESSIONE: " + pattern.getUrl());
                System.out.println("BASE URI: " + doc.baseUri());

                if (pattern.getHasPrescraping()) {
                    Elements prescraping = doc.select(pattern.getTagForPrescraping());
                    int numPag = 1;
                    for (Element pagination : prescraping) {
                        if (numPag == 1) {
                            Elements elem = doc.select(pattern.getTagForBody());
                            paginationScrape(elem, pattern);
                        } else {
                            try {
                                System.out.println("PAGINATION: "+ pagination.attr("href").toString());
                                if (!pagination.attr("href").substring(0, 4).equals("http")) {
                                    int i = 0, j = 0;
                                    do {
                                        j = pattern.getUrl().indexOf("/", j) + 1;
                                        i++;
                                    } while (i <= 2);
                                    String url = pattern.getUrl().substring(0, j - 1);
                                    
                                    

                                    Document doc2 = Jsoup.connect(url + pagination.attr("href")).timeout(TIMER).get();
                                    paginationScrape(doc2.select(pattern.getTagForBody()), pattern);
                                } else {
                                    Document doc2 = Jsoup.connect(pagination.attr("href")).timeout(TIMER).get();
                                    paginationScrape(doc2.select(pattern.getTagForBody()), pattern);
                                }
                            } catch (Exception ex) {
                                System.out.println("ERRORE 01");
                                ex.printStackTrace();
                            }
                        }
                        numPag++;
                    }
                } else {
                    Elements elem = doc.select(pattern.getTagForBody());
                    System.out.println("ERRORE tag for body\n\nELEM SIZE" + elem.size());
                    paginationScrape(elem, pattern);
                    System.out.println("ERRORE DOPO tag for body");
                }

            } catch (Exception ex) {
                System.out.println("ERRORE 02");
                ex.printStackTrace();
            }
        }
        return "";
    }

    private void paginationScrape(Elements pagination, Pattern pattern) throws IOException {

        int i = 0, j = 0;
        do {
            j = pattern.getUrl().indexOf("/", j) + 1;
            i++;
        } while (i <= 2);
        String url = pattern.getUrl().substring(0, j - 1);

        for (Element elem : pagination) {
            Entity entity = new Entity();

            try {
                Elements idElement = elem.select(pattern.getEntityId());
                if (!idElement.isEmpty()) {
                    entity.setEntityId(idElement.get(0).attr(pattern.getAttrForEntityId()));
                }
            } catch (Exception ex) {
                System.out.println("ERRORE ID");
            }

            try {
                Elements pathElement = elem.select(pattern.getEntityPath());
                if (!pathElement.isEmpty()) {
                    entity.setPath(pathElement.get(0).attr("href"));
                }
            } catch (Exception ex) {
                System.out.println("ERRORE PATH");
            }

            List<PatternObject> ptnObjs = pattern.getPatternObjects();
            if (!pattern.getPatternObjects().isEmpty()) {
                for (PatternObject po : ptnObjs) {
                    try {
                        Elements element = elem.select(po.getTagForElementToScrape());
                        if (!element.isEmpty()) {
                            if (po.getMethodForElementToScrape() == true) {
                                entity.setEntityObject(po.getElementToScrape(), element.text());
                            } else {
                                entity.setEntityObject(po.getElementToScrape(), element.get(0).attr(po.getAttrForElementToScrape()));
                            }
                        }
                    } catch (Exception exe) {
                        System.out.println("ERRORE 03"); //da cambiare
                    }
                }
            }

            try {
                if (pattern.getHasAttachments()) {
                    if (!pattern.getAttachmentObject().isEmpty()) {
                        for (AttachmentObject ao : pattern.getAttachmentObject()) {

                            Elements element = elem.select(ao.getTagForElementToScrape());
                            if (!element.isEmpty()) {
                                /*Se attachmentlink inizia per http, allora attachmentLink è già il link definitivo da usare per scaricare
                            se invece non inizia per http ha bisogno del basePath*/
                                String attachmentLink = element.get(0).attr(ao.getAttrForElementToScrape());
                                if (!attachmentLink.substring(0, 4).equals("http")) {
                                    entity.setAttachmentIds(entityService.findAttachment(entity.getBasePath() + attachmentLink, entity.getBasePath() + entity.getPath(), entity.getBasePath()));
                                } else {
                                    entity.setAttachmentIds(entityService.findAttachment(attachmentLink, entity.getBasePath() + entity.getPath(), entity.getBasePath()));
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("ERRORE 04");
                ex.printStackTrace();
            }

            Date date = new Date();
            entity.setLastScraping(date);
            entity.setBasePath(url);

            
            if (pattern.getHaveToExplore()) {
                System.out.println("ARRIVO QUIiiii?");
                if (!entity.getPath().substring(0, 4).equals("http")) {
                    Document doc2 = Jsoup.connect(url + entity.getPath()).timeout(TIMER).get();
                    innerScrape(doc2.select(pattern.getTagForInnerBody()), pattern, entity);
                } else {
                    System.out.println("ARRIVO QUI?");
                    System.out.println(entity.getPath());
                    Document doc2 = Jsoup.connect(entity.getPath()).timeout(TIMER).get();
                    innerScrape(doc2.select(pattern.getTagForInnerBody()), pattern, entity);
                }
            } else {
                entityService.updateScraping(entity, url);
                System.out.println("Sono uscito dalla save");
            }
        }
    }

    private void innerScrape(Elements pagination, Pattern pattern, Entity entity) {
        for (Element elem : pagination) {
            List<PatternObject> ptnObjs = pattern.getInnerPatternObjects();
            if (!pattern.getPatternObjects().isEmpty()) {
                for (PatternObject po : ptnObjs) {
                    try {
                        Elements element = elem.select(po.getTagForElementToScrape());
                        if (!element.isEmpty()) {
                            if (po.getMethodForElementToScrape() == true) {
                                entity.setContent(po.getElementToScrape(), element.text());
                            } else {
                                entity.setContent(po.getElementToScrape(), element.get(0).attr(po.getAttrForElementToScrape()));
                            }
                        }
                    } catch (Exception exe) {
                        System.out.println("ERRORE 05"); //da cambiare
                    }
                }
            }

            try {
                if (pattern.getHasInnerAttachments()) {
                    if (!pattern.getAttachmentInnerObject().isEmpty()) {
                        for (AttachmentObject ao : pattern.getAttachmentInnerObject()) {

                            Elements element = elem.select(ao.getTagForElementToScrape());
                            if (!element.isEmpty()) {
                                /*Se attachmentlink inizia per http, allora attachmentLink è già il link definitivo da usare per scaricare
                            se invece non inizia per http ha bisogno del basePath*/
                                String attachmentLink = element.get(0).attr(ao.getAttrForElementToScrape());
                                if (!attachmentLink.substring(0, 4).equals("http")) {
                                    entity.setAttachmentIds(entityService.findAttachment(entity.getBasePath() + attachmentLink, entity.getBasePath() + entity.getPath(), entity.getBasePath()));
                                } else {
                                    entity.setAttachmentIds(entityService.findAttachment(attachmentLink, entity.getBasePath() + entity.getPath(), entity.getBasePath()));
                                }
                            }
                        }
                    }
                }

            } catch (Exception ex) {
                System.out.println("ERRORE 06");
                ex.printStackTrace();
            }
            entityService.updateScraping(entity, entity.getBasePath());
            System.out.println("Sono uscito dalla save");
        }
    }
}
