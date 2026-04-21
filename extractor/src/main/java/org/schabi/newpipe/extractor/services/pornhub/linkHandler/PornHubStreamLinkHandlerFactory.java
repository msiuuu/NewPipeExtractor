// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandlerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PornHubStreamLinkHandlerFactory extends LinkHandlerFactory {

    private static final String CANONICAL_URL =
            "https://www.pornhub.com/view_video.php?viewkey=";

    private static final Pattern VIEWKEY_PATTERN =
            Pattern.compile("[?&]viewkey=([a-zA-Z0-9]+)");
    private static final Pattern EMBED_PATTERN =
            Pattern.compile("/embed/([a-zA-Z0-9]+)");
    private static final Pattern HOST_PATTERN =
            Pattern.compile("^https?://([a-z0-9-]+\\.)?pornhub\\.com/.+",
                    Pattern.CASE_INSENSITIVE);

    private static final PornHubStreamLinkHandlerFactory INSTANCE =
            new PornHubStreamLinkHandlerFactory();

    private PornHubStreamLinkHandlerFactory() {
    }

    public static PornHubStreamLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId(final String url)
            throws ParsingException, UnsupportedOperationException {
        Matcher m = VIEWKEY_PATTERN.matcher(url);
        if (m.find()) {
            return m.group(1);
        }
        m = EMBED_PATTERN.matcher(url);
        if (m.find()) {
            return m.group(1);
        }
        throw new ParsingException("Could not extract viewkey from URL: " + url);
    }

    @Override
    public String getUrl(final String id)
            throws ParsingException, UnsupportedOperationException {
        return CANONICAL_URL + id;
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        if (!HOST_PATTERN.matcher(url).matches()) {
            return false;
        }
        try {
            getId(url);
            return true;
        } catch (final ParsingException e) {
            return false;
        }
    }
}
