// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;

import java.util.List;

/**
 * Comment threads are identified by the video they belong to, so this handler
 * delegates entirely to PornHubStreamLinkHandlerFactory.
 */
public final class PornHubCommentsLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final PornHubCommentsLinkHandlerFactory INSTANCE =
            new PornHubCommentsLinkHandlerFactory();

    private PornHubCommentsLinkHandlerFactory() {
    }

    public static PornHubCommentsLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(final String url)
            throws ParsingException, UnsupportedOperationException {
        return PornHubStreamLinkHandlerFactory.getInstance().getId(url);
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return PornHubStreamLinkHandlerFactory.getInstance().getUrl(id);
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        return PornHubStreamLinkHandlerFactory.getInstance().onAcceptUrl(url);
    }
}
