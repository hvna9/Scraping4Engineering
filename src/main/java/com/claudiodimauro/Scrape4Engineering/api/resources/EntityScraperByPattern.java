package com.claudiodimauro.Scrape4Engineering.api.resources;

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

                if (pattern.getHasPrescraping()) {
                    Elements prescraping = doc.select(pattern.getTagForPrescraping());
                    int numPag = 1;
                    for (Element pagination : prescraping) {
                        if (numPag == 1) {
                            Elements elem = doc.select(pattern.getTagForBody());
                            paginationScrape(elem, pattern);
                        } else {
                            try {
                                if (!pagination.attr("href").substring(0, 4).equals("http")) {
                                    int i = 0, j = 0;
                                    do {
                                        j = pattern.getUrl().indexOf("/", j) + 1;
                                        System.out.println("i: " + i + " - j: " + j);
                                        i++;
                                    } while (i <= 2);
                                    String url = pattern.getUrl().substring(0, j - 1);

                                    Document doc2 = Jsoup.connect(url + pagination.attr("href")).timeout(10000).get();
                                    paginationScrape(doc2.select(pattern.getTagForBody()), pattern);
                                }

                            } catch (Exception ex) {
                                System.out.println("siamo qui?????");
                                ex.printStackTrace();
                            }
                        }
                        numPag++;
                    }
                } else {
                    Elements elem = doc.select(pattern.getTagForBody());
                    paginationScrape(elem, pattern);
                }

            } catch (Exception ex) {
                System.out.println("siamo qui?????");
                ex.printStackTrace();
            }
        }
        return "";
    }

    private void paginationScrape(Elements pagination, Pattern pattern) throws IOException {

        int i = 0, j = 0;
        do {
            j = pattern.getUrl().indexOf("/", j) + 1;
            System.out.println("i: " + i + " - j: " + j);
            i++;
        } while (i <= 2);
        String url = pattern.getUrl().substring(0, j - 1);
        System.out.println("\n\n\n URL: " + url);

        for (Element elem : pagination) {
            System.out.println("SONO NEL FOR DI PAGINATION");
            Entity entity = new Entity();

            Elements idElement = elem.select(pattern.getEntityId());
            if (!idElement.isEmpty()) {
                entity.setEntityId(idElement.get(0).attr(pattern.getAttrForEntityId()));
                System.out.println("ID: " + idElement.get(0).attr(pattern.getAttrForEntityId()));
            }
            
            //ATTUALMENTE NON FUNZIONA PERCHé NEL PATTERN ANSA NON HANNO PATH
            Elements pathElement = elem.select(pattern.getEntityPath());
            if (!pathElement.isEmpty()) {
                entity.setPath(pathElement.get(0).attr("href"));
                System.out.println("ID: " + idElement.get(0).attr(pattern.getAttrForEntityId()));
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
                        System.out.println("È QUI L'ERRORE?");
                    }
                }
            }
            Date date = new Date();
            entity.setLastScraping(date);
            entity.setBasePath(url);
            entityService.updateScraping(entity, url);
        }
    }

}
