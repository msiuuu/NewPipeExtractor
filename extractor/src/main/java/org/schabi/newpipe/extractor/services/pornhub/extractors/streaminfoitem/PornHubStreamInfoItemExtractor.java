package org.schabi.newpipe.extractor.services.pornhub.extractors.streaminfoitem;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.localization.DateWrapper;
import org.schabi.newpipe.extractor.stream.StreamInfoItemExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;

import javax.annotation.Nullable;

/**
 * Base class providing constant defaults for fields that most PornHub
 * info-item extractors don't surface. Subclasses override only what they
 * can extract from their specific page context.
 */
public abstract class PornHubStreamInfoItemExtractor implements StreamInfoItemExtractor {
    private final String uploaderUrl;

    public PornHubStreamInfoItemExtractor(final String uploaderUrl) {
        this.uploaderUrl = uploaderUrl;
    }

    @Override
    public StreamType getStreamType() {
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public long getViewCount() {
        return -1;
    }

    @Override
    public String getUploaderUrl() {
        return uploaderUrl;
    }

    @Nullable
    @Override
    public String getTextualUploadDate() {
        return null;
    }

    @Nullable
    @Override
    public DateWrapper getUploadDate() {
        return null;
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        return false;
    }

    @Override
    public boolean isAd() {
        return false;
    }
}
