package ufrgs.network.manager.network;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultSshTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import ufrgs.network.manager.data.Client;

import java.io.IOException;
import java.util.*;
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
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));

        snmp = new Snmp(transportMapping);
    }

    public List<Client> searchClients(String address, String port) throws IOException {
        String nums[] = address.split(Pattern.quote("."));

        List<Client> clientList = new ArrayList<>();

        for (int i = 1; i <= 254; i++) {
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
                        client.setSystemDescription(String.valueOf(responsePDU.get(0).getVariable().toString()));
                        client.setClientServiceList(new InfoProvider().getClientServices(client.getAddress()));
                        clientList.add(client);
                    }
                }
            }
        }

        return clientList;
    }

    public Client getClient(String address) throws IOException {
        communityTarget.setAddress(new UdpAddress(address));
        ResponseEvent responseEvent = snmp.get(pdu, communityTarget);
        if (responseEvent != null) {
            PDU responsePDU = responseEvent.getResponse();
            if (responsePDU != null) {
                int errorStatus = responsePDU.getErrorStatus();
                if (errorStatus == PDU.noError) {
                    Client client = new Client();
                    client.setAddress(address);
                    client.setSystemDescription(String.valueOf(responsePDU.get(0).getVariable().toString()));
                    client.setClientServiceList(new InfoProvider().getClientServices(client.getAddress()));
                    return client;
                }
            }
        }

        Client client = new Client();
        client.setAddress(address);
        client.setSystemDescription("Down");
        return client;
    }
}
