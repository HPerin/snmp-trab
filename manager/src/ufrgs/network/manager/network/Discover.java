package ufrgs.network.manager.network;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import ufrgs.network.manager.data.Client;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lucas on 12/3/16.
 */
public class Discover {

    private String community = "simple";
    private TransportMapping transportMapping;
    private Snmp snmp;
    private PDU pdu;
    private CommunityTarget communityTarget;

    public Discover() throws IOException {
        transportMapping = new DefaultUdpTransportMapping();
        transportMapping.listen();

        communityTarget = new CommunityTarget();
        communityTarget.setCommunity(new OctetString(community));
        communityTarget.setVersion(SnmpConstants.version2c);
        communityTarget.setAddress(new UdpAddress("127.0.0.1/5555"));
        communityTarget.setRetries(1);
        communityTarget.setTimeout(10);

        pdu = new PDU();
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1.0")));
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.6.0")));
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));

        snmp = new Snmp(transportMapping);
    }

    public List<Client> searchClients(String address, String port, Integer timeout, JProgressBar searchProgress) throws IOException, InterruptedException {
        communityTarget.setRetries(1);
        communityTarget.setTimeout(timeout);

        String nums[] = address.split(Pattern.quote("."));

        List<Client> clientList = new ArrayList<>();

        for (int i = 1; i <= 254; i++) {
            searchProgress.setValue(i);
            String probableClient = nums[0] + "." + nums[1] + "." + nums[2] + "." + Integer.toString(i) + "/" + port;

            communityTarget.setAddress(new UdpAddress(probableClient));
            ResponseEvent responseEvent = snmp.get(pdu, communityTarget);
            if (responseEvent != null) {
                PDU responsePDU = responseEvent.getResponse();
                if (responsePDU != null) {
                    int errorStatus = responsePDU.getErrorStatus();
                    if (errorStatus == PDU.noError) {
                        Client client = new Client();
                        client.setAddress(probableClient);
                        if (responsePDU.get(0).getOid().equals(new OID("1.3.6.1.2.1.1.1.0"))) {
                            client.setSystemDescription(String.valueOf(responsePDU.get(0).getVariable().toString()));
                        } else {
                            client.setSystemLocation(String.valueOf(responsePDU.get(0).getVariable().toString()));
                        }

                        if (responsePDU.get(1).getOid().equals(new OID("1.3.6.1.2.1.1.1.0"))) {
                            client.setSystemDescription(String.valueOf(responsePDU.get(1).getVariable().toString()));
                        } else {
                            client.setSystemLocation(String.valueOf(responsePDU.get(1).getVariable().toString()));
                        }

                        client.setClientServiceList(new InfoProvider().getClientServices(client.getAddress()));
                        client.setNetworkInterfaceList(new InfoProvider().getNetworkInterfaces(client.getAddress()));
                        clientList.add(client);
                    }
                }
            }
        }

        return clientList;
    }

    public Client getClient(String address) throws IOException {
        communityTarget.setRetries(3);
        communityTarget.setTimeout(100);

        communityTarget.setAddress(new UdpAddress(address));
        ResponseEvent responseEvent = snmp.get(pdu, communityTarget);
        if (responseEvent != null) {
            PDU responsePDU = responseEvent.getResponse();
            if (responsePDU != null) {
                int errorStatus = responsePDU.getErrorStatus();
                if (errorStatus == PDU.noError) {
                    Client client = new Client();
                    client.setAddress(address);
                    if (responsePDU.get(0).getOid().equals(new OID("1.3.6.1.2.1.1.1.0"))) {
                        client.setSystemDescription(String.valueOf(responsePDU.get(0).getVariable().toString()));
                    } else {
                        client.setSystemLocation(String.valueOf(responsePDU.get(0).getVariable().toString()));
                    }

                    if (responsePDU.get(1).getOid().equals(new OID("1.3.6.1.2.1.1.1.0"))) {
                        client.setSystemDescription(String.valueOf(responsePDU.get(1).getVariable().toString()));
                    } else {
                        client.setSystemLocation(String.valueOf(responsePDU.get(1).getVariable().toString()));
                    }
                    client.setClientServiceList(new InfoProvider().getClientServices(client.getAddress()));
                    client.setNetworkInterfaceList(new InfoProvider().getNetworkInterfaces(client.getAddress()));
                    return client;
                }
            }
        }

        Client client = new Client();
        client.setAddress(address);
        client.setSystemDescription("Down");
        client.setSystemLocation("Down");
        return client;
    }
}
