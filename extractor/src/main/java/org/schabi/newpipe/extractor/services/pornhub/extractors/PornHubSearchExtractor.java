// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.MetaInfo;
import org.schabi.newpipe.extractor.MultiInfoItemsCollector;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandler;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.streaminfoitem.PornHubSearchStreamInfoItemExtractor;

public class PornHubSearchExtractor extends SearchExtractor {

    public PornHubSearchExtractor(final StreamingService service,
                                  final SearchQueryHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        // fetching happens per page
    }

    @Nonnull
    @Override
    public String getSearchSuggestion() {
        return "";
    }

    @Override
    public boolean isCorrectedSearch() {
        return false;
    }

    @Nonnull
    @Override
    public List<MetaInfo> getMetaInfo() throws ParsingException {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public InfoItemsPage<InfoItem> getInitialPage()
            throws IOException, ExtractionException {
        return getPage(new Page(getUrl()));
    }

    @Override
    public InfoItemsPage<InfoItem> getPage(final Page page)
            throws IOException, ExtractionException {
        final MultiInfoItemsCollector collector =
                new MultiInfoItemsCollector(getServiceId());
        final Document doc = Jsoup.parse(
                getDownloader().get(page.getUrl()).responseBody());

        for (final Element card
                : doc.select("ul#videoSearchResult > li.pcVideoListItem")) {
            collector.commit(
                    new PornHubSearchStreamInfoItemExtractor(card));
        }

        return new InfoItemsPage<>(collector, nextPage(page.getUrl(), doc));
    }

    /**
     * Pornhub uses simple numbered pagination via &page=N. Current page is
     * derived from the URL; max page is the largest numeric link in the
     * pagination block. Returns null when we're on the last page.
     */
    private static Page nextPage(final String currentUrl, final Document doc) {
        int maxPage = 1;
        for (final Element link : doc.select(".pagination3 a")) {
            try {
                final int n = Integer.parseInt(link.text().trim());
                if (n > maxPage) {
                    maxPage = n;
                }
            } catch (final NumberFormatException ignored) {
                // skip non-numeric links ("Prev", "Next", etc)
            }
        }

        int currentPage = 1;
        final String pageParam = "&page=";
        final int ix = currentUrl.indexOf(pageParam);
        if (ix >= 0) {
            final int start = ix + pageParam.length();
            int end = start;
            while (end < currentUrl.length()
                    && Character.isDigit(currentUrl.charAt(end))) {
                end++;
            }
            try {
                currentPage = Integer.parseInt(
                        currentUrl.substring(start, end));
            } catch (final NumberFormatException ignored) {
                // leave as 1
            }
        }

        if (currentPage >= maxPage) {
            return null;
        }
        final int next = currentPage + 1;
        final String nextUrl;
        if (ix >= 0) {
            final int start = ix + pageParam.length();
            int end = start;
            while (end < currentUrl.length()
                    && Character.isDigit(currentUrl.charAt(end))) {
                end++;
            }
            nextUrl = currentUrl.substring(0, start) + next
                    + currentUrl.substring(end);
        } else {
            nextUrl = currentUrl
                    + (currentUrl.contains("?") ? "&" : "?")
                    + "page=" + next;
        }
        return new Page(nextUrl);
    }
}
