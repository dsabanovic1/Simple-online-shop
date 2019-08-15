package Controller;

import Database.Database;
import Models.*;
import Security.Secured;
import com.fasterxml.jackson.core.JsonProcessingException;
import enums.UserRights;
import helpers.ImageUpload;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/")
public class Controller {

    @POST
    @Path("/login")
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        try {
            TokenSession tokenSession = User.logIn(username, password);
            return Response.status(200).entity(tokenSession).build();

        } catch (ClassNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException | SQLException e) {
            return Response.status(500).build();

        } catch (ValidationException e) {
            return Response.status(401).build();
        }


    }


    @GET
    @Path("/products")
    //@Secured(UserRights.getProducts)
    @Produces({"application/json"})
    public Response getProduct() {
        try {
            List<Product> products = Product.getProducts();
            return Response.status(200).entity(products).build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(500).build();
        }
    }

    @GET
    @Path("/users")
    @Secured(UserRights.getUsers)
    @Produces("application/json")
    public Response getUsers() {
        try {
            List<User> users = User.getUsers();
            return Response.status(200).entity(users).build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(500).build();
        }
    }

    @POST
    @Path("/product")
    @Secured({UserRights.getProducts})
    @Consumes("application/json")
    @Produces("application/json")
    public Response addProduct(Product product) {
        try {
            return Response.status(200).entity(Product.addProduct(product)).build();

        } catch (ClassNotFoundException | JsonProcessingException | SQLException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }
    @POST
    @Path("/user")
    @Secured({UserRights.modifyUsers})
    @Produces("application/json")
    @Consumes("application/json")
    public Response addUser(User user) {
        try {
            return Response.status(200).entity(User.addUser(user)).build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(500).build();
        }

    }

    @GET
    @Path("/product/{id}")
    @Secured(UserRights.getProducts)
    @Produces("application/json")
    public Response getProduct(@PathParam("id") String productId) {
        try {
            return Response.status(200).entity(Product.getProduct(productId)).build();

        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(500).build();

        }
    }

    @PUT
    @Path("/product/{id}")
    @Secured(UserRights.modifyProducts)
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateProduct(@PathParam("id") String productId, Product product) {
        try {
            Product.updateProduct(productId, product);
            return Response.status(200).build();

        } catch (ClassNotFoundException | IOException | SQLException e) {
            return Response.status(500).build();
        }

    }

    @DELETE
    @Path("/product/{id}")
    @Secured(UserRights.modifyProducts)
    @Produces("application/json")
    @Consumes("application/json")
    public Response deleteProduct(@PathParam("id") String productId) {
        try {
            Product.deleteProduct(productId);
            return Response.status(200).build();
        } catch (ClassNotFoundException | SQLException e) {

            return Response.status(500).build();

        }


    }

    @POST
    @Path("/upload")
    @Produces("application/json")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input){

        String fileName = "C:\\Users\\Public\\Pictures";
        List<String> result = new ArrayList<>();

        Map<String, List<InputPart>> formParts = input.getFormDataMap();

        List<InputPart> inPart = formParts.get("file");

        for(InputPart inputPart : inPart)
        {
            try{
                InputStream iStream = inputPart.getBody(InputStream.class,null);

                fileName = fileName + Database.generatorId(5) + ".png";

                ImageUpload.saveFile(iStream,fileName);
                }
            catch(IOException e){
                return Response.status(500).entity(e.getMessage()).build();
            }
        }
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/search")
    @Consumes("application/json")
    @Produces("application/json")
    public Response search(SearchRequest searchRequest){
        try{
            return Response.status(200).entity(Product.search(searchRequest)).build();
        }catch(ClassNotFoundException | SQLException e){
            return Response.status(500).entity(e.getMessage()).build();

        }
    }


}