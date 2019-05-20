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
    static Pattern COMMA = Pattern.compile(",");
    static Pattern OPENPAREN = Pattern.compile("\\(");
    static Pattern CLOSEPAREN = Pattern.compile("\\)");
    static Pattern OPENBRACE = Pattern.compile("\\{");
    static Pattern CLOSEBRACE = Pattern.compile("\\}");
    static Pattern MOVE = Pattern.compile("move");
    static Pattern TURNL = Pattern.compile("turnL");
    static Pattern TURNR = Pattern.compile("turnR");
    static Pattern TAKEFUEL = Pattern.compile("takeFuel");
    static Pattern WAIT = Pattern.compile("wait");
    static Pattern LOOP = Pattern.compile("loop");
    static Pattern SHEILDON = Pattern.compile("shieldOn");
    static Pattern SHIELDOFF = Pattern.compile("shieldOff");
    static Pattern IF = Pattern.compile("if");
    static Pattern WHILE = Pattern.compile("while");
    static Pattern TURNAROUND = Pattern.compile("turnAround");
    static Pattern ELSE = Pattern.compile("else");
    static Pattern AND = Pattern.compile("and");
    static Pattern OR = Pattern.compile("or");
    static Pattern NOT = Pattern.compile("not");
    static Pattern ADD = Pattern.compile("add");
    static Pattern SUB = Pattern.compile("sub");
    static Pattern MUL = Pattern.compile("mul");
    static Pattern DIV = Pattern.compile("div");

    /**
     * PROG ::= STMT+
     */
    private static BaseNode parseProgram(Scanner s) {
        BaseNode toReturn = null;
        while (s.hasNext()) {
            if (toReturn == null) {
                toReturn = parseStmt(s);
            } else {
                BaseNode toAdd = parseStmt(s);
                toReturn = addToTree(toReturn, toAdd);
            }
        }
        return toReturn;
    }


    // Tree Access Methods

    private static ArrayList<BaseNode> addToBlock(BlockNode root, Scanner s) {
        BaseNode temp = parseStmt(s);
        if (temp instanceof PayloadNode || root.block.size() == 0) {
            root.addNewBranch(temp);
        } else {
            root.setBranch(addToTree(root.getBlock().get(root.currentBranchInUse), temp));
        }
        return root.getBlock();
    }

    private static BaseNode addToTree(BaseNode root, BaseNode itemToAdd) {
        BaseNode currentTreeLocation = root;
        // Finds the end of the tree
        while (currentTreeLocation.getChild() != null) {
            currentTreeLocation = currentTreeLocation.getChild();
        }
        // Adds a leaf
        currentTreeLocation.setChild(itemToAdd);
        if (currentTreeLocation.getChild() != null) {
            // sets the leaf's parent
            currentTreeLocation.getChild().setParent(currentTreeLocation);
        }
        // Move to the root of the tree and returns it
        while (currentTreeLocation.getParent() != null) {
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
            if (toReturn instanceof IfNode) {
                if (checkFor(ELSE, s)) {
                    ElseNode temp = ParseElse(s);
                    temp.setParent(toReturn);
                    toReturn.setChild(temp);
                }
            }
        }
        if (toReturn == null) {
            fail("parseStmt error", s);
        }
        return toReturn;
    }

    private static ElseNode ParseElse(Scanner s) {
        ElseNode toReturn = null;
        require(OPENBRACE, "Invalid else", s);
        toReturn = new ElseNode();
        toReturn.setPayloadNodeSubTree(parseBlock(s));
        return toReturn;
    }

    private static BlockNode parseBlock(Scanner s) {
        BlockNode toReturn = new BlockNode();
        while (!checkFor(CLOSEBRACE, s) && s.hasNext()) {
            if (toReturn.getBlock() == null) {
                ArrayList<BaseNode> toAdd = new ArrayList<>();
                toAdd.add(parseStmt(s));
                toReturn.setBlock(toAdd);
            } else {
                toReturn.setBlock(addToBlock(toReturn, s));
            }
        }
        return toReturn;
    }

    private static BaseNode parseLoop(Scanner s) {
        PayloadNode toReturn = null;
        if (checkFor(LOOP, s)) {
            toReturn = new LoopNode();
            toReturn.setPayloadNodeSubTree(new BlockNode());
            if (checkFor(OPENBRACE, s)) {
                toReturn.setPayloadNodeSubTree(parseBlock(s));
            }
            return toReturn;
        } else {
            if (checkFor(IF, s)) {
                toReturn = new IfNode();
            } else if (checkFor(WHILE, s)) {
                toReturn = new WhileNode();
            } else {
                // if no appropriate statements are found an error is reported
                fail("No Valid payload node found", s);
                return null;
            }
            toReturn.setPayloadNodeSubTree(new BlockNode());
            require(OPENPAREN, "Invalid statement format(No Open Paren)", s);
            toReturn.setCondition(parseCondition(s));
            require(CLOSEPAREN, "No Closing Paren Found", s);
            require(OPENBRACE, "No Open Brace Found", s);
            toReturn.setPayloadNodeSubTree(parseBlock(s));
            return toReturn;
        }
    }

    private static Condition parseCondition(Scanner s) {
        Condition toReturn = null;
        Condition left = null;
        Condition right = null;
        if (checkFor(AND, s)) {
            require(OPENPAREN, "Invalid statement format(No Open Paren)", s);
            left = parseCondition(s);
            require(COMMA, "No Comma Found, Invalid Condition", s);
            right = parseCondition(s);
            require(CLOSEPAREN, "No Closing Paren Found", s);
            toReturn = new AndNode(left, right);
        } else if (checkFor(OR, s)) {
            require(OPENPAREN, "Invalid statement format(No Open Paren)", s);
            left = parseCondition(s);
            require(COMMA, "No Comma Found, Invalid Condition", s);
            right = parseCondition(s);
            require(CLOSEPAREN, "No Closing Paren Found", s);
            toReturn = new OrNode(left, right);
        } else if (checkFor(NOT, s)) {
            require(OPENPAREN, "No opening parentheses found", s);
            toReturn = new NotNode(parseCondition(s));
            require(CLOSEPAREN, "No Closing Paren Found", s);
        } else {
            for (Renlop.renlopTypes R : Renlop.renlopTypes.values()) {
                if(checkFor(R.toString(),s)) {
                    require(OPENPAREN, "Invalid statement format(No Open Paren)", s);
                    Expression expLeft = parseExp(s);
                    require(COMMA, "No Comma Found", s);
                    Expression expRight = parseExp(s);
                    require(CLOSEPAREN, "No Closing Paren Found", s);
                    toReturn = new RenlopCondition(new Renlop(R), expLeft, expRight);
                }
            }
        }
        if (toReturn == null) {
            fail("Invalid Condition", s);
        }

        if (toReturn != null) {
            return toReturn;
        }
        return null;
    }

    private static Expression parseExp(Scanner s) {

        if (s.hasNext(NUMPAT)) {
            return new Number(requireInt(NUMPAT, "No Number Found", s));
        } else if (checkFor(ADD, s)) {
            require(OPENPAREN, "Invalid Expression", s);
            Expression left = parseExp(s);
            require(COMMA, "Invalid Expression, No Comma", s);
            Expression right = parseExp(s);
            require(CLOSEPAREN, "Invalid expression, no close paren", s);
            return new Add(left, right);
        } else if (checkFor(SUB, s)) {
            require(OPENPAREN, "Invalid Expression", s);
            Expression left = parseExp(s);
            require(COMMA, "Invalid Expression, No Comma", s);
            Expression right = parseExp(s);
            require(CLOSEPAREN, "Invalid expression, no close paren", s);
            return new Subtraction(left, right);
        } else if (checkFor(MUL, s)) {
            require(OPENPAREN, "Invalid Expression", s);
            Expression left = parseExp(s);
            require(COMMA, "Invalid Expression, No Comma", s);
            Expression right = parseExp(s);
            require(CLOSEPAREN, "Invalid expression, no close paren", s);
            return new Multiply(left, right);
        } else if (checkFor(DIV, s)) {
            require(OPENPAREN, "Invalid Expression", s);
            Expression left = parseExp(s);
            require(COMMA, "Invalid Expression, No Comma", s);
            Expression right = parseExp(s);
            require(CLOSEPAREN, "Invalid expression, no close paren", s);
            return new Divide(left, right);
        } else {
            for (Sen.senType S : Sen.senType.values()) {
                if (checkFor(S.toString(), s)) {
                    return new Sen(S);
                }
            }
            return null;
        }
    }

    private static Sen parseSensor(Scanner s) {
        for (Sen.senType senType : Sen.senType.values()) {
            if (checkFor(senType.toString(), s)) {
                return new Sen(senType);
            }
        }
        fail("No valid sensor", s);
        return null;
    }

    private static Renlop parseRenlop(Scanner s) {
        for (Renlop.renlopTypes r : Renlop.renlopTypes.values()) {
            if (checkFor(r.name(), s)) {
                return new Renlop(r);
            }
        }
        fail("No Valid Renlop Found", s);
        return null;
    }

    private static BaseNode parseAct(Scanner s) {
        ActNode toReturn = null;
        if (checkFor(MOVE, s)) {
            toReturn = new ActNode(ActNode.actionType.move);
            if (checkFor(OPENPAREN, s)) {
                Expression actExpression = parseExp(s);
                toReturn.setActArgument(actExpression);
                require(CLOSEPAREN, "Invalid format, No Closing Paren", s);
            }
        } else if (checkFor(TURNL, s)) {
            toReturn = new ActNode(ActNode.actionType.turnL);
        } else if (checkFor(TURNR, s)) {
            toReturn = new ActNode(ActNode.actionType.turnR);
        } else if (checkFor(TAKEFUEL, s)) {
            toReturn = new ActNode(ActNode.actionType.takeFuel);
        } else if (checkFor(WAIT, s)) {
            toReturn = new ActNode(ActNode.actionType.wait);
            if (checkFor(OPENPAREN, s)) {
                Expression actExpression = parseExp(s);
                toReturn.setActArgument(actExpression);
                require(CLOSEPAREN, "Invalid format, No Closing Paren", s);
            }
        } else if (checkFor(SHEILDON, s)) {
            toReturn = new ActNode(ActNode.actionType.shieldOn);
        } else if (checkFor(SHIELDOFF, s)) {
            toReturn = new ActNode(ActNode.actionType.shieldOff);
        } else if (checkFor(TURNAROUND, s)) {
            toReturn = new ActNode(ActNode.actionType.turnAround);
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
