/**
 * 
 */
package HireEngine.integration.email;

import org.springframework.stereotype.Service;

/**
 * @author Vinod Rockson
 *
 */
@Service
public class InvoiceManager {
    public void processInvoice(String invoice) {
        System.out.println("Inside process Invoice");
        System.out.println("Invoice (XML document)\n" + invoice);
    }
}