import java.io.Console;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author Eliezer Meth
 * @version 1
 * Start Date: 05.08.2020
 * Code initially copied from https://docs.oracle.com/javase/tutorial/essential/regex/test_harness.html
 */

public class RegexTestHarness
{
    Console console;

    public RegexTestHarness()
    {
        console = System.console();
        if (console == null)
        {
            System.err.println("No console.");
            System.exit(1);
        }

        while (true)
        {
            String line = console.readLine("Enter input string to search: ");

            findProperName(line);
            findNumber(line);
            findAncestor(line);
            findTenLetterPalindrome(line);
        }
    }

    //****************************************************************
    public static void main(String[] args) { new RegexTestHarness(); }
    //****************************************************************

    // > matches by first letter being uppercase and all others lowercase
    // > will throw false positives for words that start with a capital letter
    // > will throw false positives for cases like "sdIdf" and 2 for "IntroComp"
    // > will miss parts of last names, such as von
    private void findProperName(String line)
    {
        String propNameRegex = "([A-Z]{1}[a-z]+)";

        findRegex(propNameRegex, line);
    }

    // > does not matter if has + or - in front of number
    private void findNumber(String line)
    {
        String intRegex = "\\d"; // best would be "\\D\\d\\D" and strip off the outside characters; find out how to do it
        String intNotZeroRegex = "[1-9]\\d*"; // captures "23" from "023"; fix
        String decRegex = "\\d+\\.\\d+"; // will also capture "023.45"; fix

        findRegex(intRegex, line);
        findRegex(intNotZeroRegex, line);
        findRegex(decRegex, line);
    }

    // > can give positive for "great-great-Great-great-grandFather"; can fix by splitting up; but less efficient
    // > for some reason, sometimes produces "No match found." for father; how to fix?
    // > for some reason, "(([G,g]reat-)?(great-)*)*([G,g]rand)?([Fa,fa],[Mo,mo])ther" does not work
    // > also does not work by "[(Fa),(fa)]ther" : what is the error?
    private void findAncestor(String line)
    {
        String maleRegex = "(([G,g]reat-)?(great-)*)*([G,g]rand)?[F,f]ather";
        String femaleRegex = "(([G,g]reat-)?(great-)*)*([G,g]rand)?[M,m]other";

        findRegex(maleRegex, line);
        findRegex(femaleRegex, line);
    }

    private void findTenLetterPalindrome(String line)
    {
        String palindromeRegex = "(?i)(\\w)(\\w)(\\w)(\\w)(\\w)\\5\\4\\3\\2\\1";

        findRegex(palindromeRegex, line);
    }

    private void findRegex(String regex, String line)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        findRegexUtil(matcher);
    }

    private void findRegexUtil(Matcher matcher)
    {
        boolean found = false;
        while (matcher.find())
        {
            console.format("I found the text" +
                            " \"%s\" starting at " +
                            "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());

            // added for groups
            if (matcher.groupCount() > 1) // found capturing groups
            {
                for (int i = 0; i <= matcher.groupCount(); i++)
                {
                    console.format("Group %d: %s%n", i, matcher.group(i));
                }
            }
            // end added for groups

            found = true;
        }
        if(!found)
        {
            console.format("No match found.%n");
        }
    }
}

// To run: hit run, copy command path (including quotes), place into terminal
// Need clarification: will there be whitespace surrounding each string to be found?  Current assumption not.
