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
                            .setUsername("wA4EAkk6Bw3h3m4nLoJ5MXOTPQEZxB008D322l6vAtBZnclAtRghCvOWA8I9PfGPAAAAAFz0u0diaHcxOTk0")
                            .setPassword("260645f6-85c7-11e9-821c-066b071c7196")
                            .createIceServer()
            );
        }

        return iceServerList;
    }
}
