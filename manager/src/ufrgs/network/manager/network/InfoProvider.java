package ufrgs.network.manager.network;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.*;
import ufrgs.network.manager.data.ClientService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 12/3/16.
 */
public class InfoProvider {

    private String community = "simple";
    private TransportMapping transportMapping;
    private Snmp snmp;
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

        snmp = new Snmp(transportMapping);
    }

    public List<ClientService> getClientServices(String address) throws IOException {
        communityTarget.setAddress(new UdpAddress(address));

        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(communityTarget, new OID("1.3.6.1.4.1.3"));
        if (events == null || events.size() == 0) {
            return null;
        }

        ClientService clientServiceArray[] = new ClientService[1000];

        for (TreeEvent event : events) {
            if (event != null) {
                if (event.isError()) {
                    System.err.println(event.getErrorMessage());
                    return null;
                }

                VariableBinding[] variableBindings = event.getVariableBindings();
                if (variableBindings == null || variableBindings.length == 0) {
                    System.err.println("no event returned");
                    return null;
                }

                for (VariableBinding variableBinding : variableBindings) {
                    if (variableBinding.getOid().startsWith(new OID("1.3.6.1.4.1.3.1.2"))) {
                        int id = variableBinding.getOid().get(9);
                        // serviceName
                        if (clientServiceArray[id] == null) {
                            clientServiceArray[id] = new ClientService();
                        }
                        clientServiceArray[id].setName(variableBinding.getVariable().toString());
                    } else if (variableBinding.getOid().startsWith(new OID("1.3.6.1.4.1.3.1.3"))) {
                        int id = variableBinding.getOid().get(9);
                        // serviceStatus
                        if (clientServiceArray[id] == null) {
                            clientServiceArray[id] = new ClientService();
                        }
                        clientServiceArray[id].setStatus(variableBinding.getVariable().toString());
                    } else if (variableBinding.getOid().startsWith(new OID("1.3.6.1.4.1.3.1.4"))) {
                        int id = variableBinding.getOid().get(9);
                        // upTime
                        if (clientServiceArray[id] == null) {
                            clientServiceArray[id] = new ClientService();
                        }
                        clientServiceArray[id].setUptime(variableBinding.getVariable().toString());
                    } else if (variableBinding.getOid().startsWith(new OID("1.3.6.1.4.1.3.1.5"))) {
                        int id = variableBinding.getOid().get(9);
                        // fullPath
                        if (clientServiceArray[id] == null) {
                            clientServiceArray[id] = new ClientService();
                        }
                        clientServiceArray[id].setPath(variableBinding.getVariable().toString());
                    }
                }
            }
        }

        List<ClientService> clientServiceList = new ArrayList<>();
        for (ClientService clientService : clientServiceArray) {
            if (clientService != null) {
                clientServiceList.add(clientService);
            }
        }
        return clientServiceList;
    }
}
