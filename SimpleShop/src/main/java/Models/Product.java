package Models;

import Database.Database;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @JsonProperty
    String productId;
    @JsonProperty
    String name;
    @JsonProperty
    String createdBy;
    @JsonProperty
    List<String> attributes;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }


    public static List<Product>  getProducts() throws  ClassNotFoundException, SQLException{

        PreparedStatement ps = Database.prepareStatement("SELECT * FROM myapp.products");
        ResultSet rs = ps.executeQuery();

        List<Product> products= new ArrayList<>();

        while(rs.next()){
            Product product = new Product();
            product.setProductId(rs.getString("product_id"));
           product.setName(rs.getString("name"));
            product.setCreatedBy(rs.getString("createdBy"));
            product.setAttributes(Arrays.asList((String[]) rs.getArray("attributes").getArray()));

           System.out.println(product.getAttributes());


            //product.setAttributes(rs.getString("attributes"));
            //map = (HashMap<String, String>) rs.getObject("attributes");
            //product.setAttributes(map);
            //product.setAttributes(rs.getString("attributes"));
            products.add(product);

        }
        return products;
    }
    public static Product addProduct(Product product)throws ClassNotFoundException, SQLException, JsonProcessingException
    {
        PreparedStatement ps = Database.prepareStatement("INSERT INTO products ( id,name, createdBy,attributes) VALUES(?,?,?,?)");
        //PreparedStatement ps1 = Database.prepareStatement("INSERT INTO attributes (product_id, data) VALUES(?,?)");
        String id = Database.generatorId(5);
        ObjectMapper objectMapper = new ObjectMapper();
        ps.setString(1, id);
        ps.setString(2,product.getName());
        ps.setString(3,product.getCreatedBy());
        ps.setObject(4,objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(product.attributes));
        //ps1.setString(1,id);

        //ps1.setObject(2,objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(product.getAttributes().getData().replaceAll("\\\\","")));
        ps.executeUpdate();
        //ps1.executeUpdate();
        return product;
    }

    public static Product getProduct(String id) throws ClassNotFoundException, SQLException{

        PreparedStatement ps = Database.prepareStatement("SELECT * FROM products WHERE products.id=?");
        ps.setString(1,id);
        ResultSet rs = ps.executeQuery();

        Product product = new Product();

        while(rs.next()){
            product.setProductId(rs.getString("id"));
            product.setName(rs.getString("name"));
            product.setCreatedBy(rs.getString("createdBy"));
           // product.setAttributes(rs.getString("attributes"));
        }
        return product;
    }

    public static void updateProduct(String productId, Product product) throws ClassNotFoundException, SQLException, IOException {

        PreparedStatement ps = Database.prepareStatement("UPDATE products SET name = ?, attributes = ? WHERE id= ?");
        ObjectMapper objectMapper = new ObjectMapper();
        ps.setString(1, product.getName());
        ps.setObject(2,objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(product.getAttributes()));
        ps.setString(3, productId);
       /* PreparedStatement ps1 = Database.prepareStatement("UPDATE products SET attributes = JSON_SET(attributes," +
                "  '$.boja' , ? ) WHERE id = ?");
        ps1.setString(2,product.getAttributes());
        ps1.setString(1,productId);*/
        ps.executeUpdate();
        ps.executeUpdate();

    }


    public static void deleteProduct(String productId)throws ClassNotFoundException, SQLException{
        PreparedStatement ps =Database.prepareStatement("DELETE FROM products WHERE id =?");
        ps.setString(1,productId);
        ps.executeUpdate();

    }
    public static PagginationWrapper search(SearchRequest searchRequest) throws SQLException,ClassNotFoundException{
        String search = searchRequest.getSearch();
        List<String> products = null != searchRequest.getProducts() ? searchRequest.getProducts() : new ArrayList<>();
        Integer page = searchRequest.getPage();
        Integer pageSize = searchRequest.getPageSize();

        if(page== null || pageSize==null){
            page = 1;
            pageSize=10;
        }

        Integer offset = (page - 1)*pageSize;
        Integer totalCount = null;

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM products"
        );

        if(search!=null){
            sql.append(" WHERE LOWER(name) LIKE ?");
        }

        sql.append(" ORDER BY id DESC LIMIT ");
        sql.append(pageSize);
        //sql.append(" ,");
        //sql.append(" OFFSET ");
        //sql.append(offset);

        PreparedStatement preparedStatement = Database.prepareStatement(sql.toString());

        if(search!=null){
            preparedStatement.setString(1,"%" + search.toLowerCase() + "%");
        }
        ResultSet rs = preparedStatement.executeQuery();

        List<Product> productList = new ArrayList<>();

        while(rs.next()){
            Product product = new Product();
            product.setName(rs.getString("name"));
            product.setCreatedBy("admin");
            productList.add(product);
        }
        PagginationWrapper pw = new PagginationWrapper();
        pw.setData(productList);
        pw.setPage(page);
        pw.setPageSize(pageSize);
        pw.setTotalCount(totalCount);

        return pw;
    }

}
