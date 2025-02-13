import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DatabaseIntegration
{
    public static void main(String[] args)
    {
        //ref to csv file
        String csvFile = "TestData.csv";
        //using jdbc to create connection to database
        String url = "jdbc:sqlite:testdata.db";
        //try and catch dangerous code
        try(Connection conn = DriverManager.getConnection(url); Statement statement = conn.createStatement())
        {
            //creating table
            String sql = "CREATE TABLE IF NOT EXISTS scores (\n" +
                    "first_name TEXT NOT NULL,\n" +
                    "second_name TEXT NOT NULL,\n" +
                    "score INTEGER NOT NULL\n" +
                    ");";
            //execute query statement
            statement.execute(sql);
            //read csv file and insert data into database
            try(BufferedReader br = new BufferedReader(new FileReader(csvFile)))
            {
                String l;
                //skip header
                br.readLine();
                //loop to run until there's no lines left
                while((l = br.readLine()) != null)
                {
                    //splitting data
                    String[] data = l.split(",");
                    //checking first if all 'Person' characteristics are present
                    if(data.length == 3)
                    {
                        //separating 'Person' characteristics
                        String firstName = data[0];
                        String lastName = data[1];
                        int score = Integer.parseInt(data[2]);
                        //sql insert query to run
                        String insertQuery = "INSERT INTO score(first_name, second_name, score) VALUES(?, ?, ?)";
                        //try dangerous code to run
                        try(PreparedStatement preparedStatement = conn.prepareStatement(sql))
                        {
                            //setting all elements in table
                            preparedStatement.setString(1, firstName);
                            preparedStatement.setString(2, lastName);
                            preparedStatement.setInt(3, score);
                            //executing update
                            preparedStatement.executeUpdate();
                        }
                    }
                }
            }
            //std out
            System.out.println("Notice: Data has been wrtitten to database.");
        }
        //catching exceptions
        catch(SQLException | IOException e)
        {
            //printing exception errors
            System.out.println("Error: " + e);
        }
    }
}