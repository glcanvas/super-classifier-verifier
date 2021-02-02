#include <core.p4>
#include <v1model.p4>

header generated_headers_t {
// insert fields here
 %s
}
// for correct compile only
header ethernet_t {
    bit<16> etherType;
}
struct headers {
    ethernet_t   ethernet;
    generated_headers_t generated_headers;
}
struct metadata {
}
// for correct compile only
parser MyParser(packet_in packet,
                out headers hdr,
                inout metadata meta,
                inout standard_metadata_t standard_metadata) {
    state start {
        packet.extract(hdr.ethernet);
        transition select(hdr.ethernet.etherType) {
            default      : accept;
        }
    }
}

control MyIngress(inout headers hdr,
                  inout metadata meta,
                  inout standard_metadata_t standard_metadata) {
    // generate actions here
    %s
    action operation_drop() {
        mark_to_drop(standard_metadata);
    }
    
    table calculate {
        key = {
        // generate headers
            %s
        }
        actions = {
           // generate list of actions
            %s
            operation_drop;
        }
        const default_action = operation_drop();
        const entries = {
            // generate actions set
            %s
        }
    }

    apply {
        calculate.apply();
    }
}

//dummy
control MyVerifyChecksum(inout headers hdr,
                         inout metadata meta) {
    apply {}
}
control MyEgress(inout headers hdr,
                 inout metadata meta,
                 inout standard_metadata_t standard_metadata) {
    apply {}
}
control MyComputeChecksum(inout headers hdr, inout metadata meta) {
    apply {}
}
control MyDeparser(packet_out packet, in headers hdr) {
    apply {}
}

V1Switch(
MyParser(),
MyVerifyChecksum(),
MyIngress(),
MyEgress(),
MyComputeChecksum(),
MyDeparser()
) main;





