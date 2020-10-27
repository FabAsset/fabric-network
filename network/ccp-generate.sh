#!/bin/bash

function one_line_pem {
    echo "`awk 'NF {sub(/\\n/, ""); printf "%s\\\\\\\n",$0;}' $1`"
}

function json_ccp {
    local PP=$(one_line_pem $5)
    local CP=$(one_line_pem $6)
    sed -e "s/\${ORG}/$1/" \
        -e "s/\${P}/$2/" \
        -e "s/\${P0PORT}/$3/" \
        -e "s/\${CAPORT}/$4/" \
        -e "s#\${PEERPEM}#$PP#" \
        -e "s#\${CAPEM}#$CP#" \
        ccp-template.json 
}

function yaml_ccp {
    local PP=$(one_line_pem $5)
    local CP=$(one_line_pem $6)
    sed -e "s/\${ORG}/$1/" \
        -e "s/\${P}/$2/" \
        -e "s/\${P0PORT}/$3/" \
        -e "s/\${CAPORT}/$4/" \
        -e "s#\${PEERPEM}#$PP#" \
        -e "s#\${CAPEM}#$CP#" \
        ccp-template.yaml | sed -e $'s/\\\\n/\\\n        /g'
}

ORG=0
P=0
P0PORT=7051
CAPORT=7054
PEERPEM=crypto-config/peerOrganizations/org0.example.com/tlsca/tlsca.org0.example.com-cert.pem
CAPEM=crypto-config/peerOrganizations/org0.example.com/ca/ca.org0.example.com-cert.pem

echo "$(json_ccp $ORG $P $P0PORT $CAPORT $PEERPEM $CAPEM)" > connection-org0.json
echo "$(yaml_ccp $ORG $P $P0PORT $CAPORT $PEERPEM $CAPEM)" > connection-org0.yaml

ORG=1
P=1
P0PORT=8051
CAPORT=8054
PEERPEM=crypto-config/peerOrganizations/org1.example.com/tlsca/tlsca.org1.example.com-cert.pem
CAPEM=crypto-config/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem

echo "$(json_ccp $ORG $P $P0PORT $CAPORT $PEERPEM $CAPEM)" > connection-org1.json
echo "$(yaml_ccp $ORG $P $P0PORT $CAPORT $PEERPEM $CAPEM)" > connection-org1.yaml

ORG=2
P=2
P0PORT=9051
CAPORT=9054
PEERPEM=crypto-config/peerOrganizations/org2.example.com/tlsca/tlsca.org2.example.com-cert.pem
CAPEM=crypto-config/peerOrganizations/org2.example.com/ca/ca.org2.example.com-cert.pem

echo "$(json_ccp $ORG $P $P0PORT $CAPORT $PEERPEM $CAPEM)" > connection-org2.json
echo "$(yaml_ccp $ORG $P $P0PORT $CAPORT $PEERPEM $CAPEM)" > connection-org2.yaml
