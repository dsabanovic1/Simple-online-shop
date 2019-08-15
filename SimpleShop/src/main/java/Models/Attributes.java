package Models;

import Database.Database;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes {

    @JsonProperty
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty
    String value;



/*
    public static Attributes addAttributes(Attributes attributes)throws ClassNotFoundException, SQLException, JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();

        PreparedStatement ps1 = Database.prepareStatement("INSERT INTO attributes (product_id, data) VALUES" +
                "(?, ?)");
        ps1.setString(1,attributes.getProduct_id());
        ps1.setObject(2,objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(attributes.getData()));
        ps1.executeUpdate();
        return attributes;
    }
    public static List<Attributes> getAttributes() throws  ClassNotFoundException, SQLException{

        PreparedStatement ps = Database.prepareStatement("SELECT * FROM attributes");
        ResultSet rs = ps.executeQuery();

        List<Attributes> attributes= new ArrayList<>();

        while(rs.next()){
            Attributes attributes1 = new Attributes();
            attributes1.setProduct_id(rs.getString(1));
            attributes1.setData(rs.getString(2).replaceAll("\\\\",""));


            attributes.add(attributes1);
        }
        return attributes;
    }
    public static void updateAttributes(String productId, Attributes attributes) throws ClassNotFoundException, SQLException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map = mapper.readValue(attributes.getData(), Map.class);
        PreparedStatement ps1= Database.prepareStatement("UPDATE attributes SET data" +
                " = JSON_SET(data, '$.boja' , ? , '$.godiste', ?) WHERE product_id = ?");
        //Set<String> keys = map.keySet();
        /*int i = 1;
        for (String key: keys) {
            ps1.setString(i,map.get("boja"));
            i++;
        }
        ps1.setString(1,map.get("boja"));
        ps1.setString(2,map.get("godiste"));
        ps1.setString(3,productId);
        ps1.executeUpdate();
    }*/
}
