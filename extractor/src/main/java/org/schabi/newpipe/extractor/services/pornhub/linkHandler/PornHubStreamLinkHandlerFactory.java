// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper;
import org.schabi.newpipe.extractor.utils.Utils;

import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper.BASE_URL;

/**
 * <p>Tracks don't have standalone ids, they are always in combination with the band id.
 * That's why id = url.</p>
 *
 * <p>Radio (pornhub weekly) shows do have ids.</p>
 */
public final class PornHubStreamLinkHandlerFactory extends LinkHandlerFactory {

    private static final PornHubStreamLinkHandlerFactory INSTANCE
            = new PornHubStreamLinkHandlerFactory();

    private PornHubStreamLinkHandlerFactory() {
    }

    public static PornHubStreamLinkHandlerFactory getInstance() {
        return INSTANCE;
    }


    /**
     * @see PornHubStreamLinkHandlerFactory
     */
    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        if (PornHubExtractorHelper.isRadioUrl(url)) {
            return url.split("pornhub.com/\\?show=")[1];
        } else {
            return getUrl(url);
        }
    }

    /**
     * Clean up url
     * @see PornHubStreamLinkHandlerFactory
     */
    @Override
    public String getUrl(final String input)
            throws ParsingException, UnsupportedOperationException {
        if (input.matches("\\d+")) {
            return BASE_URL + "/?show=" + input;
        } else {
            return Utils.replaceHttpWithHttps(input);
        }
    }

    /**
     * Accepts URLs that point to a pornhub radio show or that are a pornhub
     * domain and point to a track.
     */
    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {

        // Accept PornHub radio
        if (PornHubExtractorHelper.isRadioUrl(url)) {
            return true;
        }

        // Don't accept URLs that don't point to a track
        if (!url.toLowerCase().matches("https?://.+\\..+/track/.+")) {
            return false;
        }

        // Test whether domain is supported
        return PornHubExtractorHelper.isArtistDomain(url);
    }
}
