package org.schabi.newpipe.extractor.services.pornhub.extractors;

import com.grack.nanojson.JsonObject;

import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItemExtractor;

import java.util.List;

import javax.annotation.Nonnull;

public class PornHubAlbumInfoItemExtractor implements PlaylistInfoItemExtractor {
    private final JsonObject albumInfoItem;
    private final String uploaderUrl;

    public PornHubAlbumInfoItemExtractor(final JsonObject albumInfoItem,
                                          final String uploaderUrl) {
        this.albumInfoItem = albumInfoItem;
        this.uploaderUrl = uploaderUrl;
    }

    @Override
    public String getName() throws ParsingException {
        return albumInfoItem.getString("title");
    }

    @Override
    public String getUrl() throws ParsingException {
        return PornHubExtractorHelper.getStreamUrlFromIds(
                albumInfoItem.getLong("band_id"),
                albumInfoItem.getLong("item_id"),
                albumInfoItem.getString("item_type"));
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        return PornHubExtractorHelper.getImagesFromImageId(albumInfoItem.getLong("art_id"), true);
    }

    @Override
    public String getUploaderName() throws ParsingException {
        return albumInfoItem.getString("band_name");
    }

    @Override
    public String getUploaderUrl() {
        return uploaderUrl;
    }

    @Override
    public boolean isUploaderVerified() {
        return false;
    }

    @Override
    public long getStreamCount() {
        return ListExtractor.ITEM_COUNT_UNKNOWN;
    }
}
