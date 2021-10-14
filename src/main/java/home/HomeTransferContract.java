package home;

import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import com.owlike.genson.Genson;

// define the contract
@Contract(name = "HomeTransfer", info = @Info(title = "HomeTransfer contract", description = "A Sample home transfer chaincode example", version = "0.0.1-SNAPSHOT"))

// implements smart contracts
@Default
public final class HomeTransferContract implements ContractInterface {

    // define genson instance and some error codes for serializing and deserializing
    // json messages
    private final Genson genson = new Genson();

    private enum HomeTransferErrors {
        HOME_NOT_FOUND, HOME_ALREADY_EXISTS
    }

    // initialize chaincode java contract (all chaincode functions should have the
    // transaction annotation)
    /**
     * Add some initial properties to the ledger
     * 
     * @param ctx the transaction context
     */
    @Transaction()
    public void initLedger(final Context ctx) {
        // define chaincode stub to connect to the ledger
        ChaincodeStub stub = ctx.getStub();
        // create a new isntance of the home file and pass some data into the home
        // constructor - so when contract is initialized this home is added by default
        home home = new home("1", "FirstHome", "2000", "Mark", "6756");
        // use genson function to serialize into json format
        String homeState = genson.serialize(home);
        // putStringState method writes to the ledger - key value pair so you're
        // serializing what you created in the new instance of home
        stub.putStringState("1", homeState);
    }

    // function that allows you to add a new home to the ledger
    /**
     * Add a new home on the ledger
     * 
     * @param ctx       the transaction context
     * @param id        the key for the new home
     * @param name      the name of the new home
     * @param area      the area of the new home
     * @param ownerName the owner of the new home
     * @param value     the value of the new home
     * @return the created home
     */

    @Transaction()
    public home addNewHome(final Context ctx, final String id, final String name, final String area,
            final String ownerName, final String value) {
        ChaincodeStub stub = ctx.getStub();

        // getStringState mehtod reads from the ledger
        String homeState = stub.getStringState(id);
        if (!homeState.isEmpty()) {
            String errorMessage = String.format("Home %s already exists", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HomeTransferErrors.HOME_ALREADY_EXISTS.toString());
        }

        home home = new home(id, name, area, ownerName, value);
        homeState = genson.serialize(home);
        stub.putStringState(id, homeState);
        return home;
    }

    // function that allows you to query the ledger based on a key
    /**
     * Retrieves a home based on home id from the ledger
     * 
     * @param ctx the transaction context
     * @param id  the key
     * @return the home from the ledger
     */

    @Transaction()
    public home queryHomeById(final Context ctx, final String id) {
        ChaincodeStub stub = ctx.getStub();
        String homeState = stub.getStringState(id);

        if (homeState.isEmpty()) {
            String errorMessage = String.format("Home %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HomeTransferErrors.HOME_NOT_FOUND.toString());
        }

        home home = genson.deserialize(homeState, home.class);
        return home;
    }

    // Transfer the ownership of the home
    /**
     * Changes the owner of the home on the ledger
     * 
     * @param ctx      the transaction context
     * @param id       the key
     * @param newOwner the new owner
     * @return the updated home ownership
     */

    @Transaction()
    public home changeHomeOwnership(final Context ctx, final String id, final String newHomeOwner) {
        ChaincodeStub stub = ctx.getStub();

        String homeState = stub.getStringState(id);

        if (homeState.isEmpty()) {
            String errorMessage = String.format("Home %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HomeTransferErrors.HOME_NOT_FOUND.toString());
        }

        home home = genson.deserialize(homeState, home.class);

        home newHome = new home(home.getId(), home.getName(), home.getArea(), newHomeOwner, home.getValue());
        String newHomeState = genson.serialize(newHome);
        stub.putStringState(id, newHomeState);

        return newHome;
    }

}
