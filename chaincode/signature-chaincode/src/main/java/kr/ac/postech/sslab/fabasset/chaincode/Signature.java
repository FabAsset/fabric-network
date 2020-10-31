package kr.ac.postech.sslab.fabasset.chaincode;

import com.github.fabasset.chaincode.client.Address;
import com.github.fabasset.chaincode.protocol.ERC721;
import com.github.fabasset.chaincode.protocol.Extension;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.io.IOException;
import java.util.List;

public class Signature {
    private static final String SIGNERS_KEY = "signers";

    private static final String SIGNATURES_KEY = "signatures";

    @SuppressWarnings("unchecked")
    public static boolean sign(ChaincodeStub stub, String id, String signature) throws IOException {
        String caller = Address.getMyAddress(stub);
        String owner = ERC721.ownerOf(stub, id);
        if (!caller.equals(owner)) {
            return false;
        }

        List<String> signers = (List<String>) Extension.getXAttr(stub, id, SIGNERS_KEY);

        List<String> signatures = (List<String>) Extension.getXAttr(stub, id, SIGNATURES_KEY);
        int num_of_signatures = signatures.size();

        if (isFinalized(stub, id)) {
            return false;
        }

        if (!caller.equals(signers.get(num_of_signatures))) {
            return false;
        }

        if (!caller.equals(ERC721.ownerOf(stub, signature))) {
            return false;
        }

        signatures.add(signature);
        Extension.setXAttr(stub, id, SIGNATURES_KEY, signatures);

        return true;
    }

    @SuppressWarnings("unchecked")
    public static boolean isFinalized(ChaincodeStub stub, String id) throws IOException {
        List<String> signers = (List<String>) Extension.getXAttr(stub, id, SIGNERS_KEY);
        int num_of_signers = signers.size();

        List<String> signatures = (List<String>) Extension.getXAttr(stub, id, SIGNATURES_KEY);
        int num_of_signatures = signatures.size();

        return num_of_signers <= num_of_signatures;
    }
}