// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper.BASE_URL;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandlerFactory;
import org.schabi.newpipe.extractor.utils.Utils;

import java.util.List;

public final class PornHubSearchQueryHandlerFactory extends SearchQueryHandlerFactory {

    private static final PornHubSearchQueryHandlerFactory INSTANCE
            = new PornHubSearchQueryHandlerFactory();

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
        return BASE_URL + "/search?q=" + Utils.encodeUrlUtf8(query) + "&page=1";
    }
}
