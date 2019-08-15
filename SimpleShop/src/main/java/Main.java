import Models.Product;
import Models.TokenSession;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, ValidationException {
        //System.out.println(TokenSession.generateSessionToken("2").getToken());

        List<Product> proizvodi = Product.getProducts();
        for (Product p: proizvodi) {

           // System.out.println(p.getAttributes());
        }

    }
}