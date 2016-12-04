package ufrgs.network.manager.network;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by lucas on 12/4/16.
 */
public class InfoUpdater {

    private String community = "simple";
    private TransportMapping transportMapping;
    private Snmp snmp;
    private CommunityTarget communityTarget;
    private PDU pdu;

    public InfoUpdater() throws IOException {
        transportMapping = new DefaultUdpTransportMapping();
        transportMapping.listen();

        communityTarget = new CommunityTarget();
        communityTarget.setCommunity(new OctetString(community));
        communityTarget.setVersion(SnmpConstants.version2c);
        communityTarget.setAddress(new UdpAddress("127.0.0.1/5555"));
        communityTarget.setRetries(2);
        communityTarget.setTimeout(1000);

        pdu = new PDU();
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.6.0")));
        pdu.setType(PDU.SET);
        pdu.setRequestID(new Integer32(1));

        snmp = new Snmp(transportMapping);
    }

    public boolean updateSystemLocation(String address, String newSystemLocation) throws IOException {
        communityTarget.setAddress(new UdpAddress(address));

        pdu.clear();
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.6.0"), new OctetString(newSystemLocation)));

        ResponseEvent response = snmp.set(pdu, communityTarget);
        if (response != null) {
            PDU responsePDU = response.getResponse();
            if (responsePDU != null) {
                int errorStatus = responsePDU.getErrorStatus();
                int errorIndex = responsePDU.getErrorIndex();
                String errorStatusText = responsePDU.getErrorStatusText();

                if (errorStatus == PDU.noError) {
                    JOptionPane.showMessageDialog(null, responsePDU.getVariableBindings());
                    return true;
                }
            }
        }

        return false;
    }
}
