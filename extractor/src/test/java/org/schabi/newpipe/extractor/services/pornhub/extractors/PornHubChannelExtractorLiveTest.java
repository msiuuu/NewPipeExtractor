package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

public class PornHubChannelExtractorLiveTest {

    private static final String SAMPLE_PATH =
            "C:/Dev_Code/pornhub_channel_sample.txt";

    @Test
    public void probeNameAndStats() throws Exception {
        final String html = new String(
                Files.readAllBytes(Paths.get(SAMPLE_PATH)),
                StandardCharsets.UTF_8);
        final Document doc = Jsoup.parse(html);

        // page title + og:title
        final Element title = doc.selectFirst("title");
        System.out.println("=== <title>: '"
                + (title == null ? "null" : title.text()) + "'");
        final Element ogTitle = doc.selectFirst("meta[property=og:title]");
        System.out.println("=== og:title: '"
                + (ogTitle == null ? "null" : ogTitle.attr("content")) + "'");

        // all h1 tags
        System.out.println("=== all <h1>:");
        for (final Element h1 : doc.select("h1")) {
            final String t = h1.text().trim();
            if (!t.isEmpty()) {
                System.out.println("  '" + t + "' class='"
                        + h1.className() + "'");
            }
        }

        // elements with "name" in their class or itemprop
        System.out.println("=== [itemprop=name]:");
        for (final Element el : doc.select("[itemprop=name]")) {
            System.out.println("  text='" + el.text()
                    + "' class='" + el.className() + "'");
        }

        // Dump text of anything near .topProfileHeader container
        System.out.println("=== .topProfileHeader parent HTML (first 2000):");
        final Element hdr = doc.selectFirst(".topProfileHeader");
        if (hdr != null && hdr.parent() != null) {
            final String p = hdr.parent().outerHtml();
            System.out.println(p.substring(0, Math.min(2000, p.length())));
        }

        // anything with "count" in class that has short text
        System.out.println("=== class*=count (short text only):");
        for (final Element el : doc.select("[class*=ount]")) {
            final String t = el.text().trim();
            if (!t.isEmpty() && t.length() < 40
                    && el.children().size() <= 3) {
                System.out.println("  '" + t + "' class='"
                        + el.className() + "'");
            }
        }
    }
}
