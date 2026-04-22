package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.extractor.services.pornhub.extractors.streaminfoitem.PornHubSearchStreamInfoItemExtractor;

public class PornHubSearchExtractorLiveTest {

    private static final String SAMPLE_PATH =
            "C:/Dev_Code/pornhub_search_sample.txt";

    @Test
    public void parseSearchResultsFromSavedHtml() throws Exception {
        final String html = new String(
                Files.readAllBytes(Paths.get(SAMPLE_PATH)),
                StandardCharsets.UTF_8);
        final Document doc = Jsoup.parse(html);

        int count = 0;
        for (final Element card
                : doc.select("ul#videoSearchResult > li.pcVideoListItem")) {
            final PornHubSearchStreamInfoItemExtractor ext =
                    new PornHubSearchStreamInfoItemExtractor(card);
            if (count < 3) {
                System.out.println("=== result " + count + ":");
                System.out.println("  name: " + ext.getName());
                System.out.println("  url: " + ext.getUrl());
                System.out.println("  uploader: " + ext.getUploaderName());
                System.out.println("  uploaderUrl: " + ext.getUploaderUrl());
                System.out.println("  duration: " + ext.getDuration());
                System.out.println("  thumbs: " + ext.getThumbnails().size()
                        + " url[0]=" + (ext.getThumbnails().isEmpty() ? "none"
                                : ext.getThumbnails().get(0).getUrl()
                                        .substring(0, 80) + "..."));
            }
            count++;
        }
        System.out.println("=== total results parsed: " + count);
    }
}
