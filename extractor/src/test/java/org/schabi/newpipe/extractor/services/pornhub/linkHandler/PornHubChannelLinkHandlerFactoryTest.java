package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PornHubChannelLinkHandlerFactoryTest {

    private final PornHubChannelLinkHandlerFactory factory =
            PornHubChannelLinkHandlerFactory.getInstance();

    @Test
    void modelUrlYieldsTypedId() throws Exception {
        assertEquals("model/lana-rhoades",
                factory.getId("https://www.pornhub.com/model/lana-rhoades"));
    }

    @Test
    void pornstarUrlYieldsTypedId() throws Exception {
        assertEquals("pornstar/abella-danger",
                factory.getId("https://www.pornhub.com/pornstar/abella-danger"));
    }

    @Test
    void usersUrlYieldsTypedId() throws Exception {
        assertEquals("users/officialriley",
                factory.getId("https://www.pornhub.com/users/officialriley"));
    }

    @Test
    void trailingPathIgnored() throws Exception {
        assertEquals("model/lana-rhoades",
                factory.getId("https://www.pornhub.com/model/lana-rhoades/videos"));
    }

    @Test
    void canonicalReconstructionMatches() throws Exception {
        assertEquals("https://www.pornhub.com/model/lana-rhoades",
                factory.getUrl("model/lana-rhoades", Collections.emptyList(), ""));
    }

    @Test
    void channelUrlIsAccepted() throws Exception {
        assertTrue(factory.onAcceptUrl(
                "https://www.pornhub.com/model/lana-rhoades"));
    }

    @Test
    void videoUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl(
                "https://www.pornhub.com/view_video.php?viewkey=abc123"));
    }

    @Test
    void nonPornhubUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl("https://www.youtube.com/channel/UCabc"));
    }
}
