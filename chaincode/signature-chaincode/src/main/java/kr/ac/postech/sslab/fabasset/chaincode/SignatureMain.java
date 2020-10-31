package kr.ac.postech.sslab.fabasset.chaincode;

import com.github.fabasset.chaincode.Main;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;

import java.io.IOException;
import java.util.List;

import static com.github.fabasset.chaincode.constant.Message.ARG_MESSAGE;
import static io.netty.util.internal.StringUtil.isNullOrEmpty;

public class SignatureMain extends Main {
    private static final String SIGN_FUNCTION_NAME = "sign";
    private static final String IS_FINALIZED_FUNCTION_NAME = "isFinalized";

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

                case IS_FINALIZED_FUNCTION_NAME:
                    response = isFinalized(stub, args);
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

        String id = args.get(0);
        String signature = args.get(1);

        return Boolean.toString(Signature.sign(stub, id, signature));
    }

    private String isFinalized(ChaincodeStub stub, List<String> args) throws IOException {
        if (args.size() != 1 || isNullOrEmpty(args.get(0))) {
            throw new IllegalArgumentException(String.format(ARG_MESSAGE, "1"));
        }

        String id = args.get(0);

        return Boolean.toString(Signature.isFinalized(stub, id));
    }

    public static void main(String[] args) {
        new SignatureMain().start(args);
    }
}