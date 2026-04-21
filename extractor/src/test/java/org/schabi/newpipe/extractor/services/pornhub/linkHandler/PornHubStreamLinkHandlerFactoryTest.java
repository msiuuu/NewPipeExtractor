package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PornHubStreamLinkHandlerFactoryTest {

    private final PornHubStreamLinkHandlerFactory factory =
            PornHubStreamLinkHandlerFactory.getInstance();

    @Test
    void canonicalUrlYieldsViewkey() throws Exception {
        assertEquals("ph5f8e9a1b2c3d4", factory.getId(
                "https://www.pornhub.com/view_video.php?viewkey=ph5f8e9a1b2c3d4"));
    }

    @Test
    void embedUrlYieldsViewkey() throws Exception {
        assertEquals("ph5f8e9a1b2c3d4", factory.getId(
                "https://www.pornhub.com/embed/ph5f8e9a1b2c3d4"));
    }

    @Test
    void extraQueryParamsStillYieldViewkey() throws Exception {
        assertEquals("abc123", factory.getId(
                "https://www.pornhub.com/view_video.php?viewkey=abc123&p=foo"));
    }

    @Test
    void nonPornhubUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl("https://www.youtube.com/watch?v=abc123"));
    }

    @Test
    void pornhubVideoUrlIsAccepted() throws Exception {
        assertTrue(factory.onAcceptUrl(
                "https://www.pornhub.com/view_video.php?viewkey=ph5f8e9a1b2c3d4"));
    }

    @Test
    void canonicalReconstructionMatches() throws Exception {
        assertEquals("https://www.pornhub.com/view_video.php?viewkey=abc123",
                factory.getUrl("abc123"));
    }
}
