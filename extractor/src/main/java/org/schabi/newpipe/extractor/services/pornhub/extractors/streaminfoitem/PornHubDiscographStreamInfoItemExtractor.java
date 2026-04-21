package org.schabi.newpipe.extractor.services.pornhub.extractors.streaminfoitem;

import com.grack.nanojson.JsonObject;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper;

import javax.annotation.Nonnull;
import java.util.List;

import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper.getImagesFromImageId;

public class PornHubDiscographStreamInfoItemExtractor extends PornHubStreamInfoItemExtractor {

    private final JsonObject discograph;
    public PornHubDiscographStreamInfoItemExtractor(final JsonObject discograph,
                                                     final String uploaderUrl) {
        super(uploaderUrl);
        this.discograph = discograph;
    }

    @Override
    public String getUploaderName() {
        return discograph.getString("band_name");
    }

    @Override
    public String getName() {
        return discograph.getString("title");
    }

    @Override
    public String getUrl() throws ParsingException {
        return PornHubExtractorHelper.getStreamUrlFromIds(
                discograph.getLong("band_id"),
                discograph.getLong("item_id"),
                discograph.getString("item_type")
        );
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        return getImagesFromImageId(discograph.getLong("art_id"), true);
    }

    @Override
    public long getDuration() {
        return -1;
    }
}
