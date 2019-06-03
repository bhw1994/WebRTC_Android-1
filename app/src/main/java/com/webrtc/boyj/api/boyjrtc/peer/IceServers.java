package com.webrtc.boyj.api.boyjrtc.peer;

        import org.webrtc.PeerConnection;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

public class IceServers {
    private IceServers() {

    }


    public static List<PeerConnection.IceServer> getIceServerList() {
        final List<PeerConnection.IceServer> iceServerList = new ArrayList<>();
        final List<String> stunUrlList = new ArrayList<>(
                Arrays.asList("stun:tk-turn2.xirsys.com")
        );
        final List<String> turnUrlList = new ArrayList<>(
                Arrays.asList(
                        "turn:tk-turn2.xirsys.com:80?transport=udp",
                        "turn:tk-turn2.xirsys.com:3478?transport=udp",
                        "turn:tk-turn2.xirsys.com:80?transport=tcp",
                        "turn:tk-turn2.xirsys.com:3478?transport=tcp",
                        "turns:tk-turn2.xirsys.com:443?transport=tcp",
                        "turns:tk-turn2.xirsys.com:5349?transport=tcp"
                )
        );

        for (final String stunServerUrl : stunUrlList) {
            iceServerList.add(
                    PeerConnection.IceServer.builder(stunServerUrl)
                            .createIceServer()
            );
        }

        for (final String turnServerUrl : turnUrlList) {
            iceServerList.add(
                    PeerConnection.IceServer.builder(turnServerUrl)
                            .setUsername("E6UT9AEw-gT6pRHfSu9K1_Au4PwnHZ3v4KgpY0amvGeGl5U7uguqyqj9ZTvhU8GGAAAAAFzytKpsZWVzbDY1")
                            .setPassword("07daff58-8492-11e9-8aad-066b071c7196")
                            .createIceServer()
            );
        }

        return iceServerList;
    }
}
