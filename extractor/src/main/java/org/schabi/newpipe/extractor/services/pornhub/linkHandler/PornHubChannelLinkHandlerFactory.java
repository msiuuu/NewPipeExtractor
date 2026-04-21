// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PornHub channel id encodes both the channel type and slug
 * (e.g. "model/lana-rhoades") so the canonical URL can be rebuilt
 * without ambiguity between model, pornstar, and user pages.
 */
public final class PornHubChannelLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final String BASE = "https://www.pornhub.com/";

    private static final Pattern CHANNEL_PATTERN = Pattern.compile(
            "^https?://([a-z0-9-]+\\.)?pornhub\\.com/(model|pornstar|users)/([^/?#]+)",
            Pattern.CASE_INSENSITIVE);

    private static final PornHubChannelLinkHandlerFactory INSTANCE =
            new PornHubChannelLinkHandlerFactory();

    private PornHubChannelLinkHandlerFactory() {
    }

    public static PornHubChannelLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(final String url)
            throws ParsingException, UnsupportedOperationException {
        final Matcher m = CHANNEL_PATTERN.matcher(url);
        if (!m.find()) {
            throw new ParsingException("Could not extract channel id from URL: " + url);
        }
        return m.group(2).toLowerCase() + "/" + m.group(3);
    }

    @Override
    public String getUrl(final String id, final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return BASE + id;
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        return CHANNEL_PATTERN.matcher(url).find();
    }
}
