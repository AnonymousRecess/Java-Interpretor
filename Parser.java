/**
 * This class is a recursive-descent parser,
 * modeled after the programming language's grammar.
 * It constructs and has-a Scanner for the program being parsed
 */
import java.util.*;

public class Parser {

	private Scanner scanner; // scanner for parsed program
	/**
	 * Checks for a match with current token
	 * @param s String to match current token with
	 * @throws SyntaxException
	 */
	private void match(String s) throws SyntaxException {
		scanner.match(new Token(s)); // calls scanner's match function with the new token from the passed in string
	}
	/**
	 * Method to return current token or error if token is null
	 * @return current token in scanner
	 * @throws SyntaxException
	 */
	private Token curr() throws SyntaxException {
		return scanner.curr(); // return current token or sytanx error if current token is null
	}
	/**
	 * Method for returning position of scanner
	 * @return position of scanner
	 */
	private int pos() {
		return scanner.pos(); // returns scanners position
	}
	/**
	 * Constructs a Node with multiplication or division based on current token
	 * @return Node constructed with multiplication and division capabilites and position and operation attributes
	 * @throws SyntaxException
	 */
	private NodeMulop parseMulop() throws SyntaxException {
		if (curr().equals(new Token("*"))) { // checks if current token is multiplication symbol
			match("*"); // checks token and increases scanner position
			return new NodeMulop(pos(), "*"); // constructs Node with multiplication and position
		}
		if (curr().equals(new Token("/"))) { // checks if current token is division symbol
			match("/"); // checks token and increases scanner position
			return new NodeMulop(pos(), "/"); // constructs Node with division and position
		}
		return null; // returns null if current token is neither multiplication or division
	}
	/**
	 * Constructs a Node with addition or subtraction based on current token
	 * @return Node constructed with addition or subtraction and position and operation attributes
	 * @throws SyntaxException
	 */
	private NodeAddop parseAddop() throws SyntaxException {
		if (curr().equals(new Token("+"))) { // checks for addition symbol
			match("+"); //  checks scanner and increases scanner position
			return new NodeAddop(pos(), "+"); // constructs Node with addition and position
		}
		if (curr().equals(new Token("-"))) { // checks for subtraction symbol
			match("-"); // checks scanner and increases position
			return new NodeAddop(pos(), "-"); // Node constructed with subtraction and position
		}
		return null; // null if current token is neither addition or subtraction
	}
	/**
	 * Parses the factors of the expression
	 * @return Node containing expression
	 * @throws SyntaxException
	 */
	private NodeFact parseFact() throws SyntaxException {
		if (curr().equals(new Token("-"))) { // unary minus check
			// 
			match("-");
			// Token unOp = curr();
			NodeFact facts = parseFact();
			// return new NodeUnaryMin(pos() , unOp.lex());
			return new NodeUnaryMin(facts);
			
		}
		if (curr().equals(new Token("("))) { // checks if current token is equal to (
			match("("); // moves position past left parenthesis
			NodeExpr expr = parseExpr(); // builds operation Node
			match(")"); // checks current token for ) moves past right parenthesis
			return new NodeFactExpr(expr); // returns built Node with Expression
		}
		if (curr().equals(new Token("id"))) { // checks if current token is an id
			Token id = curr(); // grabs current token
			match("id"); // moves scanner past id
			return new NodeFactId(pos(), id.lex()); // Build node with id and position
		}
		Token num = curr(); // if not a factor or id set token as current num
		match("num"); // ensure token is at num and move past
		return new NodeFactNum(num.lex()); // build node with integer
	}
	/**
	 * Build a NodeTerm with factors and optional ops if mult or div
	 * @return term if ops NodeTe
	 * @throws SyntaxException
	 */
	private NodeTerm parseTerm() throws SyntaxException {
		NodeFact fact = parseFact(); // builds node with expression
		NodeMulop mulop = parseMulop(); // builds node with mult/div - returns null if not mult or div token
		if (mulop == null) // not mult or div token
			return new NodeTerm(fact, null, null); // build term with no mul/div eval
		NodeTerm term = parseTerm(); // build a term
		term.append(new NodeTerm(fact, mulop, null)); // add factor and div/mult info to term
		return term; // return built term
	}
	/**
	 * Build a NodeExpr with term and  optional add/sub evals
	 * @return expr with term and eval info
	 * @throws SyntaxException
	 */
	private NodeExpr parseExpr() throws SyntaxException {
		NodeTerm term = parseTerm(); // parse the term
		NodeAddop addop = parseAddop(); // build node with add/sub op
		if (addop == null)
			return new NodeExpr(term, null, null); // build node without add/sub op
		NodeExpr expr = parseExpr();
		expr.append(new NodeExpr(term, addop, null)); // add term and add/sub info to expression
		return expr; // returns expression with add/sub/term info
	}
	/**
	 * Checks for id and = in token and makes assignment node
	 * @return
	 * @throws SyntaxException
	 */
	private NodeAssn parseAssn() throws SyntaxException {
		Token id = curr(); // sets id to current token
		match("id"); // tests token for id
		match("="); // tests token for =
		NodeExpr expr = parseExpr(); // make expression node
		NodeAssn assn = new NodeAssn(id.lex(), expr); // make assignment node with lexeme and expression
		return assn; // return assignment node
	}
	/**
	 * 
	 * @return Constructs a node statement that contains an assignment
	 * @throws SyntaxException
	 */
	private NodeStmt parseStmt() throws SyntaxException {
		NodeAssn assn = parseAssn(); // makes an expression node that conains a lexeme and expression
		match(";"); // check token for ; and position scanner after
		NodeStmt stmt = new NodeStmt(assn); // create node statement that contains the assignment
		return stmt; // returns NodeStmt
	}
	/**
	 * Creates a new scanner from source program and parses a statement. Checks for EOF then returns statement
	 * @param program Source program
	 */
	public Node parse(String program) throws SyntaxException {
		scanner = new Scanner(program); // scanner for source program
		scanner.next(); // find next token
		NodeStmt stmt = parseStmt(); // parse the statement at token position
		match("EOF"); // check if current token is EOF
		return stmt; // returns statement node
	}

}