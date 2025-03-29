import java.io.Serializable;
import java.util.ArrayList;

// This class extends ArrayList<Customer> to prevent serialization warnings
public class CustomerList extends ArrayList<Customer> implements Serializable {
    // This is just a wrapper class to prevent serialization warnings
    // No additional functionality needed
}