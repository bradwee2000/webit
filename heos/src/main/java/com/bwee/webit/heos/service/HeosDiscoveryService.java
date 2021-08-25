package com.bwee.webit.heos.service;

import com.google.common.base.Charsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Slf4j
@Service
public class HeosDiscoveryService {

    private static final int SOCKET_TIMEOUT_MILLIS = 10_000;
    private final String SSDP_HOST = "239.255.255.250";
    private final int SSDP_PORT = 1900;
    private final int SSDP_MX = 1;
    private final String SSDP_ST = "urn:schemas-denon-com:device:ACT-Denon:1";
    private final String SSDP_REQUEST =
            "M-SEARCH * HTTP/1.1\r\n"
                    + "HOST: " + SSDP_HOST + ":" + SSDP_PORT + "\r\n"
                    + "MAN: \"ssdp:discover\"" + "\r\n"
                    + "MX: " + SSDP_MX + "\r\n"
                    + "ST: " + SSDP_ST + "\r\n"
                    + "\r\n";

    @SneakyThrows
    public String discoverHeosDeviceIp() {
        /* our M-SEARCH data as a byte array */
        byte[] sendData  = SSDP_REQUEST.getBytes(Charsets.UTF_8);

        /* create a packet from our data destined for 239.255.255.250:1900 */
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(SSDP_HOST), SSDP_PORT);

        /* send packet to the socket we're creating */
        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(SOCKET_TIMEOUT_MILLIS);
        clientSocket.send(sendPacket);

        /* create byte arrays to hold our send and response data */
        byte[] receiveData = new byte[1024];

        try {
            while (true) {
                /* recieve response and store in our receivePacket */
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                String heosAddress = receivePacket.getAddress().getHostAddress();

                /* get the response as a string */
                String response = new String(receivePacket.getData());
                log.info("Found HEOS device: {}", response);
                if (response.contains(SSDP_ST) && !heosAddress.equals(InetAddress.getLocalHost().getHostAddress())) {
                    return heosAddress;
                }
            }
        } finally {
            /* close the socket */
            clientSocket.close();
        }
    }
}
