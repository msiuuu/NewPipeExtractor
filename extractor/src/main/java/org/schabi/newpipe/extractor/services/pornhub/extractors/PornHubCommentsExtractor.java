// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.
//
// Comments are stubbed as disabled for v1. PornHub loads comments via an
// XHR endpoint whose structure needs sampling before proper implementation.

package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.comments.CommentsExtractor;
import org.schabi.newpipe.extractor.comments.CommentsInfoItem;
import org.schabi.newpipe.extractor.comments.CommentsInfoItemsCollector;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;

public class PornHubCommentsExtractor extends CommentsExtractor {

    public PornHubCommentsExtractor(final StreamingService service,
                                    final ListLinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        // no-op while comments are stubbed
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return "";
    }

    @Nonnull
    @Override
    public InfoItemsPage<CommentsInfoItem> getInitialPage()
            throws IOException, ExtractionException {
        return new InfoItemsPage<>(
                new CommentsInfoItemsCollector(getServiceId()), null);
    }

    @Override
    public InfoItemsPage<CommentsInfoItem> getPage(final Page page)
            throws IOException, ExtractionException {
        return null;
    }

    @Override
    public boolean isCommentsDisabled() {
        return true;
    }
}
