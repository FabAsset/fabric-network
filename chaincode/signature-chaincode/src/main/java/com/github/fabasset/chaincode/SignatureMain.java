package com.github.fabasset.chaincode;

import com.github.fabasset.chaincode.Main;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.io.IOException;
import java.util.List;

import static com.github.fabasset.chaincode.constant.Message.ARG_MESSAGE;
import static io.netty.util.internal.StringUtil.isNullOrEmpty;

public class SignatureMain extends Main {
    private static final String SIGN_FUNCTION_NAME = "sign";
    private static final String FINALIZE_FUNCTION_NAME = "finalize";

    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            String func = stub.getFunction();
            List<String> args = stub.getParameters();
            String response;

            switch (func) {
                case SIGN_FUNCTION_NAME:
                    response = sign(stub, args);
                    break;

                case FINALIZE_FUNCTION_NAME:
                    response = finalize(stub, args);
                    break;

                default:
                    return super.invoke(stub);
            }

            return ResponseUtils.newSuccessResponse(response);
        } catch (Exception e) {
            return ResponseUtils.newErrorResponse(e.getMessage());
        }
    }

    private String sign(ChaincodeStub stub, List<String> args) throws IOException {
        if (args.size() != 2 || isNullOrEmpty(args.get(0)) || isNullOrEmpty(args.get(1))) {
            throw new IllegalArgumentException(String.format(ARG_MESSAGE, "2"));
        }

        String contractId = args.get(0);
        String sigId = args.get(1);

        return Boolean.toString(Signature.sign(stub, contractId, sigId));
    }

    private String finalize(ChaincodeStub stub, List<String> args) throws IOException {
        if (args.size() != 1 || isNullOrEmpty(args.get(0))) {
            throw new IllegalArgumentException(String.format(ARG_MESSAGE, "1"));
        }

        String id = args.get(0);

        return Boolean.toString(Signature.finalize(stub, id));
    }

    public static void main(String[] args) {
        new SignatureMain().start(args);
    }
}