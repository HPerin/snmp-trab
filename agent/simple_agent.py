import sys, os, signal
import optparse
import pprint

sys.path.insert(0, os.path.dirname(os.getcwd()))
import netsnmpagent

prgname = sys.argv[0]

parser = optparse.OptionParser()
parser.add_option(
    "-m",
    "--mastersocket",
    dest="mastersocket",
    help="Sets the transport specification for the master agent's AgentX socket",
    default="/var/agentx/master"
)
parser.add_option(
    "-p",
    "--persistencedir",
    dest="persistencedir",
    help="Sets the path to the persistence directory",
    default="/var/lib/net-snmp"
)

(options, args) = parser.parse_args()

# rows, columns = os.popen("stty size", "r").read().split()
rows = 10
columns = 80

try:
    agent = netsnmpagent.netsnmpAgent(
        AgentName="SERVICE-HEALTH",
        MasterSocket=options.mastersocket,
        PersistenceDir=options.persistencedir,
        MIBFiles=[os.path.abspath(os.path.dirname(sys.argv[0])) +
                  "/service.mib"]
    )
except netsnmpagent.netsnmpAgentException as e:
    print("{0}: {1}".format(prgname, e))
    sys.exit(1)

serviceTable = agent.Table(
    oidstr="SERVICE-HEALTH::serviceTable",
    indexes=[
        agent.Integer32()
    ],
    columns=[
        (2, agent.OctetString("UnknownName")),
        (3, agent.OctetString("UnknownState")),
        (4, agent.OctetString("")),
        (5, agent.OctetString(""))
    ],
    counterobj=agent.Integer32(
        oidstr="SERVICE-HEALTH::serviceTableCounter"
    ),
    extendable=True
)

serviceTableSnmpd = serviceTable.addRow([agent.Integer32(0)])
serviceTableSnmpd.setRowCell(2, agent.OctetString("snmpd"))
serviceTableSnmpd.setRowCell(3, agent.OctetString(""))
serviceTableSnmpd.setRowCell(4, agent.OctetString(""))
serviceTableSnmpd.setRowCell(5, agent.OctetString(""))

serviceTableHttpd = serviceTable.addRow([agent.Integer32(1)])
serviceTableHttpd.setRowCell(2, agent.OctetString("httpd"))
serviceTableHttpd.setRowCell(3, agent.OctetString(""))
serviceTableHttpd.setRowCell(4, agent.OctetString(""))
serviceTableHttpd.setRowCell(5, agent.OctetString(""))

simpleCounter = agent.Integer32(
    oidstr="SERVICE-HEALTH::simpleCounter",
    writable=True
)

try:
    agent.start()
except netsnmpagent.netsnmpAgentException as e:
    print("{0}: {1}".format(prgname, e))
    sys.exit(1)

print("{0}: AgentX connection to snmpd established.".format(prgname))


def DumpRegistered():
    for context in agent.getContexts():
        print("{0}: Registered SNMP objects in Context \"{1}\": ".format(prgname, context))
        vars = agent.getRegistered(context)
        pprint.pprint(vars, width=columns)
        print


DumpRegistered()


def TermHandler(signum, frame):
    global loop
    loop = False


signal.signal(signal.SIGINT, TermHandler)
signal.signal(signal.SIGTERM, TermHandler)


def HupHandler(signum, frame):
    DumpRegistered()


signal.signal(signal.SIGHUP, HupHandler)

print("{0}: Serving SNMP requests, send SIGHUP to dump SNMP object state, press ^C to terminate...".format(prgname))

loop = True
import subprocess
while (loop):
    # Block and process SNMP requests, if available
    agent.check_and_process()

    output = subprocess.Popen(["bash", "check_service.sh", "snmpd"], stdout=subprocess.PIPE).communicate()[0]
    output = output.split('\n', -1)
    if output[0] == '1':
        serviceTableSnmpd.setRowCell(3, agent.OctetString("running"))
        serviceTableSnmpd.setRowCell(4, agent.OctetString(output[2]))
        serviceTableSnmpd.setRowCell(5, agent.OctetString(output[3]))
    else:
        serviceTableSnmpd.setRowCell(3, agent.OctetString("stopped"))

    output = subprocess.Popen(["bash", "check_service.sh", "httpd"], stdout=subprocess.PIPE).communicate()[0]
    output = output.split('\n', -1)
    if output[0] == '1':
        serviceTableHttpd.setRowCell(3, agent.OctetString("running"))
        serviceTableHttpd.setRowCell(4, agent.OctetString(output[2]))
        serviceTableHttpd.setRowCell(5, agent.OctetString(output[3]))
    else:
        serviceTableHttpd.setRowCell(3, agent.OctetString("stopped"))

print("{0}: Terminating.".format(prgname))
agent.shutdown()
