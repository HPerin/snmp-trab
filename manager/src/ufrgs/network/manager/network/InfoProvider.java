package ufrgs.network.manager.network;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import ufrgs.network.manager.data.ClientServices;

import java.io.IOException;
import java.util.List;

/**
 * Created by lucas on 12/3/16.
 */
public class InfoProvider {

    private String community = "simple";
    private TransportMapping transportMapping;
    private Snmp snmp;
    private PDU pdu;
    private CommunityTarget communityTarget;

    public InfoProvider() throws IOException {
        transportMapping = new DefaultUdpTransportMapping();
        transportMapping.listen();

        communityTarget = new CommunityTarget();
        communityTarget.setCommunity(new OctetString(community));
        communityTarget.setVersion(SnmpConstants.version2c);
        communityTarget.setAddress(new UdpAddress("127.0.0.1/5555"));
        communityTarget.setRetries(2);
        communityTarget.setTimeout(1000);

        pdu = new PDU();
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1.0")));
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));

        snmp = new Snmp(transportMapping);
    }

    public List<ClientServices> getClientServices(String address) {
        communityTarget.setAddress(new UdpAddress(address));

        PDUFactory pduFactory = new DefaultPDUFactory(PDU.GETNEXT);
        TableUtils tableUtils = new TableUtils(snmp, pduFactory);

        OID[] columns = new OID[4];
        columns[0] = new VariableBinding(new OID("1.3.6.1.4.1.3.1.2")).getOid();
        columns[1] = new VariableBinding(new OID("1.3.6.1.4.1.3.1.3")).getOid();
        columns[2] = new VariableBinding(new OID("1.3.6.1.4.1.3.1.4")).getOid();
        columns[3] = new VariableBinding(new OID("1.3.6.1.4.1.3.1.5")).getOid();

        OID lowerBoundIndex = new OID("1.3.6.1.4.1.3.1");
        OID upperBoundIndex = new OID("1.3.6.1.4.1.3.2");

        List<TableEvent> snmpList = tableUtils.getTable(communityTarget, columns, lowerBoundIndex, upperBoundIndex);

        System.out.println(snmpList.size());

        for (TableEvent tableEvent : snmpList) {
            System.out.println(tableEvent.getErrorMessage());
        }

        return null;
    }
}
