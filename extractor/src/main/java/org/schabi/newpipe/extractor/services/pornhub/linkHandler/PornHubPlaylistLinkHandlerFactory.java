// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper;
import org.schabi.newpipe.extractor.utils.Utils;

import java.util.List;

/**
 * Just as with streams, the album ids are essentially useless for us.
 */
public final class PornHubPlaylistLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final PornHubPlaylistLinkHandlerFactory INSTANCE
            = new PornHubPlaylistLinkHandlerFactory();

    private PornHubPlaylistLinkHandlerFactory() {
    }

    public static PornHubPlaylistLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        return getUrl(url);
    }

    @Override
    public String getUrl(final String url,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return Utils.replaceHttpWithHttps(url);
    }

    /**
     * Accepts all pornhub URLs that contain /album/ behind their domain name.
     */
    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {

        // Exclude URLs which do not lead to an album
        if (!url.toLowerCase().matches("https?://.+\\..+/album/.+")) {
            return false;
        }

        // Test whether domain is supported
        return PornHubExtractorHelper.isArtistDomain(url);
    }
}
