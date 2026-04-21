package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper;
import org.schabi.newpipe.extractor.utils.Utils;

import java.util.List;

/**
 * Like in {@link PornHubStreamLinkHandlerFactory}, tracks have no meaningful IDs except for
 * their URLs
 */
public final class PornHubCommentsLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final PornHubCommentsLinkHandlerFactory INSTANCE
            = new PornHubCommentsLinkHandlerFactory();

    private PornHubCommentsLinkHandlerFactory() {
    }

    public static PornHubCommentsLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        return Utils.replaceHttpWithHttps(url);
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        if (PornHubExtractorHelper.isRadioUrl(url)) {
            return true;
        }

        // Don't accept URLs that don't point to a track
        if (!url.toLowerCase().matches("https?://.+\\..+/(track|album)/.+")) {
            return false;
        }

        // Test whether domain is supported
        return PornHubExtractorHelper.isArtistDomain(url);
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return Utils.replaceHttpWithHttps(id);
    }
}
