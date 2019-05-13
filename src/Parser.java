import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;
import javax.swing.JFileChooser;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

    /**
     * Top level parse method, called by the World
     */
    static BaseNode parseFile(File code) {
        Scanner scan = null;
        try {
            scan = new Scanner(code);

            // the only time tokens can be next to each other is
            // when one of them is one of (){},;
            scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

            BaseNode n = parseProgram(scan); // You need to implement this!!!

            scan.close();
            return n;
        } catch (FileNotFoundException e) {
            System.out.println("Robot program source file not found");
        } catch (ParserFailureException e) {
            System.out.println("Parser error:");
            System.out.println(e.getMessage());
            scan.close();
        }
        return null;
    }

    /**
     * For testing the parser without requiring the world
     */

    public static void main(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {
                File f = new File(arg);
                if (f.exists()) {
                    System.out.println("Parsing '" + f + "'");
                    RobotProgramNode prog = parseFile(f);
                    System.out.println("Parsing completed ");
                    if (prog != null) {
                        System.out.println("================\nProgram:");
                        System.out.println(prog);
                    }
                    System.out.println("=================");
                } else {
                    System.out.println("Can't find file '" + f + "'");
                }
            }
        } else {
            while (true) {
                JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
                int res = chooser.showOpenDialog(null);
                if (res != JFileChooser.APPROVE_OPTION) {
                    break;
                }
                RobotProgramNode prog = parseFile(chooser.getSelectedFile());
                System.out.println("Parsing completed");
                if (prog != null) {
                    System.out.println("Program: \n" + prog);
                }
                System.out.println("=================");
            }
        }
        System.out.println("Done");
    }

    // Useful Patterns

    static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
    static Pattern OPENPAREN = Pattern.compile("\\(");
    static Pattern CLOSEPAREN = Pattern.compile("\\)");
    static Pattern OPENBRACE = Pattern.compile("\\{");
    static Pattern CLOSEBRACE = Pattern.compile("\\}");
    static Pattern MOVE = Pattern.compile("move");
    static Pattern TURNL = Pattern.compile("turnL");
    static Pattern TURNR = Pattern.compile("turnR");
    static Pattern TAKEFUEL = Pattern.compile("takeFuel");
    static Pattern WAIT = Pattern.compile("wait");

    /**
     * PROG ::= STMT+
     */
    private static BaseNode parseProgram(Scanner s) {
        BaseNode toReturn = null;
        while (s.hasNext()) {
            if (toReturn == null) {
                toReturn = parseStmt(s);
            } else {
                toReturn = addToTree(toReturn,s);
            }
        }
        return toReturn;
    }


    // Tree Access Methods

    private static BaseNode addToTree(BaseNode root, Scanner s) {
        BaseNode currentTreeLocation = root;
        while(currentTreeLocation.getChild()!=null){
            currentTreeLocation = currentTreeLocation.getChild();
        }
        currentTreeLocation.setChild(parseStmt(s));
        if(currentTreeLocation.getChild()!=null) {
            currentTreeLocation.getChild().setParent(currentTreeLocation);
        }
        while(currentTreeLocation.getParent()!=null){
            currentTreeLocation = currentTreeLocation.getParent();
        }
        return currentTreeLocation;
    }

    // utility methods for the parser

    private static BaseNode parseStmt(Scanner s) {
        BaseNode toReturn;
        if (checkFor(";", s)) {
            return null;
        }
        toReturn = parseAct(s);
        if (toReturn == null) {
            toReturn = parseLoop(s);
        }
        if (toReturn == null) {
            fail("parseStmt error", s);
        }
        return toReturn;
    }

    private static BaseNode parseLoop(Scanner s) {
        return null;
    }

    private static BaseNode parseAct(Scanner s) {
        BaseNode toReturn = null;
        if (checkFor(MOVE, s)) {
            toReturn = new ActNode(ActNode.actionType.move);
        } else if (checkFor(TURNL, s)) {
            toReturn = new ActNode(ActNode.actionType.turnL);
        } else if (checkFor(TURNR, s)) {
            toReturn = new ActNode(ActNode.actionType.turnR);
        } else if (checkFor(TAKEFUEL, s)) {
            toReturn = new ActNode(ActNode.actionType.takeFuel);
        } else if (checkFor(WAIT, s)) {
            toReturn = new ActNode(ActNode.actionType.wait);
        } else {
            fail("parseAct error", s);
        }
        return toReturn;
    }

    /**
     * Report a failure in the parser.
     */
    static void fail(String message, Scanner s) {
        String msg = message + "\n   @ ...";
        for (int i = 0; i < 5 && s.hasNext(); i++) {
            msg += " " + s.next();
        }
        throw new ParserFailureException(msg + "...");
    }

    /**
     * Requires that the next token matches a pattern if it matches, it consumes
     * and returns the token, if not, it throws an exception with an error
     * message
     */
    static String require(String p, String message, Scanner s) {
        if (s.hasNext(p)) {
            return s.next();
        }
        fail(message, s);
        return null;
    }

    static String require(Pattern p, String message, Scanner s) {
        if (s.hasNext(p)) {
            return s.next();
        }
        fail(message, s);
        return null;
    }

    /**
     * Requires that the next token matches a pattern (which should only match a
     * number) if it matches, it consumes and returns the token as an integer if
     * not, it throws an exception with an error message
     */
    static int requireInt(String p, String message, Scanner s) {
        if (s.hasNext(p) && s.hasNextInt()) {
            return s.nextInt();
        }
        fail(message, s);
        return -1;
    }

    static int requireInt(Pattern p, String message, Scanner s) {
        if (s.hasNext(p) && s.hasNextInt()) {
            return s.nextInt();
        }
        fail(message, s);
        return -1;
    }

    /**
     * Checks whether the next token in the scanner matches the specified
     * pattern, if so, consumes the token and return true. Otherwise returns
     * false without consuming anything.
     */
    static boolean checkFor(String p, Scanner s) {
        if (s.hasNext(p)) {
            s.next();
            return true;
        } else {
            return false;
        }
    }

    static boolean checkFor(Pattern p, Scanner s) {
        if (s.hasNext(p)) {
            s.next();
            return true;
        } else {
            return false;
        }
    }

}

// You could add the node classes here, as long as they are not declared public (or private)
