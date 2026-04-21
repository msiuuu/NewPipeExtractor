// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.schabi.newpipe.extractor.channel.tabs.ChannelTabs;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.UnsupportedTabException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public final class PornHubChannelTabLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final PornHubChannelTabLinkHandlerFactory INSTANCE =
            new PornHubChannelTabLinkHandlerFactory();

    private PornHubChannelTabLinkHandlerFactory() {
    }

    public static PornHubChannelTabLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Nonnull
    public static String getUrlSuffix(@Nonnull final String tab)
            throws UnsupportedTabException {
        switch (tab) {
            case ChannelTabs.VIDEOS:
                return "/videos";
            case ChannelTabs.PLAYLISTS:
                return "/playlists";
            default:
                throw new UnsupportedTabException(tab);
        }
    }

    @Override
    public String getId(final String url)
            throws ParsingException, UnsupportedOperationException {
        return PornHubChannelLinkHandlerFactory.getInstance().getId(url);
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return PornHubChannelLinkHandlerFactory.getInstance().getUrl(id)
                + getUrlSuffix(contentFilter.get(0));
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        return PornHubChannelLinkHandlerFactory.getInstance().onAcceptUrl(url);
    }

    @Override
    public String[] getAvailableContentFilter() {
        return new String[]{
                ChannelTabs.VIDEOS,
                ChannelTabs.PLAYLISTS,
        };
    }
}
