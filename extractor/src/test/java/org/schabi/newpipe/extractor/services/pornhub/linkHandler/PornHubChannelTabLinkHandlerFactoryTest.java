package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.junit.jupiter.api.Test;
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabs;
import org.schabi.newpipe.extractor.exceptions.UnsupportedTabException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PornHubChannelTabLinkHandlerFactoryTest {

    private final PornHubChannelTabLinkHandlerFactory factory =
            PornHubChannelTabLinkHandlerFactory.getInstance();

    @Test
    void getIdDelegatesToChannelHandler() throws Exception {
        assertEquals("model/lana-rhoades",
                factory.getId("https://www.pornhub.com/model/lana-rhoades"));
    }

    @Test
    void videosTabUrlBuilt() throws Exception {
        assertEquals("https://www.pornhub.com/model/lana-rhoades/videos",
                factory.getUrl("model/lana-rhoades",
                        Collections.singletonList(ChannelTabs.VIDEOS), ""));
    }

    @Test
    void playlistsTabUrlBuilt() throws Exception {
        assertEquals("https://www.pornhub.com/model/lana-rhoades/playlists",
                factory.getUrl("model/lana-rhoades",
                        Collections.singletonList(ChannelTabs.PLAYLISTS), ""));
    }

    @Test
    void unsupportedTabThrows() {
        assertThrows(UnsupportedTabException.class, () ->
                factory.getUrl("model/lana-rhoades",
                        Collections.singletonList(ChannelTabs.TRACKS), ""));
    }

    @Test
    void channelUrlIsAccepted() throws Exception {
        assertTrue(factory.onAcceptUrl(
                "https://www.pornhub.com/model/lana-rhoades"));
    }

    @Test
    void channelTabUrlIsAccepted() throws Exception {
        assertTrue(factory.onAcceptUrl(
                "https://www.pornhub.com/model/lana-rhoades/videos"));
    }

    @Test
    void videoUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl(
                "https://www.pornhub.com/view_video.php?viewkey=abc123"));
    }

    @Test
    void availableTabsCorrect() {
        final List<String> tabs = Arrays.asList(factory.getAvailableContentFilter());
        assertTrue(tabs.contains(ChannelTabs.VIDEOS));
        assertTrue(tabs.contains(ChannelTabs.PLAYLISTS));
    }
}
