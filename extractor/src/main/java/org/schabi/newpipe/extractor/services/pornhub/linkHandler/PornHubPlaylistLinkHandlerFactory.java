// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PornHubPlaylistLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final String CANONICAL_URL = "https://www.pornhub.com/playlist/";

    private static final Pattern PLAYLIST_PATTERN = Pattern.compile(
            "^https?://([a-z0-9-]+\\.)?pornhub\\.com/playlist/(\\d+)",
            Pattern.CASE_INSENSITIVE);

    private static final PornHubPlaylistLinkHandlerFactory INSTANCE =
            new PornHubPlaylistLinkHandlerFactory();

    private PornHubPlaylistLinkHandlerFactory() {
    }

    public static PornHubPlaylistLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(final String url)
            throws ParsingException, UnsupportedOperationException {
        final Matcher m = PLAYLIST_PATTERN.matcher(url);
        if (!m.find()) {
            throw new ParsingException(
                    "Could not extract playlist id from URL: " + url);
        }
        return m.group(2);
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return CANONICAL_URL + id;
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        return PLAYLIST_PATTERN.matcher(url).find();
    }
}
