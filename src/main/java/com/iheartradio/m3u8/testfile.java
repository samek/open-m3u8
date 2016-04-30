package com.iheartradio.m3u8;

import com.iheartradio.m3u8.*;
import com.iheartradio.m3u8.data.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by samek on 30/04/16.
 */
public class testfile {
    static Integer seqnum=0;
    Double targetDuration=10.00;

    MediaPlaylist mediaPlaylist = null;
    Playlist playlist = null;
    static String urlPrefix="http://hls.24ur.com/POPTV/enc/";


    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        //String out = buildMediaPlist();
        System.out.println(buildMediaPlist());

    }


    public static String buildMediaPlist() {

        EncryptionData encryptionData=null;
        TrackData trackData = null;

        List<TrackData> tracks = new ArrayList<TrackData>();
        double maxDuration=0.0;
        for (int x=0;x<3;x++) {

            double currentDuration = 10.2;


            //Check max duration and set the target duration to it//
            if (maxDuration<currentDuration)
                maxDuration=currentDuration;

            List<Integer> l = new ArrayList<Integer>();
            l.add(1);

            Integer keyNum = seqnum+x;
            keyNum =(int) keyNum / 100;
            encryptionData = new EncryptionData.Builder()
                    .withUri("http://hls.24ur.com/plist/POPTV/1/asd/"+keyNum+"/index.key")
                    .withMethod(EncryptionMethod.AES)
                    .withKeyFormat("identity")
                    .withKeyFormatVersions(l)
                    .build();


            trackData = new TrackData.Builder()
                    .withTrackInfo(new TrackInfo((float) currentDuration, ""))
                    .withUri(urlPrefix+"file_name_"+x+".ts")
                    .withEncryptionData(encryptionData)
                    .build();

            tracks.add(trackData);
            //////////CHUNKS ADDED////
        }


        MediaPlaylist mediaPlaylist = new MediaPlaylist.Builder()
                .withMediaSequenceNumber(seqnum)
                .withTargetDuration((int)Math.ceil(maxDuration))
                .withTracks(tracks)
                .withIsOngoing(true)
                .build();

        Playlist playlist = new Playlist.Builder()
                .withCompatibilityVersion(5)
                .withMediaPlaylist(mediaPlaylist)

                .build();


        return writePlaylist(playlist);
        //return trackData.toString();
        //return output.toString();
    }

    static String writePlaylist(Playlist playlist)  {


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PlaylistWriter writer = new PlaylistWriter(os, Format.EXT_M3U, Encoding.UTF_8);

        try {
            writer.write(playlist);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (PlaylistException e) {
            e.printStackTrace();
        }
        return os.toString();
        //return os.toString().replace("#EXT-X-ENDLIST\n","");

    }

}

