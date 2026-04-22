package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

public class PornHubStreamExtractorLiveTest {

    private static final String SAMPLE_PATH = "C:/Dev_Code/pornhub_sample.txt";

    private static String textOf(final Element el) {
        return el == null ? "(null)" : el.text();
    }

    @Test
    public void probeCountsAndVotes() throws Exception {
        final String html = new String(
                Files.readAllBytes(Paths.get(SAMPLE_PATH)),
                StandardCharsets.UTF_8);
        final Document doc = Jsoup.parse(html);

        // confirm uploader
        System.out.println("=== uploader: '"
                + textOf(doc.selectFirst(
                        ".video-detailed-info .usernameWrap a")) + "'");

        // view count probes
        final String[] viewSel = {
                ".count-container .count",
                ".count-videoinfo",
                ".videoInfo .count",
                ".view-count",
                ".views-count",
                ".video-detailed-info .views",
                ".viewsWrapper",
                ".video-actions-container .views",
                ".video-detailed-info span.count",
                ".tooltipTrig span",
                ".videoCounter"
        };
        System.out.println("=== view count probes:");
        for (final String sel : viewSel) {
            final Element el = doc.selectFirst(sel);
            if (el != null) {
                System.out.println("  " + sel + " -> '" + el.text() + "'");
            }
        }

        // votes probes
        final String[] voteSel = {
                ".votesUp", ".votesDown",
                "#votesUp", "#votesDown",
                ".vote-action .voteUp", ".vote-action .voteDown",
                "[data-action=video-like]", "[data-action=video-dislike]"
        };
        System.out.println("=== vote probes:");
        for (final String sel : voteSel) {
            final Element el = doc.selectFirst(sel);
            if (el != null) {
                System.out.println("  " + sel + " -> text='" + el.text()
                        + "' data-count='" + el.attr("data-count") + "'");
            }
        }
    }
}
