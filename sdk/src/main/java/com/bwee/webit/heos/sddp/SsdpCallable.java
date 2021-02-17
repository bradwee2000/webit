package com.bwee.webit.heos.sddp;

import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.util.concurrent.Callable;

@Slf4j
public class SsdpCallable implements Callable {
    private final String SSDP_HOST = "239.255.255.250";
    private final int SSDP_PORT = 1900;
    private final int SSDP_MX = 1;
    private final String SSDP_ST = "urn:schemas-denon-com:device:ACT-Denon:1";

    public String SSDP_REQUEST(){
        return "M-SEARCH * HTTP/1.1\r\n"
                + "HOST: " + SSDP_HOST + ":" + SSDP_PORT + "\r\n"
                + "MAN: \"ssdp:discover\"" + "\r\n"
                + "MX: " + SSDP_MX + "\r\n"
                + "ST: " + SSDP_ST + "\r\n"
                + "\r\n";
    }

    @Override
    public Object call() throws Exception {
        InetAddress multicastAddress = InetAddress.getByName(this.SSDP_HOST);

        InetSocketAddress multicast = new InetSocketAddress(InetAddress.getByName(this.SSDP_HOST), SSDP_PORT);
        MulticastSocket socket = new MulticastSocket(SSDP_PORT);
        socket.setReuseAddress(true);
        socket.setSoTimeout(10000);
        log.info("multicastAddress: {}", socket.getLocalSocketAddress());
        socket.joinGroup(multicast, socket.getNetworkInterface());

        byte[] packetBuffer = SSDP_REQUEST().getBytes("UTF-8");
        DatagramPacket mSearchPacket = new DatagramPacket(packetBuffer, packetBuffer.length, multicastAddress, SSDP_PORT);
        socket.send(mSearchPacket);

        /**
         * Should run until either:
         * - A compatible device is found
         * - The socket times out (10000ms)
         */
        while(true){

            byte[] responseBuffer = new byte[8192];
            DatagramPacket packet = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(packet);

            String packetAddress = packet.getAddress().getHostAddress();

            String fullResponse = new String(responseBuffer, 0, packet.getLength());

            /**
             * Make sure the response is not from localhost and the USN contains the HEOS urn.
             */
            if(!packetAddress.equals(InetAddress.getLocalHost().getHostAddress())){
                if(fullResponse.contains(SSDP_ST)){
                    return packetAddress;
                }
            }
        }
    }

}
