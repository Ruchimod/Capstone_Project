package Capstone;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Aadhar_Validation {

        Lookup reuse;
        Connection dbconnection;
        String adharfromprop;
        String Getfname;
        String Getaadharnumber;
        String Getrunaddress ;
        String Getlname ;
        String GetPhone;

        String requrl="https://reqres.in/api/users";
        int id;
        
        @BeforeClass
        public void setup()
        {

            reuse = new Lookup();
        }

   @Test(priority = 1)
    void create_table()
    {
        try
        {

            //URL, DB User Name , DB Password
            dbconnection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306", "root", "Bunny@2024");
            if (dbconnection != null)
            {
                System.out.println("Database server is connected");
            }

            Statement stamnt = dbconnection.createStatement();
            stamnt.execute("use capstone");


            // Create an empty Table

           stamnt.execute("create table new_capstone\n" +
                    "(Fname varchar(20) ,\n" +
                    "Lname varchar(20) ,\n" +
                    "Aadhar_No varchar(50) , \n" +
                   "Address varchar(50) , \n" +
                    "phone_no  varchar(10) ) ");


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

  @Test(priority = 2)
     void insertintotable()
    {
        try
        {

            //URL, DB User Name , DB Password
            dbconnection=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306", "root", "Bunny@2024");
            if (dbconnection != null)
            {
                System.out.println("Database server is connected");
            }


            Statement stamnt = dbconnection.createStatement();
            // Use Database where we need to create a new Table

            stamnt.execute("use capstone");



            stamnt.execute("Insert into new_capstone(Fname,Lname,Aadhar_No,Address,phone_no) values " +
                    "('Shashi','Kapoor','978645429016','Bangalore','8794634725') ,\n" +
                    "('Rohit','Anand','569656356352','Mumbai','7653221197'),\n" +
                    "('Nancy','Priya','675736563563','Durgapur','5646342300'),\n" +
                    "('Shipra','Verma','865335657536','Dhanbad','7564624534');");



            System.out.println("Record Inserted");






        }
        catch (Exception e)
        {
            System.out.println("URL , User name or Password is wrong");
        }

    }
        @Test(priority = 3)
        void readadharfrompropertiesfile()
        {


            try {

                String path = "C:/Users/ruchim/IdeaProjects/Capstone/Aadhar_detail.properties";

                Properties prt;
                prt = new Properties();
                FileInputStream fip = new FileInputStream(path);
                prt.load(fip);
                adharfromprop = prt.getProperty("aadhar");

                System.out.println("aadhar ::>" + adharfromprop);

            }
            catch(Exception e){
                System.out.println("error" + e);
            }
        }

        @Test(priority = 2)
        void select_datafromtable()
        {
            try
            {

                //URL, DB User Name , DB Password
                dbconnection=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306", "root", "Bunny@2024");
                if (dbconnection != null)
                {
                    System.out.println("Database server is connected");
                }


                Statement stamnt = dbconnection.createStatement();
                // Use Database where we need to create a new Table

                stamnt.execute("use capstone");

                ResultSet result= stamnt.executeQuery("SELECT Fname,Lname,Aadhar_No,Address,phone_no FROM capstone.new_capstone\n" +
                        "where Aadhar_No=\""+adharfromprop+"\";");


                while (result.next())
                {
                    Getaadharnumber =result.getString("Aadhar_No");
                    Getrunaddress =result.getString("Address");
                    Getfname =result.getString("Fname");
                    Getlname =result.getString("Lname");
                    GetPhone =result.getString("phone_no");
                }

                System.out.println("Name is : "+ Getfname+ "   " +  Getlname+ " from  "  + Getrunaddress+ " City  "
                        +"Aadhar number is :"+ Getaadharnumber
                        +" Phone number is : "+ GetPhone
                );


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Test(priority = 4)
        void createUserinAPIusingproperiesfile()
        {

            Response res = given()
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                            "    \"Fname\": \""+Getfname+"\",\n" +
                            "    \"Lname\": \""+Getlname+"\",\n" +
                            "    \"Aadhar_No\": \""+adharfromprop+"\",\n" +
                            "    \"Address\": \""+Getrunaddress+"\",\n" +
                            "    \"Phone\": \""+GetPhone+"\",\n" +
                            "\"accountno\":\"810956822\"\n" +
                            "\n" +
                            "}")
                    .log().all()
                    .when()
                    .post(requrl);

        }
    }

