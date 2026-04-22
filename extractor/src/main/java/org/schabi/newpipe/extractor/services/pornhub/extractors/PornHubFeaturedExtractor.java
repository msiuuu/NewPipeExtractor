// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.
//
// Stubbed for v1. Trending kiosk requires switching the generic type from
// PlaylistInfoItem to StreamInfoItem and updating service registration,
// which is follow-up scope.

package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem;
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItemsCollector;

public class PornHubFeaturedExtractor extends KioskExtractor<PlaylistInfoItem> {

    public static final String KIOSK_FEATURED = "Featured";

    /**
     * Placeholder URL identifying the Featured kiosk. References to this
     * constant from the link handler factory + service class rely on the
     * value existing; the eventual proper implementation will fetch this
     * page. Not currently hit while the extractor is stubbed.
     */
    public static final String FEATURED_API_URL =
            "https://www.pornhub.com/video?o=tr";

    public PornHubFeaturedExtractor(final StreamingService streamingService,
                                    final ListLinkHandler listLinkHandler,
                                    final String kioskId) {
        super(streamingService, listLinkHandler, kioskId);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        // no-op while stubbed
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return KIOSK_FEATURED;
    }

    @Nonnull
    @Override
    public InfoItemsPage<PlaylistInfoItem> getInitialPage()
            throws IOException, ExtractionException {
        return new InfoItemsPage<>(
                new PlaylistInfoItemsCollector(getServiceId()), null);
    }

    @Override
    public InfoItemsPage<PlaylistInfoItem> getPage(final Page page)
            throws IOException, ExtractionException {
        return null;
    }
}
