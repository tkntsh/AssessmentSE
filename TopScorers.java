import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopScorers
{
    public static void main(String[] args)
    {
        //storing data in string from csv file
        String csvFile = "TestData.csv";
        //creating array to store object of type Person
        List<Person> people = new ArrayList<>();
        //try and catch for dangerous code
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile)))
        {
            String l;
            //skipping header
            br.readLine();
            while((l = br.readLine()) != null)
            {
                //split data using ',' comma delimiter
                String[] data = l.split(",");
                //checking if all data is present before adding
                if(data.length == 3)
                {
                    //adding data to Person object
                    people.add(new Person(data[0], data[1], Integer.parseInt(data[2])));
                }
            }
        }
        //catching exception
        catch(IOException e)
        {
            //printing exception
            System.out.println("Error: " + e);
        }
        //finding top scorers using findMaxScore(), findTopScorers() method
        int maxScore = findMaxScore(people);
        List<Person> topScorers = findTopScorers(people, maxScore);
        //std out
        System.out.println("Top Scorers:");
        System.out.println("Scorer: " + maxScore);
        //sorting in alphabetical order
        Collections.sort(topScorers);

        for(Person person : topScorers)
        {
            System.out.println(person.firstName + " " + person.lastName);
        }
    }
    //method to find max score
    private static int findMaxScore(List<Person> people)
    {
        int maxScore = Integer.MIN_VALUE;
        //loop running until there's no people left in list
        for(Person person : people)
        {
            //checking if the current person's score is greater than the current maxScore value
            if(person.score > maxScore)
            {
                //if so current person's score = maxScore
                maxScore = person.score;;
            }
        }
        //returning the maxScore
        return maxScore;
    }
    //method to find all people with max score
    private static List<Person> findTopScorers(List<Person> people, int maxScore)
    {
        List<Person> topScorers = new ArrayList<>();
        //running until there's no people in the list
        for(Person person : people)
        {
            //checking if current person's score = to the max score
            if(person.score == maxScore)
            {
                //adding that person to the list of top scorers
                topScorers.add(person);
            }
        }
        //returning list of topScorers
        return topScorers;
    }
}

class Person implements Comparable<Person>
{
    //characteristics of a person
    String firstName;
    String lastName;
    int score;
    //constructor
    public Person(String firstName, String lastName, int score)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.score = score;
    }

    @Override
    public int compareTo(Person other)
    {
        //compare by first name then last name
        int firstNameCom = this.firstName.compareTo(other.firstName);
        if(firstNameCom != 0)
        {
            return firstNameCom;
        }
        //if first names are the same, compare by last name
        return this.lastName.compareTo(other.lastName);
    }
}
