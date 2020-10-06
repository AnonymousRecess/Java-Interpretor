// This class is a scanner for the program
// and programming language being interpreted.

import java.util.*;

public class Scanner {

	private String program; // source program being interpreted
	private int pos; // index of next char in program
	private Token token; // last/current scanned token

	// sets of various characters and lexemes
	private Set < String > whitespace = new HashSet < String > (); // initialize whitespace Set
	private Set < String > digits = new HashSet < String > (); // initialize digits Set
	private Set < String > letters = new HashSet < String > (); // initialize leters Set
	private Set < String > legits = new HashSet < String > (); // initialize legits Set
	private Set < String > keywords = new HashSet < String > (); // initialize keywords Set
	private Set < String > operators = new HashSet < String > (); // initialize operators Set
	private Set < String > comments = new HashSet < String > (); // initialize operators Set
	// initializers for previous sets

	/** 
	 * This method is responsible for filling the Scanner's lexicon for what valid tokens are.
	 * @param s This is the first paramater and specifies the Set to fill
	 * @param lo This is the second paramater and indicates the first character to fill the set with
	 * @param hi This is the third paramater and indicates the last character to add to the set
	 */
	private void fill(Set < String > s, char lo, char hi) {
		for (char c = lo; c <= hi; c++) // loop that begins at first character lo and ends after the final character hi. Increments c with each iteration.
			s.add(c + ""); // add the character to the Set
	}

	/**
	 * This method is responsible for adding tokens to the whitespace Set
	 * @param s This is the first parameter and specifies the Set to add each character to
	 */
	private void initWhitespace(Set < String > s) {
		s.add(" "); // Adds the empty space to the whitespace Set
		s.add("\n"); // Adds the newline character to the whitespace Set
		s.add("\t"); // Adds the tab character to the whitespace Set
	}

	/**
	 * This method uses the previously declared fill function to add a range of characters to the digits Set
	 * @param s This is the first parameter and specifies the Set to add each character to
	 */
	private void initDigits(Set < String > s) {
		fill(s, '0', '9'); // Pass the Set, lo('0'), and hi('9') paramaters for the fill function to add the characters in range to the Set
		s.add("."); // add double support
	}
	
	/**
	 * This method uses the previously declared fill function to add a range of characters to the letters Set
	 * @param s This is the first parameter and specifies the Set to add each character to
	 */
	private void initLetters(Set < String > s) {
		fill(s, 'A', 'Z'); // Pass Set A and Z to the fill function
		fill(s, 'a', 'z'); // Pass Set a and z to the fill function
	}

	/**
	 * This method is responsible for adding the letters and digits Set to the legits Set
	 * @param s This is the first parameter and specifies the set to add to
	 */
	private void initLegits(Set < String > s) {
		s.addAll(letters); // add letters to the legits set
		s.addAll(digits); // add digits to the legits set
	}

	/**
	 * This method is responsible for adding characters to the operators Set
	 * @param s This is the first parameter and specifies the set to add to
	 */
	private void initOperators(Set < String > s) {
		s.add("="); // add the = operator to the Set
		s.add("+"); // add the + operator to the Set
		s.add("-"); // add the - operator to the Set
		s.add("*"); // add the * operator to the Set
		s.add("/"); // add the / operator to the Set
		s.add("("); // add the ( operator to the Set
		s.add(")"); // add the ) operator to the Set
		s.add(";"); // add the ; operator to the Set
	}

	private void initKeywords(Set < String > s) {}

	private void initComments(Set < String > s) {
		s.add("#");
	}
	/** 
	 * This method initializes program, position and token variables and also calls the set initializer functions
	 *  @param program This is the program sent to the Scanner as args[0]
	*/
	public Scanner(String program) {
		this.program = program; // instantiates program variable
		pos = 0; // initializes position to 0
		token = null; // sets token default to null
		initWhitespace(whitespace); // initializes data in whitespace
		initDigits(digits); // initializes data in digits
		initLetters(letters); // initializes data in letters
		initLegits(legits); // intitializes data in legits
		initKeywords(keywords); // initializes data in keywords
		initOperators(operators); // initializes data in operators
		initComments(comments);
	}

	// handy string-processing methods
	/**
	 * This method checks if the scanner is finished
	 * @return Returns true if the scanner has reached the end of the program
	 */
	public boolean done() {
		return pos >= program.length(); // true or false return based on position value in relation to program length
	}
	/**
	 * advances position until position no longer contains set character or lexeme or is at end of source program
	 * @param s Set containing characters and lexemes
	 */
	private void many(Set < String > s) {
		while (!done() && s.contains(program.charAt(pos) + ""))
			pos++;
	}
	/**
	 * This method advances the position until past the first occurance of the character passed in
	 * @param c character to search program at position for
	 */
	private void past(char c) {
		while (!done() && c != program.charAt(pos)) 
			pos++; // addvance value of position by one until char is found or scanner is at end
		if (!done() && c == program.charAt(pos))
			pos++; // advances once more after character is found
	}

	// scan various kinds of lexeme
	/**
	 * creates token from a string of ints in source program
	 */
	private void nextNumber() {
		int old = pos; // beginning position of int 
		many(digits); // finds ending position of int 
		token = new Token("num", program.substring(old, pos)); // assigns num identifier and content of int lexeme to token
	}
	/**
	 * creates token from a string of keyword lexemes in the source program
	 */
	private void nextKwId() {
		int old = pos; // beginning position of keyword lexeme
		many(letters); // finds end position for letter
		many(legits); // finds end position for legits
		String lexeme = program.substring(old, pos); // creates lexeme from ende position
		token = new Token((keywords.contains(lexeme) ? lexeme : "id"), lexeme); // creates token as id or ? identifier from lexeme
	}
	/**
	 * Scans source program for operator and creates a token 
	 */
	private void nextOp() {
		int old = pos; // begining position for operator
		pos = old + 2; // advance position by two for end of substring
		if (!done()) {
			String lexeme = program.substring(old, pos); // create lexeme from substring 
			if (operators.contains(lexeme)) {
				token = new Token(lexeme); // two-char operator
				return;
			}
		}
		pos = old + 1; // increment position
		String lexeme = program.substring(old, pos); // create substring for string lexeme
		token = new Token(lexeme); // one-char operator
	}


	/**
	 * This method determines the kind of the next token (e.g., "id"),
	 * and calls a method to scan that token's lexeme (e.g., "foo").
	 * @return true if error
	 */
	public boolean next() {
		many(whitespace); //pass by whitespaces
		if (done()) { // if at end of program to scan
			token = new Token("EOF"); // make end of file token
			return false; // signal end of scanning
		}
		String c = program.charAt(pos) + ""; // character at scanner
		if (digits.contains(c)) // check if character is in digit Set
			nextNumber();  // create a token with nums identifier 
		else if (letters.contains(c)) // check if character is in letter Set
			nextKwId(); // create keyword token
		else if (operators.contains(c)) // check if c is in operators Set
			nextOp();  // create a operator token 
	
		else if (comments.contains(c))
			{
				pos++;
				past(c.charAt(0));
				next();
			}
		else {
			System.err.println("illegal character at position " + pos); // not a valid token
			pos++; // increment scanner past invalid character
			return next(); // check next character
		}
		return true; // keep on scanning
	}

	/**
	 * This method scans the next lexeme,
	 * if the current token is the expected token.
	 * @param t 
	 */
	public void match(Token t) throws SyntaxException {
		if (!t.equals(curr())) // check if token is equal to expected
			throw new SyntaxException(pos, t, curr()); // throws error if token does not match what parser expects
		next(); // advance scanner if match
	}
	/**
	 * Throws error if token was not set
	 * @return current token
	 * @throws SyntaxException
	 */
	public Token curr() throws SyntaxException {
		if (token == null) // checks if token is set to default null
			throw new SyntaxException(pos, new Token("ANY"), new Token("EMPTY")); // error if current token is null
		return token; // return current token
	}
	/**
	 * Getter for position
	 * @return integer containing position of scanner
	 */
	public int pos() {
		return pos; // position of scanner
	}

	
	/**
	 * for unit testing - creates a scanner and scans through source program
	 * @param args is the source program to be scanned
	 */
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(args[0]); // scanner creation with system argument to scan through
			while (scanner.next())
				System.out.println(scanner.curr()); // print current token
		} catch (SyntaxException e) {
			System.err.println(e); // print error to standard error
		}
	}

}
