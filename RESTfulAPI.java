import spark.Spark;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RESTfulAPI
{
    private static final String dbUrl = "jdbc:sqlite:testdata.db";
    private static final Gson gson = new Gson();

    public static void main(String[] args)
    {
        //starting server
        Spark.port(4567);
        //POST endpoint adding new scores
        Spark.post("/scores", (req, res) ->
        {
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int score = Integer.parseInt(req.queryParams("score"));

            try(Connection conn = DriverManager.getConnection(dbUrl))
            {
                String sql = "INSERT INTO scores(first_name, second_name, score) VALUE(?, ?, ?)";
                
                try(PreparedStatement preparedStatement = conn.prepareStatement(sql))
                {
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setInt(3, score);
                    //executing update query
                    preparedStatement.executeUpdate();
                }
            }
            return gson.toJson("Notice: Score added successfully.");
        });
        
        //GET endpoint specific person's score
        Spark.get("/scores/:firstName/:secondName", (req, res) ->
        {
            String firstName = req.params("firstName");
            String lastName = req.params("lastName");
            
            try(Connection conn = DriverManager.getConnection(dbUrl); PreparedStatement preparedStatement = conn.prepareStatement("SELECT score FROM scores WHERE first_name = ? AND second_name = ?"))
            {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);

                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next())
                {
                    return gson.toJson(resultSet.getInt("score"));
                }
                else
                {
                    return gson.toJson("Notice: Person not found.")
                }
            }
        });

        //GET endpoint for top scorers
        Spark.get("/topScorers", (req, res) ->
        {
            List<Person> topScorers = new ArrayList<>();
            int maxScore = 0;

            try(Connection conn = DriverManager.getConnection((dbUrl); Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT* FROM scores"))
            {
                while(resultSet.next())
                {
                    int score = resultSet.getInt("score");
                    if(score > maxScore)
                    {
                        maxScore = score;
                        topScorers.clear();
                    }
                    if(score == maxScore)
                    {
                        topScorers.add(new Person(resultSet.getString("first_name"), resultSet.getString("second_name"), score));
                    }
                }
                Collections.sort(topScorers);
            }
            return gson.toJson(topScorers);
        });
    }
}
