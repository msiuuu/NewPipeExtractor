// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandlerFactory;
import org.schabi.newpipe.extractor.utils.Utils;

import java.util.List;

public final class PornHubSearchQueryHandlerFactory extends SearchQueryHandlerFactory {

    private static final String SEARCH_URL =
            "https://www.pornhub.com/video/search?search=";

    private static final PornHubSearchQueryHandlerFactory INSTANCE =
            new PornHubSearchQueryHandlerFactory();

    private PornHubSearchQueryHandlerFactory() {
    }

    public static PornHubSearchQueryHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUrl(final String query,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return SEARCH_URL + Utils.encodeUrlUtf8(query);
    }
}
