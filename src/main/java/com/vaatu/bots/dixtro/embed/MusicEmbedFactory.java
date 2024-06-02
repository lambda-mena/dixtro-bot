package com.vaatu.bots.dixtro.embed;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class MusicEmbedFactory {
    public static String getVideoLength(Long videoLength) {
        long parsedLongToDouble = videoLength / 60000;
        int nearestInteger = (int) Math.floor(parsedLongToDouble);
        String minutes = String.valueOf(nearestInteger);
        return minutes + " minutes";
    }

    public static MessageEmbed createSongEmbed(AudioTrackInfo trackInfo) {
        EmbedBuilder newEmbed = new EmbedBuilder();
        newEmbed.setTitle("🎵 " + trackInfo.title);
        newEmbed.setAuthor("🖋️ Author:" + trackInfo.author);
        newEmbed.addField("💿 Video Length:", getVideoLength(trackInfo.length), false);
        newEmbed.setImage(trackInfo.artworkUrl);
        newEmbed.setColor(Color.ORANGE);
        return newEmbed.build();
    }

    public static MessageEmbed createFinishedTracks() {
        EmbedBuilder newEmbed = new EmbedBuilder();
        newEmbed.setTitle("🥳 Finished all tracks!");
        newEmbed.setColor(Color.BLUE);
        return newEmbed.build();
    }

    public static MessageEmbed createUserErrorEmbed(String errorReason) {
        EmbedBuilder newEmbed = new EmbedBuilder();
        newEmbed.setTitle("User Input Error");
        newEmbed.addField("Reason:", errorReason, true);
        newEmbed.setColor(Color.RED);
        return newEmbed.build();
    }

    public static MessageEmbed createInternalErrorEmbed() {
        EmbedBuilder newEmbed = new EmbedBuilder();
        newEmbed.setTitle("Internal Error.");
        newEmbed.addField("Suggestion", "report this whenever you can.", true);
        newEmbed.setColor(Color.RED);
        return newEmbed.build();
    }
}
