// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;
import org.schabi.newpipe.extractor.utils.Utils;

import java.util.List;

import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubFeaturedExtractor.FEATURED_API_URL;
import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubFeaturedExtractor.KIOSK_FEATURED;

public final class PornHubFeaturedLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final PornHubFeaturedLinkHandlerFactory INSTANCE =
            new PornHubFeaturedLinkHandlerFactory();

    private PornHubFeaturedLinkHandlerFactory() {
    }

    public static PornHubFeaturedLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        if (id.equals(KIOSK_FEATURED)) {
            return FEATURED_API_URL; // doesn't have a website
        } else {
            return null;
        }
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        final String fixedUrl = Utils.replaceHttpWithHttps(url);
        if (fixedUrl.equals(FEATURED_API_URL)) {
            return KIOSK_FEATURED;
        } else {
            return null;
        }
    }

    @Override
    public boolean onAcceptUrl(final String url) {
        final String fixedUrl = Utils.replaceHttpWithHttps(url);
        return fixedUrl.equals(FEATURED_API_URL);
    }
}
