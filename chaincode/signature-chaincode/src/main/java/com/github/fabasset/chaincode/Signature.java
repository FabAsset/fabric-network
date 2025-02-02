package com.github.fabasset.chaincode;

import com.github.fabasset.chaincode.client.Address;
import com.github.fabasset.chaincode.protocol.ERC721;
import com.github.fabasset.chaincode.protocol.Extension;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.io.IOException;
import java.util.List;

public class Signature {
    private static final String SIGNERS_KEY = "signers";

    private static final String SIGNATURES_KEY = "signatures";

    private static final String FINALIZED_KEY = "finalized";

    @SuppressWarnings("unchecked")
    public static boolean sign(ChaincodeStub stub, String contractId, String sigId) throws IOException {
        String caller = Address.getMyAddress(stub);
        String owner = ERC721.ownerOf(stub, contractId);
        if (!caller.equals(owner)) {
            return false;
        }

        boolean isFinalized = (boolean) Extension.getXAttr(stub, contractId, FINALIZED_KEY);
        if (isFinalized) {
            return false;
        }

        List<String> signatures = (List<String>) Extension.getXAttr(stub, contractId, SIGNATURES_KEY);
        List<String> signers = (List<String>) Extension.getXAttr(stub, contractId, SIGNERS_KEY);
        if (!caller.equals(signers.get(signatures.size()))) {
            return false;
        }

        if (!caller.equals(ERC721.ownerOf(stub, sigId))) {
            return false;
        }

        signatures.add(sigId);
        Extension.setXAttr(stub, contractId, SIGNATURES_KEY, signatures);

        return true;
    }

    @SuppressWarnings("unchecked")
    public static boolean finalize(ChaincodeStub stub, String id) throws IOException {
        String caller = Address.getMyAddress(stub);
        String owner = ERC721.ownerOf(stub, id);
        if (!caller.equals(owner)) {
            return false;
        }

        List<String> signers = (List<String>) Extension.getXAttr(stub, id, SIGNERS_KEY);
        List<String> signatures = (List<String>) Extension.getXAttr(stub, id, SIGNATURES_KEY);

        if (signers.size() > signatures.size()) {
            return false;
        }

        Extension.setXAttr(stub, id, FINALIZED_KEY, true);
        return true;
    }
}