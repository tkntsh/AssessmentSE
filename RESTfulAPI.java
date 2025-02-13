import spark.Spark;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RESTfulAPI
{
    //final variables
    private static final String dbUrl = "jdbc:sqlite:testdata.db";
    private static final Gson gson = new Gson();

    public static void main(String[] args)
    {
        //starting server
        Spark.port(4567);
        //POST endpoint adding new scores
        Spark.post("/scores", (req, res) ->
        {
            //extracting parameters from requests
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int score = Integer.parseInt(req.queryParams("score"));
            //dangerous code try connecting to database
            try(Connection conn = DriverManager.getConnection(dbUrl))
            {
                //query to be run
                String sql = "INSERT INTO scores(first_name, second_name, score) VALUE(?, ?, ?)";
                //dangerous code to run
                try(PreparedStatement preparedStatement = conn.prepareStatement(sql))
                {
                    //setting variables
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setInt(3, score);
                    //executing update query
                    preparedStatement.executeUpdate();
                }
                //returning message as json
                return gson.toJson("Notice: Score added successfully.");
            }
            //handling exception
            catch(SQLException e)
            {
                //printing errors
                System.out.println("Error: " + e);
                return gson.toJson("Error retrieving score: " + e.getMessage());
            }
        });
        
        //GET endpoint specific person's score
        Spark.get("/scores/:firstName/:secondName", (req, res) ->
        {
            //extracting parameters from url
            String firstName = req.params("firstName");
            String lastName = req.params("lastName");
            //connection to database
            try(Connection conn = DriverManager.getConnection(dbUrl); PreparedStatement preparedStatement = conn.prepareStatement("SELECT score FROM scores WHERE first_name = ? AND second_name = ?"))
            {
                //preparing sql statements to be executed
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);

                ResultSet resultSet = preparedStatement.executeQuery();
                //checking if person exists in database
                if(resultSet.next())
                {
                    //returning results as json
                    return gson.toJson(resultSet.getInt("score"));
                }
                else
                {
                    return gson.toJson("Notice: Person not found.")
                }
            }
            //handling exception
            catch(SQLException e)
            {
                //printing errors
                System.out.println("Error: " + e);
                return gson.toJson("Error retrieving score: " + e.getMessage());
            }
        });

        //GET endpoint for top scorers
        Spark.get("/topScorers", (req, res) ->
        {
            List<Person> topScorers = new ArrayList<>();
            int maxScore = 0;
            //dangerous code, connection to database
            try(Connection conn = DriverManager.getConnection((dbUrl)); Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT* FROM scores"))
            {
                //go through all results presented
                while(resultSet.next())
                {
                    int score = resultSet.getInt("score");
                    //checking for a new max score
                    if(score > maxScore)
                    {
                        //updating new max score
                        maxScore = score;
                        topScorers.clear();
                    }
                    //add to list if score is equal to the highest
                    if(score == maxScore)
                    {
                        topScorers.add(new Person(resultSet.getString("first_name"), resultSet.getString("second_name"), score));
                    }
                }
                //sorting in alphabetical order
                Collections.sort(topScorers);
                //returning list as json
                return gson.toJson(topScorers);
            }
            //handling exception
            catch(SQLException e)
            {
            //printing errors
            System.out.println("Error: " + e);
            return gson.toJson("Error retrieving score: " + e.getMessage());
        }
        });
    }
}
