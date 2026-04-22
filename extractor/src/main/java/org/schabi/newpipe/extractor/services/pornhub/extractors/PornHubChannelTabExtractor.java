// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.MultiInfoItemsCollector;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor;
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabs;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.services.pornhub.extractors.streaminfoitem.PornHubSearchStreamInfoItemExtractor;

public class PornHubChannelTabExtractor extends ChannelTabExtractor {

    public PornHubChannelTabExtractor(final StreamingService service,
                                      final ListLinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader) {
        // fetched per page
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

        final String tab = getLinkHandler().getContentFilters().isEmpty()
                ? ChannelTabs.VIDEOS
                : getLinkHandler().getContentFilters().get(0);

        if (ChannelTabs.VIDEOS.equals(tab)) {
            for (final Element card
                    : doc.select("ul.videos li.pcVideoListItem")) {
                collector.commit(
                        new PornHubSearchStreamInfoItemExtractor(card));
            }
        }
        // PLAYLISTS tab wired in a follow-up.

        return new InfoItemsPage<>(collector, nextPage(page.getUrl(), doc));
    }

    private static Page nextPage(final String currentUrl, final Document doc) {
        int maxPage = 1;
        for (final Element link : doc.select(".pagination3 a")) {
            try {
                final int n = Integer.parseInt(link.text().trim());
                if (n > maxPage) {
                    maxPage = n;
                }
            } catch (final NumberFormatException ignored) {
                // skip non-numeric ("Prev"/"Next")
            }
        }

        int currentPage = 1;
        final String pageParam = "page=";
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
                // leave currentPage as 1
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
